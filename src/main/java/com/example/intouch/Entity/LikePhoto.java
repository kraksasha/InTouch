package com.example.intouch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Likes_Photo")
@Data
public class LikePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "author_Id")
    private Long authorId;
    @Column(name = "photo_Id")
    private Long photoId;

    @ManyToOne
    @JoinColumn(name = "photo_Id", insertable = false, updatable = false)
    @JsonIgnore
    private Photo photo;


}
