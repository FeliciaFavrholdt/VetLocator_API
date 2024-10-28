package dat.config;

import dat.entities.*;
import dat.enums.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Populate {

    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        City copenhagen, aarhus, odense, aalborg, esbjerg;
        Clinic c1, c2, c3, c4, c5;
        Veterinarian v1, v2, v3, v4, v5, v6;
        Client cl1, cl2, cl3;
        Animal a1, a2, a3, a4, a5;
        Appointment app1, app2, app3;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Populate Cities
            copenhagen = new City(null, "Copenhagen", "1000");
            aarhus = new City(null, "Aarhus", "8000");
            odense = new City(null, "Odense", "5000");
            aalborg = new City(null, "Aalborg", "9000");
            esbjerg = new City(null, "Esbjerg", "6700");

            // Persist all cities
            em.persist(copenhagen);
            em.persist(aarhus);
            em.persist(odense);
            em.persist(aalborg);
            em.persist(esbjerg);

            // Populate Clinics
            c1 = new Clinic(null, "Copenhagen Clinic", "Kongevejen 27", "+45 30 30 30 30", copenhagen, true, null);
            c2 = new Clinic(null, "Aarhus Clinic", "Viborgvej 27", "+45 40 40 40 40", aarhus, true, null);
            c3 = new Clinic(null, "Odense Clinic", "Hjallesevej 27", "+45 50 50 50 50", odense, true, null);
            c4 = new Clinic(null, "Aalborg Clinic", "Hobrovej 27", "+45 60 60 60 60", aalborg, true, null);
            c5 = new Clinic(null, "Esbjerg Clinic", "Søndre Ringvej 27", "+45 70 70 70 70", esbjerg, true, null);

            // Persisting Clinics
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.persist(c4);
            em.persist(c5);

            // Populate Veterinarians connected to the clinics
            v1 = new Veterinarian(null, "Henrik Larsen", Specialization.GENERAL_PRACTICE, c1, Availability.AVAILABLE);
            v2 = new Veterinarian(null, "Anna Jensen", Specialization.SURGERY, c1, Availability.BOOKED);
            v3 = new Veterinarian(null, "Peter Hansen", Specialization.DENTISTRY, c2, Availability.AVAILABLE);
            v4 = new Veterinarian(null, "Maria Nielsen", Specialization.DERMATOLOGY, c3, Availability.UNAVAILABLE);
            v5 = new Veterinarian(null, "Søren Kristensen", Specialization.INTERNAL_MEDICINE, c4, Availability.BOOKED);
            v6 = new Veterinarian(null, "Lotte Petersen", Specialization.GENERAL_PRACTICE, c5, Availability.ON_VACATION);

            // Persisting Veterinarians
            em.persist(v1);
            em.persist(v2);
            em.persist(v3);
            em.persist(v4);
            em.persist(v5);
            em.persist(v6);

            // Populate Clients with gender and city references
            cl1 = new Client(null, "Mads", Gender.MALE, "mads@gmail.com", "+45 30 30 30 30", "Kongevejen 27", copenhagen, null);
            cl2 = new Client(null, "Lars", Gender.MALE, "lars@gmail.com", "+45 40 40 40 40", "Viborgvej 27", aarhus, null);
            cl3 = new Client(null, "Sofie", Gender.FEMALE, "sofie@gmail.com", "+45 50 50 50 50", "Hjallesevej 27", odense, null);

            // Persist Clients first (without animals)
            em.persist(cl1);
            em.persist(cl2);
            em.persist(cl3);

            // Populate Animals (set owners after creating clients)
            a1 = new Animal(null, "Coco", Animals.DOG, "Labrador", 5, cl1, MedicalHistory.DEWORMED);
            a2 = new Animal(null, "Cleo", Animals.CAT, "Siamese", 3, cl1, MedicalHistory.DIETARY_RESTRICTIONS);
            a3 = new Animal(null, "Buster", Animals.DOG, "Golden Retriever", 2, cl2, MedicalHistory.FLEA_TREATED);
            a4 = new Animal(null, "Molly", Animals.RABBIT, "Normal", 4, cl3, MedicalHistory.VACCINATED);
            a5 = new Animal(null, "Sofus", Animals.FISH, "Normal", 1, cl3, MedicalHistory.DEWORMED);

            // Persist Animals
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.persist(a4);
            em.persist(a5);

            // Now set the animals for clients
            cl1.setAnimals(List.of(a1, a2));
            cl2.setAnimals(List.of(a3));
            cl3.setAnimals(List.of(a4, a5));

            // Populate Appointments
            app1 = new Appointment(
                    null,
                    LocalDateTime.of(2024, 10, 10, 10, 0),  // Example of LocalDateTime
                    "Checkup for Coco",
                    AppointmentStatus.SCHEDULED,
                    c1,  // Clinic
                    cl1,  // Client
                    a1,   // Animal (Coco)
                    v1    // Veterinarian (add this argument)
            );

            app2 = new Appointment(
                    null,
                    LocalDateTime.of(2024, 1, 15, 11, 0),  // Specific date and time
                    "Vaccination for Cleo",
                    AppointmentStatus.SCHEDULED,
                    c1,
                    cl1,
                    a2,
                    v2
            );

            app3 = new Appointment(
                    null,
                    LocalDateTime.of(2024, 1, 16, 9, 0),
                    "Dental check for Buster",
                    AppointmentStatus.SCHEDULED,
                    c2,
                    cl2,
                    a3,
                    v3
            );

            // Persisting Appointments
            em.persist(app1);
            em.persist(app2);
            em.persist(app3);

            em.getTransaction().commit();
        }
    }
}
