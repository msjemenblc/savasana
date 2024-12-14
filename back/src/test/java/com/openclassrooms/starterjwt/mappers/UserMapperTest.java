package com.openclassrooms.starterjwt.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
            .id(1L)
            .email("john.doe@test.com")
            .firstName("John")
            .lastName("Doe")
            .password("password")
            .build();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("john.doe@test.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("password");
    }

    @Test
    public void testToEntity() {
        User userResult = userMapper.toEntity(userDto);
        
        assertThat(userResult).isNotNull();
        assertThat(userResult.getId()).isEqualTo(1L);
        assertThat(userResult.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(userResult.getFirstName()).isEqualTo("John");
        assertThat(userResult.getLastName()).isEqualTo("Doe");
        assertThat(userResult.getPassword()).isEqualTo("password");
    }

    @Test
    public void testToDto() {
        UserDto userDtoResult = userMapper.toDto(user);

        assertThat(userDtoResult).isNotNull();
        assertThat(userDtoResult.getId()).isEqualTo(1L);
        assertThat(userDtoResult.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(userDtoResult.getFirstName()).isEqualTo("John");
        assertThat(userDtoResult.getLastName()).isEqualTo("Doe");
        assertThat(userDtoResult.getPassword()).isEqualTo("password");
    }

    @Test
    public void testToEntityList() {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);

        List<User> users = userMapper.toEntity(userDtos);

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getId()).isEqualTo(1L);
        assertThat(users.get(0).getEmail()).isEqualTo("john.doe@test.com");
        assertThat(users.get(0).getFirstName()).isEqualTo("John");
        assertThat(users.get(0).getLastName()).isEqualTo("Doe");
        assertThat(users.get(0).getPassword()).isEqualTo("password");
    }

    @Test
    public void testToDtoList() {
        List<User> users = new ArrayList<>();
        users.add(user);

        List<UserDto> userDtos = userMapper.toDto(users);

        assertThat(userDtos).isNotNull();
        assertThat(userDtos.size()).isEqualTo(1);
        assertThat(userDtos.get(0).getId()).isEqualTo(1L);
        assertThat(userDtos.get(0).getEmail()).isEqualTo("john.doe@test.com");
        assertThat(userDtos.get(0).getFirstName()).isEqualTo("John");
        assertThat(userDtos.get(0).getLastName()).isEqualTo("Doe");
        assertThat(userDtos.get(0).getPassword()).isEqualTo("password");
    }

}
