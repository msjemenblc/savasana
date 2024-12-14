package com.openclassrooms.starterjwt.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
public class JwtUtilsIntegrationTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void testJwtGenerationAndValidation() {
        UserDetailsImpl userDetails = UserDetailsImpl
            .builder()
            .id(1L)
            .username("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .admin(true)
            .password("password")
            .build();

            Authentication authentication = Mockito.mock(Authentication.class);
            Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);
    }

}
