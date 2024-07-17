package com.example.intouch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Comments_Photo")
@Data
public class CommentPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "text")
    private String text;
    @Column(name = "id_User_Author")
    private Long userAuthorId;
    @Column(name = "photo_Id")
    private Long photoId;

    @ManyToOne
    @JoinColumn(name = "photo_Id", insertable = false, updatable = false)
    @JsonIgnore
    private Photo photo;
}
