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
            City copenhagen = new City(null, "Copenhagen", 1000);
            City aarhus = new City(null, "Aarhus", 8000);
            City odense = new City(null, "Odense", 5000);
            City aalborg = new City(null, "Aalborg", 9000);
            City esbjerg = new City(null, "Esbjerg", 6700);
            City randers = new City(null, "Randers", 8900);
            City kolding = new City(null, "Kolding", 6000);
            City horsens = new City(null, "Horsens", 8700);
            City vejle = new City(null, "Vejle", 7100);
            City roskilde = new City(null, "Roskilde", 4000);

            em.persist(copenhagen);
            em.persist(aarhus);
            em.persist(odense);
            em.persist(aalborg);
            em.persist(esbjerg);
            em.persist(randers);
            em.persist(kolding);
            em.persist(horsens);
            em.persist(vejle);
            em.persist(roskilde);


            // Populate Clinics
// Populate Clinics
            Clinic clinicCopenhagen = new Clinic("Byens bedste dyreklinik", Specialization.CAT, "+45 11 22 33 44", "byensbedstedyreklinik@mail.dk", "Kronprinsessegade 76", copenhagen.getPostalCode());
            Clinic clinicAarhus = new Clinic("Aarhus Hunde klinikken", Specialization.DOG, "+45 22 33 44 55", "aarhus@hundeklinikken.dk", "Haslevej 10", aarhus.getPostalCode());
            Clinic clinicOdense = new Clinic("Odense vingehjælp", Specialization.BIRD, "+45 33 44 55 66", "odense@vingehjælp.dk", "Andersgade 99", odense.getPostalCode());
            Clinic clinicAalborg = new Clinic("Aalborg Reptil klinik", Specialization.REPTILE, "+45 44 55 66 77", "aalborg@reptil.dk", "Vesterbro 332", aalborg.getPostalCode());
            Clinic clinicEsbjerg = new Clinic("Esbjerg Hesteklinik", Specialization.HORSE, "+45 55 66 77 88", "esbjerg@hesteklinik.dk", "Hellerupgade 24", esbjerg.getPostalCode());
            Clinic clinicRanders = new Clinic("Randers Kattehospital", Specialization.CAT, "+45 66 77 88 99", "randers@kattehospital.dk", "Bolværksvej 393", randers.getPostalCode());
            Clinic clinicKolding = new Clinic("Hundenes hjem", Specialization.DOG, "+45 77 88 99 00", "kolding@undeneshjem.dk", "Åkandevej 2", kolding.getPostalCode());
            Clinic clinicHorsens = new Clinic("Horsens Dyreklinik", Specialization.RABBIT, "+45 88 99 00 11", "horsens@dyreklinik.dk", "Myntegade 13", horsens.getPostalCode());
            Clinic clinicVejle = new Clinic("Vejle Byensdyr", Specialization.BIRD, "+45 99 00 11 22", "vejle@byensdyr.dk", "Gyngegyden 73", vejle.getPostalCode());
            Clinic clinicRoskilde = new Clinic("Roskilde Dyrehjælp", Specialization.DOG, "+45 00 11 22 33", "roskilde@dyrehjælp.dk", "Boulevard 2", roskilde.getPostalCode());

            em.persist(clinicCopenhagen);
            em.persist(clinicAarhus);
            em.persist(clinicOdense);
            em.persist(clinicAalborg);
            em.persist(clinicEsbjerg);
            em.persist(clinicRanders);
            em.persist(clinicKolding);
            em.persist(clinicHorsens);
            em.persist(clinicVejle);
            em.persist(clinicRoskilde);


// Populate Veterinarians connected to the clinics
            Veterinarian vetCopenhagen1 = new Veterinarian(null, "Henrik Larsen", "+45 12 34 56 78", Veterinarian.Availability.AVAILABLE, clinicCopenhagen);
            Veterinarian vetCopenhagen2 = new Veterinarian(null, "Anna Jensen", "+45 87 65 43 21", Veterinarian.Availability.BOOKED, clinicCopenhagen);
            Veterinarian vetAarhus = new Veterinarian(null, "Peter Hansen", "+45 22 44 66 88", Veterinarian.Availability.AVAILABLE, clinicAarhus);
            Veterinarian vetOdense = new Veterinarian(null, "Maria Nielsen", "+45 33 55 77 99", Veterinarian.Availability.BOOKED, clinicOdense);
            Veterinarian vetAalborg = new Veterinarian(null, "Søren Kristensen", "+45 44 66 88 00", Veterinarian.Availability.AVAILABLE, clinicAalborg);
            Veterinarian vetEsbjerg = new Veterinarian(null, "Lotte Petersen", "+45 55 77 99 11", Veterinarian.Availability.BOOKED, clinicEsbjerg);
            Veterinarian vetRanders = new Veterinarian(null, "Mikkel Thomsen", "+45 66 88 00 22", Veterinarian.Availability.AVAILABLE, clinicRanders);
            Veterinarian vetKolding = new Veterinarian(null, "Karina Sørensen", "+45 77 99 11 33", Veterinarian.Availability.BOOKED, clinicKolding);
            Veterinarian vetHorsens = new Veterinarian(null, "Lars Pedersen", "+45 88 00 22 44", Veterinarian.Availability.AVAILABLE, clinicHorsens);
            Veterinarian vetVejle = new Veterinarian(null, "Birgitte Andersen", "+45 99 11 33 55", Veterinarian.Availability.BOOKED, clinicVejle);
            Veterinarian vetRoskilde = new Veterinarian(null, "Thomas Olesen", "+45 00 22 44 66", Veterinarian.Availability.AVAILABLE, clinicRoskilde);

