package com.example.intouch.DTO;

import lombok.Data;

@Data
public class UserEdit {
    private String firstName;
    private String lastName;
    private String dateBorn;
    private int age;
    private String email;
    private String phone;
    private String aboutMe;
    private String town;
    private String language;
    private String lowSchool;
    private String highSchool;
}
