package com.fullstackofhay.logic;

import java.util.List;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fullstackofhay.dao.VoltageDAO;
import com.fullstackofhay.entities.TemperatureRecord;
import com.fullstackofhay.entities.Temperatures;
import com.fullstackofhay.entities.VoltageRecord;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
@Service
@Singleton
public class VoltageLogic {
  private final VoltageDAO voltDAO;
    private static final Logger log =
        LoggerFactory.getLogger(TemperatureLogic.class);

  @Inject
  public VoltageLogic(VoltageDAO voltDAO) {
    this.voltDAO = voltDAO;
  }

  public List<VoltageRecord> fetchAllVoltages() {
    return voltDAO.getAllVoltages();
  }

  public VoltageRecord addVoltage(VoltageRecord temp) {
    int id = voltDAO.addVoltage(temp);
    return voltDAO.getVoltage(id);
  }
  
  public List<VoltageRecord> getVoltagesAbove(int aboveVoltage){
    List<VoltageRecord> voltages = fetchAllVoltages().stream()
      .filter(temp -> temp.voltage() > aboveVoltage).toList();
    return voltages;
  }
}
