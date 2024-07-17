package com.production.intouchcompany.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name_Photo")
    private String namePhoto;
    @Column(name = "path_To_Photo")
    private String pathToPhoto;
    @Column(name = "user_Id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_Id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "photo")
    private List<CommentPhoto> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "photo")
    private List<LikePhoto> likes;
}
