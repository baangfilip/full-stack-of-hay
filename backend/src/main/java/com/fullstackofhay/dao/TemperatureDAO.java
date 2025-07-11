package com.fullstackofhay.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.fullstackofhay.entities.TemperatureRecord;

import jakarta.inject.Inject;

public class TemperatureDAO {
    private final DataSource ds;

    @Inject
    public TemperatureDAO(DataSource ds) {
        this.ds = ds;
    }

    public List<TemperatureRecord> getAllTemperetures(){
        List<TemperatureRecord> temperatures = new ArrayList<TemperatureRecord>();
        String sql = "SELECT * FROM temperature";
        try (Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

        // Iterate over the results
        while (rs.next()) {
            TemperatureRecord t = 
                new TemperatureRecord(
                    rs.getInt("id"), 
                    rs.getFloat("temperature"), 
                    new Date(rs.getTimestamp("sensordate").getTime()), 
                    new Date(rs.getTimestamp("createdat").getTime()));
            temperatures.add(t);
        }

        } catch (SQLException e) {
            System.err.println("Error querying temperatures: " + e.getMessage());
            e.printStackTrace();
        }
        return temperatures;
    }

    public int addTemp(TemperatureRecord temp) {
        String sql = "INSERT INTO temperature (temperature, sensordate) VALUES (?, ?)";
        try (Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);){
                ps.setFloat(1, temp.temperature());
                ps.setDate(2, temp.sensordate());
                int affected = ps.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Insert failed, no rows affected.");
                }
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }else {
                        throw new SQLException("Insert succeeded but no ID obtained.");
                    }
                }
        } catch (SQLException e) {
            System.err.println("Error inserting temperature: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public TemperatureRecord getTemp(int id) {
        String sql = "SELECT * FROM temperature WHERE id = ?";
        try (Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            TemperatureRecord t = 
                new TemperatureRecord(
                    rs.getInt("id"), 
                    rs.getFloat("temperature"), 
                    new Date(rs.getTimestamp("sensordate").getTime()), 
                    new Date(rs.getTimestamp("createdat").getTime()));
            return t;
        }

        } catch (SQLException e) {
            System.err.println("Error querying temperatures: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
