package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void beforeEach() {
        teacher1 = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        teacher2 = Teacher.builder()
            .id(2L)
            .firstName("Jane")
            .lastName("Smith")
            .build();
    }

    @Test
    public void testFindAll() {
        when(teacherRepository.findAll())
            .thenReturn(Arrays.asList(teacher1, teacher2));

        List<Teacher> teachers = teacherService.findAll();

        assertNotNull(teachers, "La liste des professeurs ne doit pas être nulle");

        assertEquals(2, teachers.size(), "Le nombre de professeurs dans la liste doit être égal à 2");

        assertEquals("John", teachers.get(0).getFirstName(), "Le prénom du premier professeur doit être 'John'");
        assertEquals("Doe", teachers.get(0).getLastName(), "Le nom de famille du premier professeur doit être 'Doe'");

        assertEquals("Jane", teachers.get(1).getFirstName(), "Le prénom du deuxième professeur doit être 'Jane'");
        assertEquals("Smith", teachers.get(1).getLastName(), "Le nom de famille du deuxième professeur doit être 'Smith'");
    }

    @Test
    public void testFindAll_withEmptyRepository() {
        when(teacherRepository.findAll())
            .thenReturn(Collections.emptyList());

        List<Teacher> teachers = teacherService.findAll();

        assertNotNull(teachers, "La liste des professeurs ne doit pas être nulle");
        assertEquals(0, teachers.size(), "La liste des professeurs doit être vide");
    }

    @Test
    public void testFindById_withExistingId() {
        when(teacherRepository.findById(1L))
            .thenReturn(Optional.of(teacher1));

        Teacher teacher = teacherService.findById(1L);

        assertNotNull(teacher, "Le professeur trouvé ne doit pas être nul");

        assertEquals("John", teacher.getFirstName(), "Le prénom du professeur doit être 'John'");
        assertEquals("Doe", teacher.getLastName(), "Le nom de famille du professeur doit être 'Doe'");
    }

    @Test
    public void testFindById_withNonExistingId() {
        when(teacherRepository.findById(3L))
            .thenReturn(Optional.empty());

        Teacher teacher = teacherService.findById(3L);

        assertNull(teacher, "Le professeur trouvé doit être nul quand l'ID n'existe pas");
    }

}
