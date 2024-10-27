package dat.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private String breed;
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Client owner;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
}

