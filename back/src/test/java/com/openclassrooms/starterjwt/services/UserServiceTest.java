package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .build();
    }

    @Test
    public void testFindById_withExistingId() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(user));

        User user = userService.findById(1L);

        assertNotNull(user, "L'utilisateur trouvé ne doit pas être nul");

        assertEquals("john.doe@test.com", user.getEmail(), "l'email de l'utilisateur doit être 'john.doe@test.com'");
        assertEquals("John", user.getFirstName(), "le prénom de l'utilisateur doit être 'John'");
        assertEquals("Doe", user.getLastName(), "le nom de famille de l'utilisateur doit être 'Doe'");
        assertEquals("password", user.getPassword(), "le mot de passe de l'utilisateur doit être 'password'");
    }

    @Test
    public void testFindById_withNonExistingId() {
        when(userRepository.findById(2L))
            .thenReturn(Optional.empty());

        User user = userService.findById(2L);

        assertNull(user, "L'utilisateur trouvé doit être nul quand l'ID n'existe pas");
    }

    @Test
    public void testDelete() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

}
