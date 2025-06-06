package com.fullstackofhay.entities;

import java.sql.Date;

public class Temperature {
    private int id;
    private float temperature;
    private Date sensordate;
    private Date createdat;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public float getTemperature() {
        return temperature;
    }
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    public Date getSensordate() {
        return sensordate;
    }
    public void setSensordate(Date sensordate) {
        this.sensordate = sensordate;
    }
    public Date getCreatedat() {
        return createdat;
    }
    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }
    
}
