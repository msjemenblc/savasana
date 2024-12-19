package com.openclassrooms.starterjwt.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TeacherTest {

    @Mock
    private Teacher teacherMock;

    @Test
    public void testSetters() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        teacher.setId(2L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertThat(teacher.getId()).isEqualTo(2L);
        assertThat(teacher.getFirstName()).isEqualTo("Jane");
        assertThat(teacher.getLastName()).isEqualTo("Smith");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testToString() {
        Teacher teacher = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        String expected = "Teacher(id=1, lastName=Doe, firstName=John, createdAt=null, updatedAt=null)";
        assertThat(expected).isEqualTo(teacher.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Teacher teacher1 = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        Teacher teacher2 = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        Teacher teacher3 = Teacher.builder()
            .id(2L)
            .firstName("Jane")
            .lastName("Smith")
            .build();

        assertThat(teacher1).isEqualTo(teacher2);
        assertThat(teacher1).isNotEqualTo(teacher3);

        assertThat(teacher1.hashCode()).isEqualTo(teacher2.hashCode());
        assertThat(teacher1.hashCode()).isNotEqualTo(teacher3.hashCode());
    }
}
