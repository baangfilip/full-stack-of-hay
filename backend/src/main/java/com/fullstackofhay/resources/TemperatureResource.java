package com.fullstackofhay.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.inject.Inject;

import java.sql.Date;
import java.util.List;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fullstackofhay.entities.Temperature;
import com.fullstackofhay.entities.Temperatures;
import com.fullstackofhay.logic.TemperatureLogic;

@Path("/temperature")
public class TemperatureResource {
    private static final Logger log =
        LoggerFactory.getLogger(TemperatureResource.class);
  private final Producer producer;
  private final TemperatureLogic temperatureLogic;

  @Inject
  public TemperatureResource(Producer producer,
                       TemperatureLogic temperatureLogic) {
    this.producer = producer;
    this.temperatureLogic = temperatureLogic;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTemps() {
    Temperatures temps = temperatureLogic.fetchAllTemperatures();
    return Response.ok(temps).build();
  }

  @GET
  @Path("spam-temps/{amount}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response spamTemps(@PathParam("amount") int amount) {
    log.info("spam temps");
    for(int i = 0; i < amount; i++){
      double tempCelsius = Math.random() * 30 + 1;
      producer.send(new org.apache.kafka.clients.producer.ProducerRecord<>("incoming-temperatures","","{\n" + //
      "\t\"sensordate\": \"2025-12-12 12:12:12\",\n" + //
      "\t\"temperature\": "+tempCelsius+",\n" + //
      "\t\"messageID\": "+i+"\n" + //
              "}"));
      log.info("spammed message", i);
    }
    return Response.ok("Spammed done: " + amount).build();
  }

  @GET
  @Path("temps-above/{above-temp}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response aboveTemp(@PathParam("above-temp") int aboveTemp) {
    Temperatures temps = temperatureLogic.fetchAllTemperatures();
    List<Temperature> filtered = temps.getTemperatures().stream().filter(temp -> temp.getTemperature() > aboveTemp).toList();
    return Response.ok(filtered).build();
  }


  @GET
  @Path("/add")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addTemp() {
    Temperature newtemp = new Temperature();
    newtemp.setSensordate(new Date(System.currentTimeMillis()));
    double tempCelsius = Math.random() * 30 + 1;
    newtemp.setTemperature((float) tempCelsius);
    Temperature temp = temperatureLogic.addTemperature(newtemp);
    return Response.ok(temp).build();
  }
}
