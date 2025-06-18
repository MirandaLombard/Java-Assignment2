/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.patelhardware.response;
import com.patelhardware.model.User;
/**
 *
 * @author miran
 */
public class LoginResponse {
    private boolean success;
    private String message;
    private User user; 

    /* Constructor for a login response.*/
    public LoginResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // --- Getters ---
    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public User getUser() {
        return user;
    }
    // --- Setters ---
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
