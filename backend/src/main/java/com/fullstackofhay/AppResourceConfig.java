package com.fullstackofhay;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.hk2.api.Immediate;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;

import jakarta.inject.Singleton;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.fullstackofhay.logic.TemperatureLogic;
import com.fullstackofhay.logic.VoltageLogic;
import com.google.gson.Gson;
import com.fullstackofhay.dao.TemperatureDAO;
import com.fullstackofhay.dao.VoltageDAO;
import com.fullstackofhay.logic.KafkaConsumerService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Properties;

@ApplicationPath("/api")
public class AppResourceConfig extends ResourceConfig {
  
  @Inject
  public AppResourceConfig(ServiceLocator locator) {

    ServiceLocatorUtilities.enableImmediateScope(locator);
    
    packages(
      "com.fullstackofhay.resources");

    register(JacksonFeature.class);

    register(new AbstractBinder() {
      @Override 
      protected void configure() {
        Config cfg = ConfigFactory.load();
        HikariConfig hc = new HikariConfig();
        hc.setJdbcUrl(   cfg.getString("db.url"));
        hc.setUsername(  cfg.getString("db.user"));
        hc.setPassword(  cfg.getString("db.pass"));
        hc.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        DataSource ds = new HikariDataSource(hc);
        bind(ds).to(DataSource.class).in(Singleton.class);

        Properties pp = new Properties();
        pp.put("bootstrap.servers", cfg.getString("kafka.bootstrap.servers"));
        pp.put("key.serializer",   "org.apache.kafka.common.serialization.StringSerializer");
        pp.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String,String> producer = new KafkaProducer<>(pp);
        bind(producer)
            .to(Producer.class)
            .in(Singleton.class);

        bind(TemperatureLogic.class)
          .to(TemperatureLogic.class);
        bind(VoltageLogic.class)
          .to(VoltageLogic.class)
          .in(Singleton.class);
        bind(TemperatureDAO.class)
          .to(TemperatureDAO.class);
        bind(VoltageDAO.class)
          .to(VoltageDAO.class)
          .in(Singleton.class);
        bindAsContract(KafkaConsumerService.class)
          .in(Immediate.class);
        Gson gson = new GsonProducer().gson();
        bind(gson).to(Gson.class).in(Singleton.class);

      }
    });
  }
}