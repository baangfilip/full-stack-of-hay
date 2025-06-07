package com.fullstackofhay.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.inject.Inject;

import java.sql.Date;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fullstackofhay.entities.TemperatureRecord;
import com.fullstackofhay.entities.Temperatures;
import com.fullstackofhay.entities.VoltageRecord;
import com.fullstackofhay.logic.VoltageLogic;

@Path("/voltage")
public class VoltageResource {
    private static final Logger log =
        LoggerFactory.getLogger(TemperatureResource.class);
  private final Producer producer;
  private final VoltageLogic voltageLogic;

  @Inject
  public VoltageResource(Producer producer,
                       VoltageLogic voltageLogic) {
    this.producer = producer;
    this.voltageLogic = voltageLogic;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTemps() {
    return Response.ok(voltageLogic.fetchAllVoltages()).build();
  }

  @GET
  @Path("spam-voltages/{amount}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response spamTemps(@PathParam("amount") int amount) {
    log.info("spam volt");
    for(int i = 0; i < amount; i++){
      double tempCelsius = Math.random() * 30 + 1;
      producer.send(new org.apache.kafka.clients.producer.ProducerRecord<>("incoming-voltage","","{\n" + //
      "\t\"sensordate\": \"2025-12-12 12:12:12\",\n" + //
      "\t\"voltage\": "+tempCelsius+",\n" + //
      "\t\"messageID\": "+i+"\n" + //
              "}"));
      log.info("spammed message", i);
    }
    return Response.ok("Spammed done: " + amount).build();
  }

  @GET
  @Path("voltage-above/{above-volt}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response aboveTemp(@PathParam("above-volt") int aboveVolt) {
    return Response.ok(voltageLogic.getVoltagesAbove(aboveVolt)).build();
  }


  @GET
  @Path("/add")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addTemp() {
    double voltage = Math.random() * 30 + 1;
    VoltageRecord newvolt = new VoltageRecord(0, (float) voltage, new Date(System.currentTimeMillis()), null);
    VoltageRecord temp = voltageLogic.addVoltage(newvolt);
    return Response.ok(temp).build();
  }
}
