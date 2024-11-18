package com.example.intouch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Musics")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name_Music")
    private String nameMusic;
    @Column(name = "path_To_Music")
    private String pathToMusic;

    @ManyToMany
    @JoinTable(name = "users_musics",
            joinColumns = @JoinColumn(name = "music_Id"),
            inverseJoinColumns = @JoinColumn(name = "user_Id"))
    @JsonIgnore

    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "music")
    private List<LikeMusic> likes;
}
