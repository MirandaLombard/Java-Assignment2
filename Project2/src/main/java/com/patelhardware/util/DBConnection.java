/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author miran
 */
public class DBConnection {
    // Database connection parameters
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
    private static final String DB_URL = "jdbc:mysql://localhost:3306/patel_hardware_db";
    private static final String USER = "root"; 
    private static final String PASS = "";    

    // Logger for logging connection issues
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Register JDBC driver 
            Class.forName(JDBC_DRIVER);

            // Open a connection
            LOGGER.info("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            LOGGER.info("Database connection established.");
        } catch (SQLException se) {
            // Handle errors for JDBC
            LOGGER.log(Level.SEVERE, "SQL Exception during database connection: " + se.getMessage(), se);
        } catch (ClassNotFoundException e) {
            // Handle errors for Class.forName
            LOGGER.log(Level.SEVERE, "JDBC Driver not found: " + e.getMessage(), e);
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                LOGGER.info("Database connection closed.");
            } catch (SQLException se) {
                LOGGER.log(Level.SEVERE, "SQL Exception during database connection close: " + se.getMessage(), se);
            }
        }
    }
}
