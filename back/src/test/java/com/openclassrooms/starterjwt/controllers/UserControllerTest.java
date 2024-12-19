package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    private UserController userController;

    private User user;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(userService, userMapper);

        user = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .build();
    }

    @Test
    public void testFindById_success() {
        UserDto userDto = new UserDto();

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());

        verify(userService).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    public void testFindById_notFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userService).findById(1L);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void testFindById_badRequest() {
        ResponseEntity<?> response = userController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    
        verifyNoInteractions(userService);
        verifyNoInteractions(userMapper);
    }

}
