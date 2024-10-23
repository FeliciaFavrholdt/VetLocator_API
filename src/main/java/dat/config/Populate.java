package dat.config;

import dat.entities.*;
import dat.enums.AppointmentStatus;
import dat.enums.Gender;
import dat.enums.Specialization;
import dat.enums.Weekday;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Populate Cities
            City copenhagen = new City(null, "Copenhagen", "Main Street 123", "1000");
            City aarhus = new City(null, "Aarhus", "Central Street 456", "8000");
            em.persist(copenhagen);
            em.persist(aarhus);

            // Populate Clients (Users)
            Client johnDoe = new Client("johndoe", "password123", "John", "Doe", Gender.MALE, "johndoe@example.com", "+45 12 34 56 78");
            Client janeDoe = new Client("janedoe", "password123", "Jane", "Doe", Gender.FEMALE, "janedoe@example.com", "+45 98 76 54 32");
            em.persist(johnDoe);
            em.persist(janeDoe);

            // Populate Animals for Clients
            Set<Animal> johnsAnimals = getJohnsAnimals(johnDoe);
            Set<Animal> janesAnimals = getJanesAnimals(janeDoe);
            johnDoe.setAnimals(johnsAnimals);
            janeDoe.setAnimals(janesAnimals);
            johnsAnimals.forEach(em::persist);
            janesAnimals.forEach(em::persist);

            // Populate Clinics
            Clinic happyPaws = new Clinic("Happy Paws", Specialization.BIRD, "+45 22 33 44 55", "contact@happypaws.dk", "Clinic Street 12", copenhagen);
            Clinic petCare = new Clinic("Pet Care", Specialization.CAT, "+45 44 33 22 11", "contact@petcare.dk", "Care Street 8", aarhus);
            em.persist(happyPaws);
            em.persist(petCare);

            // Populate Veterinarians
            Veterinarian vet1 = new Veterinarian(null, "Dr. Smith", "+45 55 55 55 55", Veterinarian.Availability.AVAILABLE, happyPaws);
            Veterinarian vet2 = new Veterinarian(null, "Dr. Johnson", "+45 66 66 66 66", Veterinarian.Availability.AVAILABLE, petCare);
            em.persist(vet1);
            em.persist(vet2);

            // Populate Appointments
            Appointment johnsAppointment = new Appointment(null, LocalDate.now(), LocalTime.of(10, 0), "Routine check-up", AppointmentStatus.SCHEDULED, happyPaws, johnDoe, johnsAnimals.iterator().next());
            Appointment janesAppointment = new Appointment(null, LocalDate.now().plusDays(1), LocalTime.of(11, 0), "Vaccination", AppointmentStatus.SCHEDULED, petCare, janeDoe, janesAnimals.iterator().next());
            em.persist(johnsAppointment);
            em.persist(janesAppointment);

            // Populate Opening Hours for Clinics
            OpeningHours happyPawsOpening = new OpeningHours(null, Weekday.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), happyPaws);
            OpeningHours petCareOpening = new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), petCare);
            em.persist(happyPawsOpening);
            em.persist(petCareOpening);

            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Animal> getJohnsAnimals(Client client) {
        Animal buddy = new Animal(null, "Buddy", "Dog", 5, client);
        Animal whiskers = new Animal(null, "Whiskers", "Cat", 3, client);
        return Set.of(buddy, whiskers);
    }

    @NotNull
    private static Set<Animal> getJanesAnimals(Client client) {
        Animal rex = new Animal(null, "Rex", "Dog", 4, client);
        Animal fluffy = new Animal(null, "Fluffy", "Cat", 2, client);
        return Set.of(rex, fluffy);
    }
}
