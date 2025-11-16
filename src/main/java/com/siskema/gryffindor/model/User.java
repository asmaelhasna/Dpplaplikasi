package com.siskema.gryffindor.model;

public class User {
    private String username; // Ini adalah NIM/ID unik
    private String password;
    private String fullName;
    private UserRole role;
    private String organizationName; // Nama UKM/Himpunan jika rolenya Penyelenggara

    public User(String username, String password, String fullName, UserRole role, String organizationName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.organizationName = organizationName;
    }

    // --- Getters dan Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
}