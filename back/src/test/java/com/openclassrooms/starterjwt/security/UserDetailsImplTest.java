package com.openclassrooms.starterjwt.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

public class UserDetailsImplTest {

    @Test
    public void testEquals() {
        UserDetailsImpl user1 = UserDetailsImpl
            .builder()
            .id(1L)
            .username("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .admin(true)
            .password("password")
            .build();

        UserDetailsImpl user2 = UserDetailsImpl
            .builder()
            .id(1L)
            .username("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .admin(true)
            .password("password")
            .build();

        UserDetailsImpl user3 = UserDetailsImpl
            .builder()
            .id(2L)
            .username("jane.smith@test.com")
            .firstName("Jane")
            .lastName("Smith")
            .admin(false)
            .password("password123")
            .build();

        Object nonUserObject = new Object();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);

        assertThat(user1).isNotEqualTo(null);
        assertThat(user1).isNotEqualTo(nonUserObject);
    }

    @Test
    public void testGetAdmin() {
        UserDetailsImpl admin = UserDetailsImpl
            .builder()
            .admin(true)
            .build();

        Boolean isAdmin = admin.getAdmin();

        assertTrue(isAdmin);
    }

}
