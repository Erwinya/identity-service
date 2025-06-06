package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id", length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "name", length = 25, nullable = false)
    private String name;

    @Column(name = "surname", length = 25, nullable = false)
    private String surname;
}
