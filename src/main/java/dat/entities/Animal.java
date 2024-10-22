package dat.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String species;

    private int age;

    // Many-to-One relationship with User (owner)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key to the User table
    private User user;

}