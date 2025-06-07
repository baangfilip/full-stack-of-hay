package com.fullstackofhay.logic;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.glassfish.hk2.api.Immediate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fullstackofhay.entities.TemperatureRecord;
import com.fullstackofhay.entities.VoltageRecord;
import com.google.gson.Gson;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Immediate
@Singleton
public class KafkaConsumerService {
    private static final Logger log =
        LoggerFactory.getLogger(KafkaConsumerService.class);
    private final KafkaConsumer<String,String> consumer;
    private final Thread        thread;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final TemperatureLogic tempLogic;
    private final VoltageLogic voltLogic;
    @Inject
    private Gson gson;
    @Inject
    public KafkaConsumerService(TemperatureLogic tempLogic, VoltageLogic voltLogic) {
        this.tempLogic = tempLogic;
        this.voltLogic = voltLogic;
        Config cfg = ConfigFactory.load();
        Properties pc = new Properties();
        pc.put("bootstrap.servers", cfg.getString("kafka.bootstrap.servers"));
        pc.put("group.id",          cfg.getString("kafka.group.id"));
        pc.put("key.deserializer",   "org.apache.kafka.common.serialization.StringDeserializer");
        pc.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(pc);
        this.consumer.subscribe(List.of("incoming-temperatures", "incoming-voltage"));
        this.thread = new Thread(this::pollLoop, "kafka-consumer-thread");
        this.thread.setDaemon(true);
    }

    @PostConstruct
    public void start() {
        thread.start();
    }

    private void pollLoop() {
        try {
            while (running.get()) {
                ConsumerRecords<String,String> records =
                  consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String,String> rec : records) {
                    handleRecord(rec);
                }
                consumer.commitSync();
            }
        } catch (WakeupException e) {
        } finally {
            log.info("Kafka consumer close");
            consumer.close();
        }
    }

    private void handleRecord(ConsumerRecord<String,String> record) {
        log.info("Consumed topic={} partition={} offset={} key={} value={}",
                 record.topic(),
                 record.partition(),
                 record.offset(),
                 record.key(),
                 record.value());
        if(record.topic().equals("incoming-temperatures")){
            try{
                TemperatureRecord temp = gson.fromJson(record.value(), TemperatureRecord.class);
                tempLogic.addTemperature(temp);
            }catch(Exception e){
                log.info("Couldnt parse message value to tempareature", record.value());
                e.printStackTrace();
            }
        }else if(record.topic().equals("incoming-voltage")){
            try{
                VoltageRecord temp = gson.fromJson(record.value(), VoltageRecord.class);
                voltLogic.addVoltage(temp);
            }catch(Exception e){
                log.info("Couldnt parse message value to voltage", record.value());
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void stop() {
        running.set(false);
        consumer.wakeup();
        try {
            thread.join();
        } catch (InterruptedException ignored) {}
    }
}
