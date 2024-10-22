package dat.config;

import dat.entities.Animal;
import dat.entities.Clinic;
import dat.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Set<Animal> animals = getAnimals();
        Set<User> users = getUsers();
        Set<Clinic> clinics = getClinics();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            animals.forEach(em::persist);
            users.forEach(em::persist);
            clinics.forEach(em::persist);
            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Animal> getAnimals() {
        Animal dog = new Animal();
        Animal cat = new Animal();
        Animal parrot = new Animal();
        Animal horse = new Animal();

        Animal[] animalArray = {dog, cat, parrot, horse};
        return Set.of(animalArray);
    }

    @NotNull
    private static Set<User> getUsers() {
        User john = new User();
        User jane = new User();
        User mike = new User();
        User alice = new User();

        User[] userArray = {john, jane, mike, alice};
        return Set.of(userArray);
    }

    @NotNull
    private static Set<Clinic> getClinics() {
        Clinic vetcare = new Clinic();
        Clinic healthypets = new Clinic();
        Clinic petwellness = new Clinic();
        Clinic pawclinic = new Clinic();

        Clinic[] clinicArray = {vetcare, healthypets, petwellness, pawclinic};
        return Set.of(clinicArray);
    }
}