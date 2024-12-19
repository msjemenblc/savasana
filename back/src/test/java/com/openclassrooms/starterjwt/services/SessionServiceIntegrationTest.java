package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class SessionServiceIntegrationTest {

    @Autowired 
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session1;

    private User user;

    @BeforeEach
    public void beforeEach() {
        session1 = Session.builder()
            .name("Yoga session")
            .date(new Date())
            .description("Relaxing yoga session !")
            .users(new ArrayList<>())
            .build();

        user = User.builder()
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .admin(false)
            .build();

        sessionRepository.save(session1);

        userRepository.save(user);
    }

    @Test
    public void testCreate() {
        Session newSession = Session.builder()
            .name("Yoga session")
            .date(new Date())
            .description("Session created !")
            .users(new ArrayList<>())
            .build();

        Session createdSession = sessionService.create(newSession);

        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getId()).isNotNull(); // ID généré automatiquement
        assertThat(createdSession.getName()).isEqualTo("Yoga session");
        assertThat(createdSession.getDescription()).isEqualTo("Session created !");
    
        // Vérification depuis la DB
        Session fromDBSession = sessionRepository.findById(createdSession.getId()).orElse(null);

        assertThat(fromDBSession).isNotNull();
        assertThat(fromDBSession.getName()).isEqualTo("Yoga session");
        assertThat(fromDBSession.getDescription()).isEqualTo("Session created !");

    }

    @Test
    public void testDelete() {
        Session sessionBeforeDelete = sessionRepository.findById(session1.getId()).orElse(null);

        assertThat(sessionBeforeDelete).isNotNull();

        sessionService.delete(session1.getId());

        Session sessionAfterDelete = sessionRepository.findById(session1.getId()).orElse(null);

        assertThat(sessionAfterDelete).isNull();
    }

    @Test
    public void testUpdate() {
        Session existingSessionData = Session.builder()
            .name("Initial Yoga session")
            .date(new Date())
            .description("Initial description")
            .users(new ArrayList<>())
            .build();

        Session existingSession = sessionRepository.save(existingSessionData);

        Session updatedSessionData = Session.builder()
            .name("Updated Yoga session")
            .date(new Date()) // Nouveaux paramètres
            .description("Updated description")
            .users(new ArrayList<>())
            .build();

        Session updatedSession = sessionService.update(existingSession.getId(), updatedSessionData);

        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getId()).isEqualTo(existingSession.getId());
        assertThat(updatedSession.getName()).isEqualTo("Updated Yoga session");
        assertThat(updatedSession.getDescription()).isEqualTo("Updated description");

        // Vérification depuis la DB
        Session fromDBSession = sessionRepository.findById(existingSession.getId()).orElse(null);

        assertThat(fromDBSession).isNotNull();
        assertThat(fromDBSession.getName()).isEqualTo("Updated Yoga session");
        assertThat(fromDBSession.getDescription()).isEqualTo("Updated description");
    }

    @Test
    public void testParticipate() {
        sessionService.participate(session1.getId(), user.getId());

        Session sessionWithParticipatingUser = sessionRepository.findById(session1.getId()).orElse(null);

        assertThat(sessionWithParticipatingUser.getUsers()).hasSize(1);
        assertThat(sessionWithParticipatingUser.getUsers().get(0).getId()).isEqualTo(user.getId());
    }

    @Test
    public void testNoLongerParticipate() {
        sessionService.participate(session1.getId(), user.getId());
        sessionService.noLongerParticipate(session1.getId(), user.getId());

        Session sessionWithoutParticipatingUser = sessionRepository.findById(session1.getId()).orElse(null);

        assertThat(sessionWithoutParticipatingUser.getUsers()).hasSize(0);
        assertThat(sessionWithoutParticipatingUser.getUsers()).isEmpty();
    }

}
