package com.fullstackofhay.entities;

import java.sql.Date;

public record TemperatureRecord(int id, float temperature, Date sensordate, Date createdat) {}
