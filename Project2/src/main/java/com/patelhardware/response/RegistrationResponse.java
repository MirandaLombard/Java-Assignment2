/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.response;

/**
 *
 * @author miran
 */
public class RegistrationResponse {
    private boolean success;
    private String message;

    /*Constructor for a registration response.*/
    public RegistrationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // --- Getters ---
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // --- Setters ---
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
