package com.fullstackofhay.logic;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fullstackofhay.dao.TemperatureDAO;
import com.fullstackofhay.entities.Temperature;
import com.fullstackofhay.entities.TemperatureRecord;
import com.fullstackofhay.entities.Temperatures;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
@Service
@Singleton
public class TemperatureLogic {
  private final TemperatureDAO tempDAO;

  @Inject
  public TemperatureLogic(TemperatureDAO tempDAO) {
    this.tempDAO = tempDAO;
  }

  public Temperatures fetchAllTemperatures() {
    Temperatures result = new Temperatures();
    result.setTemperatures(tempDAO.getAllTemperetures());
    return result;
  }

  public Temperature addTemperature(TemperatureRecord temp) {
    int id = tempDAO.addTemp(temp);
    return tempDAO.getTemp(id);
  } 

  public Temperature addTemperature(Temperature temp) {
    int id = tempDAO.addTemp(temp);
    return tempDAO.getTemp(id);
  }
}
