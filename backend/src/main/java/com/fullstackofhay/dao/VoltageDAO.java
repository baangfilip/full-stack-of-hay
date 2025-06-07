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

import com.fullstackofhay.entities.VoltageRecord;

import jakarta.inject.Inject;

public class VoltageDAO {
    private final DataSource ds;

    @Inject
    public VoltageDAO(DataSource ds) {
        this.ds = ds;
    }

    public List<VoltageRecord> getAllVoltages(){
        List<VoltageRecord> temperatures = new ArrayList<VoltageRecord>();
        String sql = "SELECT * FROM voltage";
        try (Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

        // Iterate over the results
        while (rs.next()) {
            VoltageRecord t = 
                new VoltageRecord(
                    rs.getInt("id"), 
                    rs.getFloat("voltage"), 
                    new Date(rs.getTimestamp("sensordate").getTime()), 
                    new Date(rs.getTimestamp("createdat").getTime()));
            temperatures.add(t);
        }

        } catch (SQLException e) {
            System.err.println("Error querying voltage: " + e.getMessage());
            e.printStackTrace();
        }
        return temperatures;
    }

    public int addVoltage(VoltageRecord volt) {
        String sql = "INSERT INTO voltage (voltage, sensordate) VALUES (?, ?)";
        try (Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);){
                ps.setFloat(1, volt.voltage());
                ps.setDate(2, volt.sensordate());
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

    public VoltageRecord getVoltage(int id) {
        String sql = "SELECT * FROM voltage WHERE id = ?";
        try (Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            VoltageRecord t = 
                new VoltageRecord(
                    rs.getInt("id"), 
                    rs.getFloat("voltage"), 
                    new Date(rs.getTimestamp("sensordate").getTime()), 
                    new Date(rs.getTimestamp("createdat").getTime()));
            return t;
        }

        } catch (SQLException e) {
            System.err.println("Error querying voltage: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
