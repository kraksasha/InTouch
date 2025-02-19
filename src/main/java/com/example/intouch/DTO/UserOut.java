package com.example.intouch.DTO;

import com.example.intouch.Entity.Friend;
import com.example.intouch.Entity.Music;
import com.example.intouch.Entity.Photo;
import lombok.Data;

import java.util.List;

@Data
public class UserOut {

    private Long id;
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
    private String pathAvatar;
    private List<Photo> photos;
    private List<Music> musics;
    private List<Friend> friends;
}
