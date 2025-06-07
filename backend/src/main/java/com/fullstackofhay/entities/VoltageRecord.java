package com.fullstackofhay.entities;

import java.sql.Date;

public record VoltageRecord(int id, float voltage, Date sensordate, Date createdat) {}
