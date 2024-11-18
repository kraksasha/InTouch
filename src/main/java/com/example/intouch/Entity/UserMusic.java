package com.example.intouch.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users_musics")
@Data
public class UserMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_Id")
    private Long userId;
    @Column(name = "music_Id")
    private Long musicId;

}
