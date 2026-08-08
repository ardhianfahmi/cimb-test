package com.ardhian.callmonitoring.auth.dto.response;

public class LoginResponse {

    private String token;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(
            String token,
            String username,
            String fullName,
            String email,
            String phoneNumber,
            String role
    ) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
