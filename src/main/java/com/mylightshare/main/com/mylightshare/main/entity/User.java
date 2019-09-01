package com.mylightshare.main.com.mylightshare.main.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="username")
    @Size(min=4, max=12, message="Username length must be in between 4 and 12 characters.")
    private String username;

    @Column(name="email")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="enabled")
    private boolean isEnabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
