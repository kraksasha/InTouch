package com.example.intouch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Likes_Music")
@Data
public class LikeMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "author_Id")
    private Long authorId;
    @Column(name = "music_Id")
    private Long musicId;

    @ManyToOne
    @JoinColumn(name = "music_Id", insertable = false, updatable = false)
    @JsonIgnore
    private Music music;
}