// Persisting veterinarians
            em.persist(vetCopenhagen1);
            em.persist(vetCopenhagen2);
            em.persist(vetAarhus);
            em.persist(vetOdense);
            em.persist(vetAalborg);
            em.persist(vetEsbjerg);
            em.persist(vetRanders);
            em.persist(vetKolding);
            em.persist(vetHorsens);
            em.persist(vetVejle);
            em.persist(vetRoskilde);

            // Populate Opening Hours for Clinics
            clinicCopenhagen.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicCopenhagen));
            clinicCopenhagen.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicCopenhagen));
            clinicCopenhagen.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicCopenhagen));
            clinicCopenhagen.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicCopenhagen));
            clinicCopenhagen.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicCopenhagen));

            clinicAarhus.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAarhus));
            clinicAarhus.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAarhus));
            clinicAarhus.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAarhus));
            clinicAarhus.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAarhus));
            clinicAarhus.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAarhus));
            // Clinic Odense opening hours
            clinicOdense.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicOdense));
            clinicOdense.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicOdense));
            clinicOdense.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicOdense));
            clinicOdense.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicOdense));
            clinicOdense.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicOdense));

// Clinic Aalborg opening hours
            clinicAalborg.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAalborg));
            clinicAalborg.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAalborg));
            clinicAalborg.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAalborg));
            clinicAalborg.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAalborg));
            clinicAalborg.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicAalborg));

// Clinic Esbjerg opening hours
            clinicEsbjerg.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicEsbjerg));
            clinicEsbjerg.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicEsbjerg));
            clinicEsbjerg.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicEsbjerg));
            clinicEsbjerg.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicEsbjerg));
            clinicEsbjerg.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicEsbjerg));

// Clinic Randers opening hours
            clinicRanders.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRanders));
            clinicRanders.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRanders));
            clinicRanders.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRanders));
            clinicRanders.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRanders));
            clinicRanders.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRanders));

// Clinic Kolding opening hours
            clinicKolding.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicKolding));
            clinicKolding.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicKolding));
            clinicKolding.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicKolding));
            clinicKolding.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicKolding));
            clinicKolding.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicKolding));

// Clinic Horsens opening hours
            clinicHorsens.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicHorsens));
            clinicHorsens.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicHorsens));
            clinicHorsens.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicHorsens));
            clinicHorsens.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicHorsens));
            clinicHorsens.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicHorsens));

// Clinic Vejle opening hours
            clinicVejle.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicVejle));
            clinicVejle.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicVejle));
            clinicVejle.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicVejle));
            clinicVejle.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicVejle));
            clinicVejle.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), clinicVejle));

