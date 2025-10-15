package BankingApp;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String passwordHash;
    private String salt;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private boolean isActive;
    
    public User(String username, String passwordHash, String salt, 
                String firstName, String lastName, String email) {
        this.username = sanitizeInput(username);
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.firstName = sanitizeInput(firstName);
        this.lastName = sanitizeInput(lastName);
        this.email = sanitizeInput(email);
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>\"']", "");
    }
    
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getSalt() { return salt; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return isActive; }
    
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return String.format("User{username='%s', firstName='%s', lastName='%s', email='%s'}", 
                           username, firstName, lastName, email);
    }
}