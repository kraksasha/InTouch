package com.example.intouch.DTO;

import lombok.Data;

@Data
public class PasswordChange {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String encodedNewPassword;
}