// Clinic Roskilde opening hours
            clinicRoskilde.addOpeningHour(new OpeningHours(null, Weekday.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRoskilde));
            clinicRoskilde.addOpeningHour(new OpeningHours(null, Weekday.TUESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRoskilde));
            clinicRoskilde.addOpeningHour(new OpeningHours(null, Weekday.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRoskilde));
            clinicRoskilde.addOpeningHour(new OpeningHours(null, Weekday.THURSDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRoskilde));
            clinicRoskilde.addOpeningHour(new OpeningHours(null, Weekday.FRIDAY, LocalTime.of(8, 0), LocalTime.of(16, 0), clinicRoskilde));

            // Fetching clinics (example: by ID or any appropriate way)
            // Populate Clients (Users)
            Client client1 = new Client("john_doe", "password123", "John", "Doe", Gender.MALE, "john.doe@example.com", "+45 12 34 56 78");
            client1.addAnimal(new Animal(null, "Buddy", "Dog", 3, client1));
            client1.addAnimal(new Animal(null, "Whiskers", "Cat", 2, client1));

            Client client2 = new Client("jane_doe", "password123", "Jane", "Doe", Gender.FEMALE, "jane.doe@example.com", "+45 87 65 43 21");
            client2.addAnimal(new Animal(null, "Fluffy", "Rabbit", 1, client2));

            Client client3 = new Client("michael_scott", "password123", "Michael", "Scott", Gender.MALE, "michael.scott@example.com", "+45 22 33 44 55");
            client3.addAnimal(new Animal(null, "Goldie", "Fish", 1, client3));

            Client client4 = new Client("pam_beesly", "password123", "Pam", "Beesly", Gender.FEMALE, "pam.beesly@example.com", "+45 33 44 55 66");
            client4.addAnimal(new Animal(null, "Max", "Dog", 4, client4));
            client4.addAnimal(new Animal(null, "Cleo", "Cat", 3, client4));

            Client client5 = new Client("jim_halpert", "password123", "Jim", "Halpert", Gender.MALE, "jim.halpert@example.com", "+45 44 55 66 77");
            client5.addAnimal(new Animal(null, "Spike", "Turtle", 5, client5));

            Client client6 = new Client("dwight_schrute", "password123", "Dwight", "Schrute", Gender.MALE, "dwight.schrute@example.com", "+45 55 66 77 88");
            client6.addAnimal(new Animal(null, "Beet", "Horse", 6, client6));

            Client client7 = new Client("kevin_malone", "password123", "Kevin", "Malone", Gender.MALE, "kevin.malone@example.com", "+45 66 77 88 99");
            client7.addAnimal(new Animal(null, "Snowball", "Hamster", 1, client7));

            Client client8 = new Client("angela_martin", "password123", "Angela", "Martin", Gender.FEMALE, "angela.martin@example.com", "+45 77 88 99 00");
            client8.addAnimal(new Animal(null, "Sprinkles", "Cat", 3, client8));

            Client client9 = new Client("stanley_hudson", "password123", "Stanley", "Hudson", Gender.MALE, "stanley.hudson@example.com", "+45 88 99 00 11");
            client9.addAnimal(new Animal(null, "Puffy", "Dog", 7, client9));

            Client client10 = new Client("kelly_kapoor", "password123", "Kelly", "Kapoor", Gender.FEMALE, "kelly.kapoor@example.com", "+45 99 00 11 22");
            client10.addAnimal(new Animal(null, "Bella", "Dog", 2, client10));

            // Persisting clients and their animals
            em.persist(client1);
            em.persist(client2);
            em.persist(client3);
            em.persist(client4);
            em.persist(client5);
            em.persist(client6);
            em.persist(client7);
            em.persist(client8);
            em.persist(client9);
            em.persist(client10);

            em.find(Clinic.class, 1);  // Assuming clinic with ID 1
            em.find(Clinic.class, 2);
            em.find(Clinic.class, 3);
            em.find(Clinic.class, 4);
            em.find(Clinic.class, 5);

            // Fetching clients (example: by ID or any appropriate way)
            em.find(Client.class, 1);
            em.find(Client.class, 2);
            em.find(Client.class, 3);
            em.find(Client.class, 4);
            em.find(Client.class, 5);

            // Fetching animals
            Animal buddy = em.find(Animal.class, 1);
            Animal whiskers = em.find(Animal.class, 2);
            Animal fluffy = em.find(Animal.class, 3);
            Animal goldie = em.find(Animal.class, 4);
            Animal max = em.find(Animal.class, 5);

            // Appointments for client1 (John Doe) and his animals
            Appointment appointment1 = new Appointment(
                    null,
                    LocalDate.now().plusDays(2),  // Schedule for 2 days later
                    LocalTime.of(10, 0),  // Appointment at 10:00 AM
                    "Checkup for Buddy",
                    AppointmentStatus.SCHEDULED,
                    clinicCopenhagen,
                    client1,
                    buddy
            );

            Appointment appointment2 = new Appointment(
                    null,
                    LocalDate.now().plusDays(5),
                    LocalTime.of(14, 0),
                    "Vaccination for Whiskers",
                    AppointmentStatus.SCHEDULED,
                    clinicCopenhagen,
                    client1,
                    whiskers
            );

            // Appointments for client2 (Jane Doe) and her animal Fluffy
            Appointment appointment3 = new Appointment(
                    null,
                    LocalDate.now().plusDays(3),
                    LocalTime.of(11, 30),
                    "Routine checkup for Fluffy",
                    AppointmentStatus.SCHEDULED,
                    clinicAarhus,
                    client2,
                    fluffy
            );

            // Appointments for client3 (Michael Scott) and his animal Goldie
            Appointment appointment4 = new Appointment(
                    null,
                    LocalDate.now().plusDays(4),
                    LocalTime.of(9, 0),
                    "Swim checkup for Goldie",
                    AppointmentStatus.SCHEDULED,
                    clinicOdense,
                    client3,
                    goldie
            );

            // Appointments for client4 (Pam Beesly) and her animal Max
            Appointment appointment5 = new Appointment(
                    null,
                    LocalDate.now().plusDays(1),
                    LocalTime.of(12, 0),
                    "Dental checkup for Max",
                    AppointmentStatus.SCHEDULED,
                    clinicAalborg,
                    client4,
                    max
            );

            // Appointments for client5 (Jim Halpert) and his animal Spike
            Appointment appointment6 = new Appointment(
                    null,
                    LocalDate.now().plusDays(7),
                    LocalTime.of(15, 0),
                    "General health checkup for Spike",
                    AppointmentStatus.SCHEDULED,
                    clinicEsbjerg,
                    client5,
                    max
            );

            // Persisting appointments
            em.persist(appointment1);
            em.persist(appointment2);
            em.persist(appointment3);
            em.persist(appointment4);
            em.persist(appointment5);
            em.persist(appointment6);

            em.getTransaction().commit();
        }
    }
}
