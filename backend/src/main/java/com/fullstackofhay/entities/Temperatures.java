package com.fullstackofhay.entities;

import java.util.ArrayList;
import java.util.List;

public class Temperatures {
    private List<TemperatureRecord> temperatures = new ArrayList<TemperatureRecord>();

    public List<TemperatureRecord> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<TemperatureRecord> temperatures) {
        this.temperatures = temperatures;
    }
    public void add(TemperatureRecord temp){
        this.temperatures.add(temp);
    }
}
