package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void beforeEach() {
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
    public void testFindById() {
        User foundUser = userService.findById(user.getId());

        assertThat(foundUser).isNotNull();

        assertThat(user.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("password");
    }

    @Test
    public void testDelete() {
        User userBeforeDelete = userRepository.findById(user.getId()).orElse(null);

        assertThat(userBeforeDelete).isNotNull();

        userService.delete(user.getId());

        User userAfterDelete = userRepository.findById(user.getId()).orElse(null);

        assertThat(userAfterDelete).isNull();
    }

}
