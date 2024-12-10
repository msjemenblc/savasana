package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeAll
    public void beforeAll() {
        user = User.builder()
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .admin(false)
            .build();

        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "john.doe@test.com", roles = {"ADMIN"})
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/user/" + user.getId()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com", roles = {"ADMIN"})
    public void testDelete_notFound() throws Exception {
        mockMvc.perform(delete("/api/user/9999"))
            .andExpect(status().isNotFound());
    }

    // Quand le mail joint est différent de celui demandé
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"USER"})
    public void testDelete_unauthorized() throws Exception {
        mockMvc.perform(delete("/api/user/" + user.getId()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "john.doe@test.com", roles = {"ADMIN"})
    public void testDelete_badRequest() throws Exception {
        mockMvc.perform(delete("/api/user/abc"))
            .andExpect(status().isBadRequest());
    }

}
