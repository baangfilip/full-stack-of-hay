package com.fullstackofhay.entities;

import java.util.ArrayList;
import java.util.List;

public class Temperatures {
    private List<Temperature> temperatures = new ArrayList<Temperature>();

    public List<Temperature> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<Temperature> temperatures) {
        this.temperatures = temperatures;
    }
    public void add(Temperature temp){
        this.temperatures.add(temp);
    }
}
