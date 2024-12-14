package com.openclassrooms.starterjwt.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Mock
    private User user;

    @Test
    public void testSetters() {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .admin(false)
            .build();

        user.setId(2L);
        user.setEmail("jane.smith@test.com");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setPassword("password123");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getEmail()).isEqualTo("jane.smith@test.com");
        assertThat(user.getFirstName()).isEqualTo("Jane");
        assertThat(user.getLastName()).isEqualTo("Smith");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.isAdmin()).isEqualTo(true);
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    public void testToString() {
        user = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .admin(false)
            .build();

        String expected = "User(id=1, email=john.doe@test.com, lastName=Doe, firstName=John, password=password, admin=false, createdAt=null, updatedAt=null)";
        assertThat(expected).isEqualTo(user.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .build();

        User user2 = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .build();

        User user3 = User.builder()
            .id(2L)
            .email("jane.smith@test.com")
            .firstName("Jane")
            .lastName("Smith")
            .password("password123")
            .build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

}
