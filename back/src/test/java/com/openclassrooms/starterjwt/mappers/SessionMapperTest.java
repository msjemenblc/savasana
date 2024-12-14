package com.openclassrooms.starterjwt.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private SessionDto sessionDto;
    private Session session;

    private Teacher teacher;
    private User user;

    @BeforeEach
    public void beforeEach() {
        teacher = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        user = User.builder()
            .id(1L)
            .email("jane.smith@test.com")
            .firstName("Jane")
            .lastName("Smith")
            .password("password")
            .build();

        List<User> userList = new ArrayList<>();
        userList.add(user);

        session = Session.builder()
            .id(1L)
            .name("Yoga Session")
            .date(new Date())
            .description("Relaxing yoga session !")
            .teacher(teacher)
            .users(userList)
            .build();

        sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setDescription("Relaxing yoga session !");
        sessionDto.setUsers(new ArrayList<>());
        sessionDto.getUsers().add(user.getId());
    }

    @Test
    public void testToEntity() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);

        Session sessionResult = sessionMapper.toEntity(sessionDto);

        verify(teacherService).findById(1L);
        verify(userService).findById(1L);

        assertThat(sessionResult).isNotNull();
        assertThat(sessionResult.getDescription()).isEqualTo("Relaxing yoga session !");
        assertThat(sessionResult.getTeacher()).isNotNull();
        assertThat(sessionResult.getTeacher().getFirstName()).isEqualTo("John");
        assertThat(sessionResult.getUsers()).isNotNull();
        assertThat(sessionResult.getUsers().size()).isEqualTo(1);
        assertThat(sessionResult.getUsers().get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    public void testToDto() {
        SessionDto sessionDtoResult = sessionMapper.toDto(session);

        assertThat(sessionDtoResult).isNotNull();
        assertThat(sessionDtoResult.getDescription()).isEqualTo("Relaxing yoga session !");
        assertThat(sessionDtoResult.getTeacher_id()).isEqualTo(teacher.getId());
        assertThat(sessionDtoResult.getUsers()).isNotNull();
        assertThat(sessionDtoResult.getUsers().get(0)).isEqualTo(user.getId());
    }

    @Test
    public void testToEntity_withNullValues() {
        sessionDto.setUsers(null);

        Session sessionResult = sessionMapper.toEntity(sessionDto);

        assertThat(sessionResult).isNotNull();
        assertThat(sessionResult.getUsers()).isEmpty();
    }

    @Test
    public void testToEntityList() {
        List<SessionDto> sessionDtos = new ArrayList<>();
        sessionDtos.add(sessionDto);

        List<Session> sessions = sessionMapper.toEntity(sessionDtos);

        assertThat(sessions).isNotNull();
        assertThat(sessions.size()).isEqualTo(1);
        assertThat(sessions.get(0).getName()).isEqualTo("Yoga Session");
    }

    @Test
    public void testToDtoList() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);

        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);

        assertThat(sessionDtos).isNotNull();
        assertThat(sessionDtos.size()).isEqualTo(1);
        assertThat(sessionDtos.get(0).getName()).isEqualTo("Yoga Session");
    }

}
