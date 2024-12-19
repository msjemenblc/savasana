package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session1;
    private Session session2;

    private User user;

    @BeforeEach
    void beforeEach() {
        session1 = Session.builder()
            .id(1L)
            .name("Yoga session")
            .description("Relaxing yoga session !")
            .users(new ArrayList<>())
            .build();

        session2 = Session.builder()
            .id(2L)
            .name("Yoga lesson")
            .description("Cool yoga session !")
            .users(new ArrayList<>())
            .build();

        user = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .build();
    }

    @Test
    public void testCreate() {
        when(sessionRepository.save(session1))
            .thenReturn(session1);

        Session createdSession = sessionService.create(session1);

        assertEquals(session1, createdSession, "La fonction create doit retourner la session créée");

        verify(sessionRepository, times(1)).save(session1);
    }

    @Test
    public void testDelete() {
        sessionService.delete(1L);

        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindAll() {
        when(sessionRepository.findAll())
            .thenReturn(Arrays.asList(session1, session2));

        List<Session> sessions = sessionService.findAll();

        assertNotNull(sessions, "La liste des sessions ne doit pas être nulle");

        assertEquals(2, sessions.size(), "Le nombre de sessions dans la liste doit être égal à 2");

        assertEquals("Yoga session", sessions.get(0).getName(), "Le nom de la première session doit être 'Yoga session'");
        assertEquals("Relaxing yoga session !", sessions.get(0).getDescription(), "Le nom de la première session doit être 'Relaxing yoga session !'");
    
        assertEquals("Yoga lesson", sessions.get(1).getName(), "Le nom de la deuxième session doit être 'Yoga lesson'");
        assertEquals("Cool yoga session !", sessions.get(1).getDescription(), "Le nom de la deuxième session doit être 'Cool yoga session !'");
    }

    @Test
    public void testFindAll_withEmptyRepository() {
        when(sessionRepository.findAll())
            .thenReturn(Collections.emptyList());

        List<Session> sessions = sessionService.findAll();

        assertNotNull(sessions, "La liste des sessions ne doit pas être nulle");
        assertEquals(0, sessions.size(), "La liste des sessions doit être vide");
    }

    @Test
    public void testFindById_withExistingId() {
        when(sessionRepository.findById(1L))
            .thenReturn(Optional.of(session1));

        Session session = sessionService.getById(1L);

        assertNotNull(session, "La session trouvée ne doit pas être nulle");

        assertEquals("Yoga session", session.getName(), "Le nom de la session doit être 'Yoga session'");
        assertEquals("Relaxing yoga session !", session.getDescription(), "Le nom de la session doit être 'Relaxing yoga session !'");
    }

    @Test
    public void testFindById_withNonExistingId() {
        when(sessionRepository.findById(3L))
            .thenReturn(Optional.empty());

        Session session = sessionService.getById(3L);

        assertNull(session, "La session trouvée doit être nulle quand l'ID n'existe pas");
    }

    @Test
    public void testUpdate() {
        when(sessionRepository.save(session1))
            .thenReturn(session1);

        Session createdSession = sessionService.update(1L, session1);

        verify(sessionRepository, times(1)).save(session1);
        
        assertEquals(session1, createdSession, "La fonction create doit retourner la session créée");
    }

    @Test
    public void testParticipate() {
        when(sessionRepository.findById(1L))
            .thenReturn(Optional.of(session1));

        when(userRepository.findById(1L))
            .thenReturn(Optional.of(user));

        sessionService.participate(1L, 1L);

        verify(sessionRepository, times(1)).save(session1);

        assertTrue(session1.getUsers().contains(user), "La session doit avoir l'utilisateur enregistré");
    }

    @Test
    public void testNoLongerParticipate() {
        when(sessionRepository.findById(1L))
            .thenReturn(Optional.of(session1));

        session1.getUsers().add(user);

        sessionService.noLongerParticipate(1L, 1L);

        verify(sessionRepository, times(1)).save(session1);

        assertFalse(session1.getUsers().contains(user));
    }
}
