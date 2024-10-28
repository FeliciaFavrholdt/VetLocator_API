package dat.dto;

import dat.entities.Animal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalDTOTest {

    private AnimalDTO animalDTO;

    @BeforeEach
    void setUp() {
        animalDTO = new AnimalDTO();
    }

    @AfterEach
    void tearDown() {
        animalDTO = null;
    }

    @Test
    void toEntity() {
        animalDTO.setId(1L);
        animalDTO.setName("Rex");
        animalDTO.setSpecies("Dog");
        animalDTO.setBreed("Golden Retriever");
        animalDTO.setAge(5);
        animalDTO.setOwnerId(100L);

        Animal animalEntity = animalDTO.toEntity();

        assertEquals(animalDTO.getId(), animalEntity.getId());
        assertEquals(animalDTO.getName(), animalEntity.getName());
        assertEquals(animalDTO.getSpecies(), animalEntity.getSpecies());
        assertEquals(animalDTO.getBreed(), animalEntity.getBreed());
        assertEquals(animalDTO.getAge(), animalEntity.getAge());
        assertEquals(animalDTO.getOwnerId(), animalEntity.getOwner().getId());
    }

    @Test
    void getName() {
        animalDTO.setName("Rex");
        assertEquals("Rex", animalDTO.getName());
    }

    @Test
    void setName() {
        animalDTO.setName("Rex");
        assertEquals("Rex", animalDTO.getName());
    }

    @Test
    void testEquals() {
        AnimalDTO animalDTO1 = new AnimalDTO();
        animalDTO1.setId(1L);
        animalDTO1.setName("Rex");

        AnimalDTO animalDTO2 = new AnimalDTO();
        animalDTO2.setId(1L);
        animalDTO2.setName("Rex");

        assertEquals(animalDTO1, animalDTO2);
    }

    @Test
    void testHashCode() {
        AnimalDTO animalDTO1 = new AnimalDTO();
        animalDTO1.setId(1L);
        animalDTO1.setName("Rex");

        AnimalDTO animalDTO2 = new AnimalDTO();
        animalDTO2.setId(1L);
        animalDTO2.setName("Rex");

        assertEquals(animalDTO1.hashCode(), animalDTO2.hashCode());
    }

    @Test
    void testToString() {
        animalDTO.setId(1L);
        animalDTO.setName("Rex");
        animalDTO.setSpecies("Dog");

        String expected = "AnimalDTO{id=1, name='Rex', species='Dog'}";
        assertEquals(expected, animalDTO.toString());
    }

    @Test
    void builder() {
        AnimalDTO animalDTO = AnimalDTO.builder()
                .id(1L)
                .name("Rex")
                .species("Dog")
                .breed("Golden Retriever")
                .age(5)
                .ownerId(100L)
                .build();

        assertEquals(1, animalDTO.getId());
        assertEquals("Rex", animalDTO.getName());
        assertEquals("Dog", animalDTO.getSpecies());
        assertEquals("Golden Retriever", animalDTO.getBreed());
        assertEquals(5, animalDTO.getAge());
        assertEquals(100, animalDTO.getOwnerId());
    }
}
