package com.mylightshare.main.com.mylightshare.main.formview;

import javax.validation.constraints.Size;

public class AccountUpdateForm {

    private String email;

    @Size(min=4, max=12, message="Username length must be in between 4 and 12 characters.")
    private String username;

    private String currentPassword;

    private String newPassword;

    public AccountUpdateForm() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
