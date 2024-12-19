package com.openclassrooms.starterjwt.services;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class TeacherServiceIntegrationTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    public void beforeEach() {
        teacher1 = Teacher.builder()
            .firstName("John")
            .lastName("Doe")
            .build();

        teacher2 = Teacher.builder()
            .firstName("Jane")
            .lastName("Smith")
            .build();

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
    }

    @Test
    public void testFindById() {
        Teacher foundTeacher = teacherService.findById(teacher1.getId());

        assertThat(foundTeacher).isNotNull();

        assertThat(foundTeacher.getFirstName()).isEqualTo("John");
        assertThat(foundTeacher.getLastName()).isEqualTo("Doe");
    }

}
