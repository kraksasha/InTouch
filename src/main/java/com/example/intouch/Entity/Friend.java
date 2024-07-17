package com.example.intouch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Friends")
@Data
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "friend_Id")
    private Long friendId;
    @Column(name = "user_Id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_Id", insertable = false, updatable = false)
    @JsonIgnore
    private User user;


}
