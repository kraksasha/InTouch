package com.example.intouch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_Name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "date_Born")
    private String dateBorn;
    @Column(name = "age")
    private int age;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    private String phone;
    @Column(name = "about_Me")
    private String aboutMe;
    @Column(name = "town")
    private String town;
    @Column(name = "language")
    private String language;
    @Column(name = "low_School")
    private String lowSchool;
    @Column(name = "high_School")
    private String highSchool;
    @Column(name = "path_Avatar")
    private String pathAvatar;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    private Collection<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Photo> photos;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Friend> friends;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Music> musics;


}
