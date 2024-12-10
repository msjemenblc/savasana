package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session1;
    private Session session2;

    private User user;

    @BeforeAll
    public void beforeAll() {
        session1 = Session.builder()
            .name("Yoga session")
            .date(new Date())
            .description("Relaxing yoga session !")
            .users(new ArrayList<>())
            .build();

        session2 = Session.builder()
            .name("Yoga lesson")
            .date(new Date())
            .description("Cool yoga session !")
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
        sessionRepository.save(session2);
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testFindById_success() throws Exception {
        mockMvc.perform(get("/api/session/" + session1.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(session1.getId()))
            .andExpect(jsonPath("$.name").value("Yoga session"));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testFindById_notFound() throws Exception {
        mockMvc.perform(get("/api/session/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/session/abc"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testCreate_validSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(user.getId());
        sessionDto.setDescription("Session created !");
        sessionDto.setUsers(new ArrayList<>());

        mockMvc.perform(post("/api/session")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(sessionDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("New Session"))
            .andExpect(jsonPath("$.description").value("Session created !"));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testCreate_invalidSession() throws Exception {
        SessionDto sessionDto = new SessionDto();

        mockMvc.perform(post("/api/session")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(sessionDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testUpdate_validSession() throws Exception {
        Session toUpdateSession = new Session();
        toUpdateSession.setName("To update session");
        toUpdateSession.setDate(java.sql.Date.valueOf("2024-12-01"));
        toUpdateSession.setDescription("Session created !");
        sessionRepository.save(toUpdateSession);

        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setName("testece");
        updatedSessionDto.setDate(new Date());
        updatedSessionDto.setTeacher_id(user.getId());
        updatedSessionDto.setDescription("Session updated !");
        updatedSessionDto.setUsers(new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(put("/api/session/" + toUpdateSession.getId())
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(updatedSessionDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("testece"));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testUpdate_invalidSession() throws Exception {
        SessionDto sessionDto = new SessionDto();

        mockMvc.perform(put("/api/session/abc")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(sessionDto)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(""))
            .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testDelete() throws Exception {
        Session toDeleteSession = new Session();
        toDeleteSession.setName("To update session");
        toDeleteSession.setDate(java.sql.Date.valueOf("2024-12-01"));
        toDeleteSession.setDescription("Session created !");
        sessionRepository.save(toDeleteSession);

        mockMvc.perform(delete("/api/session/" + toDeleteSession.getId()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testDelete_notFound() throws Exception {
        mockMvc.perform(delete("/api/session/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testDelete_badRequest() throws Exception {
        mockMvc.perform(delete("/api/session/abc"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testParticipate_badRequest() throws Exception {
        mockMvc.perform(post("/api/session/abc/participate/xyz"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    public void testNoLongerParticipate_badRequest() throws Exception {
        mockMvc.perform(delete("/api/session/abc/participate/xyz"))
            .andExpect(status().isBadRequest());
    }
}
