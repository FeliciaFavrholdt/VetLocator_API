package com.example.entities;

import dat.entities.Client;
import jakarta.persistence.*;
import lombok.Data;
import dat.enums.Animals;

@Data
@Entity
@Table(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Animals species;

    private String breed;
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Client owner;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
}
