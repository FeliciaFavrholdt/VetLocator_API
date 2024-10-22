package dat.config;

import dat.entities.Animal;
import dat.entities.Clinic;
import dat.entities.City;
import dat.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        // Create animals for John and Jane
        Set<Animal> johnsAnimals = getJohnsAnimals();
        Set<Animal> janesAnimals = getJanesAnimals();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create City entities
            City copenhagen = new City(null, "Copenhagen", "Main Street 123", "1000");
            City aarhus = new City(null, "Aarhus", "Central Street 456", "8000");

            // Persist cities
            em.persist(copenhagen);
            em.persist(aarhus);

            // Create Users
            User johnDoe = new User("johndoe", "password123", "John", "Doe", "johndoe@example.com", "+45 12 34 56 78");
            User janeDoe = new User("janedoe", "password123", "Jane", "Doe", "janedoe@example.com", "+45 98 76 54 32");

            // Assign animals to users
            johnDoe.setAnimals(johnsAnimals);
            janesAnimals.forEach(animal -> animal.setUser(janeDoe));  // Assign animals to Jane Doe
            johnsAnimals.forEach(animal -> animal.setUser(johnDoe));  // Assign animals to John Doe
            janeDoe.setAnimals(janesAnimals);

            // Persist users
            em.persist(johnDoe);
            em.persist(janeDoe);

            // Create Clinics with associated cities
            Clinic happyPaws = new Clinic("Happy Paws", "+45 22 33 44 55", "contact@happypaws.dk", "Clinic Street 12", copenhagen);
            Clinic petCare = new Clinic("Pet Care", "+45 44 33 22 11", "contact@petcare.dk", "Care Street 8", aarhus);

            // Persist clinics
            em.persist(happyPaws);
            em.persist(petCare);

            // Commit transaction
            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Animal> getJohnsAnimals() {
        Animal buddy = new Animal(null, "Buddy", "Dog", 5, null);  // ID and User are set later
        Animal whiskers = new Animal(null, "Whiskers", "Cat", 3, null);  // ID and User are set later
        return Set.of(buddy, whiskers);
    }

    @NotNull
    private static Set<Animal> getJanesAnimals() {
        Animal rex = new Animal(null, "Rex", "Dog", 4, null);  // ID and User are set later
        Animal fluffy = new Animal(null, "Fluffy", "Cat", 2, null);  // ID and User are set later
        return Set.of(rex, fluffy);
    }
}
