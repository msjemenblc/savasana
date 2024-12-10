package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    private SessionController sessionController;

    private Session session1;
    private Session session2;

    private User user;

    @BeforeEach
    void beforeEach() {
        sessionController = new SessionController(sessionService, sessionMapper);

        session1 = Session.builder()
            .id(1L)
            .name("Yoga session")
            .date(new Date())
            .description("Relaxing yoga session !")
            .users(new ArrayList<>())
            .build();

        session2 = Session.builder()
            .id(2L)
            .name("Yoga lesson")
            .date(new Date())
            .description("Cool yoga session !")
            .users(new ArrayList<>())
            .build();

        user = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .admin(false)
            .build();
    }

    @Test
    public void testFindAll() {
        List<Session> sessions = List.of(
            session1, session2
        );

        List<SessionDto> sessionDtos = List.of(
            new SessionDto(), new SessionDto()
        );

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());

        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
    }

    @Test
    public void testFindAll_withNoSessions() {
        List<Session> sessions = List.of();
        List<SessionDto> sessionDtos = List.of();

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());

        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
    }

    @Test
    public void testParticipate() {
        doNothing().when(sessionService).participate(anyLong(), anyLong());

        ResponseEntity<?> response = sessionController.participate(session1.getId().toString(), user.getId().toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(sessionService, times(1)).participate(anyLong(), anyLong());
    }

    @Test
    public void testNoLongerParticipate() {
        doNothing().when(sessionService).noLongerParticipate(anyLong(), anyLong());

        ResponseEntity<?> response = sessionController.noLongerParticipate(session1.getId().toString(), user.getId().toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(sessionService, times(1)).noLongerParticipate(anyLong(), anyLong());
    }
}
