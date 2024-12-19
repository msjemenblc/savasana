package com.openclassrooms.starterjwt.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SessionTest {

    @Mock
    private Session session;

    @Test
    public void testSetters() {
        LocalDateTime now = LocalDateTime.now();

        session = Session.builder()
            .id(1L)
            .name("Yoga session")
            .date(new Date())
            .description("Relaxing yoga session !")
            .users(new ArrayList<>())
            .build();

        Teacher teacher = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        session.setTeacher(teacher);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getCreatedAt()).isEqualTo(now);
        assertThat(session.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testToString() {
        session = Session.builder()
            .id(1L)
            .name("Yoga session")
            .description("Relaxing yoga session !")
            .build();

        String expected = "Session(id=1, name=Yoga session, date=null, description=Relaxing yoga session !, teacher=null, users=null, createdAt=null, updatedAt=null)";
        assertThat(expected).isEqualTo(session.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Session session1 = Session.builder()
            .id(1L)
            .name("Yoga Session")
            .date(new Date())
            .description("Relaxing yoga session !")
            .build();
        
        Session session2 = Session.builder()
            .id(1L)
            .name("Yoga Session")
            .date(new Date())
            .description("Relaxing yoga session !")
            .build();

        Session session3 = Session.builder()
            .id(2L)
            .name("Yoga lesson")
            .date(new Date())
            .description("Relaxing yoga lesson !")
            .build();

        assertThat(session1).isEqualTo(session2);
        assertThat(session1).isNotEqualTo(session3);

        assertThat(session1.hashCode()).isEqualTo(session2.hashCode());
        assertThat(session1.hashCode()).isNotEqualTo(session3.hashCode());
    }

}
