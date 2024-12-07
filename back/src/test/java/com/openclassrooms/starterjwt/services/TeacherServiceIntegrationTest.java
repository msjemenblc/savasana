package com.openclassrooms.starterjwt.services;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TeacherServiceIntegrationTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeAll
    public void beforeAll() {
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

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
    }

    @Test
    public void testFindAll() {
        List<Teacher> teachers = teacherService.findAll();

        assertThat(teachers).isNotNull();

        assertThat(teachers).hasSize(2);

        assertThat(teachers.get(0).getFirstName()).isEqualTo("John");
        assertThat(teachers.get(0).getLastName()).isEqualTo("Doe");

        assertThat(teachers.get(1).getFirstName()).isEqualTo("Jane");
        assertThat(teachers.get(1).getLastName()).isEqualTo("Smith");
    }

    @Test
    public void testFindById() {
        Teacher foundTeacher = teacherService.findById(1L);

        assertThat(foundTeacher).isNotNull();

        assertThat(foundTeacher.getFirstName()).isEqualTo("John");
        assertThat(foundTeacher.getLastName()).isEqualTo("Doe");
    }

}
