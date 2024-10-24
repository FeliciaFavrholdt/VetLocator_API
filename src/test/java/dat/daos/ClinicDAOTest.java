package dat.daos;

import dat.config.HibernateConfig;
import dat.dao.impl.ClinicDAO;
import dat.dto.ClinicDTO;
import dat.entities.City;
import dat.entities.Clinic;
import dat.enums.Specialization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    public class ClinicDAOTest {

        private EntityManagerFactory emf;
        private EntityManager em;
        private ClinicDAO clinicDao;
        private int testClinicId;
        private long testCityId;

        @BeforeEach
        public void setUp() {
            // Initialize the EntityManagerFactory for test purposes
            emf = HibernateConfig.getEntityManagerFactoryForTest();
            em = emf.createEntityManager();
            clinicDao = ClinicDAO.getInstance(emf);

            // Begin transaction
            em.getTransaction().begin();

            // Create and persist a City entity
            City city = new City();
            city.setCityName("Test City");
            city.setPostalCode(12345);
            em.persist(city);  // Persist the city to the test database
            testCityId = city.getId();  // Store the city ID for testing

            // Create and persist a Clinic entity associated with the City
            Clinic clinic = new Clinic();
            clinic.setClinicName("Test Clinic");
            clinic.setAddress("123 Test St");
            clinic.setEmail("Clinic@clinic.com");
            clinic.setPhone("+45 29 83 45 12");
            clinic.setSpecialization(Specialization.CAT);
            clinic.setPostalCode(city.getPostalCode());  // Set the city for this clinic
            em.persist(clinic);  // Persist the clinic to the test database
            em.getTransaction().commit();

            testClinicId = clinic.getId();  // Store the generated clinic ID for testing
        }

        @Test
        void createClinic() {
            // Prepare ClinicDTO for creation
            ClinicDTO clinicDTO = new ClinicDTO();
            clinicDTO.setClinicName("New Clinic");
            clinicDTO.setAddress("456 New St");
            clinicDTO.setCityName("Test City");
            clinicDTO.setPostalCode(12345);
            clinicDTO.setEmail("newClinic@clinic.com");
            clinicDTO.setSpecialization(Specialization.CAT);
            clinicDTO.setPhone("+45 12 34 56 78");

            // Use the create method to add a new clinic
            ClinicDTO createdClinic = clinicDao.create(clinicDTO);

            // Verify that the created clinic is not null
            assertNotNull(createdClinic);

            // Verify the properties of the newly created clinic
            assertEquals("New Clinic", createdClinic.getClinicName());
            assertEquals("456 New St", createdClinic.getAddress());
            assertEquals("Test City", createdClinic.getCityName());
            assertEquals(12345, createdClinic.getPostalCode());
        }

        @Test
        void readClinicById() {
            // Use the read method to get the clinic by its ID
            ClinicDTO clinicDTO = clinicDao.read(testClinicId);

            // Verify that the clinic was found
            assertNotNull(clinicDTO);

            // Verify the clinic's properties
            assertEquals("Test Clinic", clinicDTO.getClinicName());
            assertEquals("123 Test St", clinicDTO.getAddress());
            assertEquals("Test City", clinicDTO.getCityName());
            assertEquals(12345, clinicDTO.getPostalCode());
        }

        @Test
        void readAllClinics() {
            // Use the readAll method to retrieve all clinics
            List<ClinicDTO> clinics = clinicDao.readAll();

            // Verify that the list is not empty and contains at least one clinic
            assertNotNull(clinics);
            assertFalse(clinics.isEmpty());

            // Verify the properties of the first clinic in the list
            ClinicDTO firstClinic = clinics.get(0);
            assertEquals("Test Clinic", firstClinic.getClinicName());
        }

        @Test
        void updateClinic() {
            // Prepare updated ClinicDTO
            ClinicDTO updatedClinicDTO = new ClinicDTO();
            updatedClinicDTO.setClinicName("Updated Clinic");
            updatedClinicDTO.setAddress("789 Updated St");
            updatedClinicDTO.setCityName("Test City");
            updatedClinicDTO.setPostalCode(12345);

            // Use the update method to update the clinic
            ClinicDTO updatedClinic = clinicDao.update(testClinicId, updatedClinicDTO);

            // Verify that the updated clinic is not null
            assertNotNull(updatedClinic);

            // Verify the updated properties of the clinic
            assertEquals("Updated Clinic", updatedClinic.getClinicName());
            assertEquals("789 Updated St", updatedClinic.getAddress());
            assertEquals("Test City", updatedClinic.getCityName());
            assertEquals(12345, updatedClinic.getPostalCode());
        }

        @Test
        void deleteClinicById() {
            // Delete the clinic using the delete method
            clinicDao.delete(testClinicId);

            // Verify that the clinic no longer exists in the database
            ClinicDTO deletedClinic = clinicDao.read(testClinicId);
            assertNull(deletedClinic);  // The clinic should be null since it was deleted
        }

        @Test
        void validatePrimaryKey() {
            // Validate that the primary key exists
            boolean isValid = clinicDao.validatePrimaryKey(testClinicId);
            assertTrue(isValid);

            // Validate that a non-existent primary key returns false
            boolean isInvalid = clinicDao.validatePrimaryKey(9999);
            assertFalse(isInvalid);
        }

        @AfterEach
        public void tearDown() {
            if (em != null) {
                em.close();  // Close the EntityManager
            }
            if (emf != null) {
                emf.close();  // Close the EntityManagerFactory
            }
        }

    }


