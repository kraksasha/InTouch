package com.example.intouch.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Secrets")
@Data
public class Secret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "key_Original")
    private String key;
}
