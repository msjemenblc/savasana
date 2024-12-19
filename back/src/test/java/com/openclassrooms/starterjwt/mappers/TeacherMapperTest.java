package com.openclassrooms.starterjwt.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;

@ExtendWith(MockitoExtension.class)
public class TeacherMapperTest {

    @InjectMocks
    private TeacherMapperImpl teacherMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void beforeEach() {
        teacher = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
    }

    @Test
    public void testToEntity() {
        Teacher teacherResult = teacherMapper.toEntity(teacherDto);

        assertThat(teacherResult).isNotNull();
        assertThat(teacherResult.getId()).isEqualTo(1L);
        assertThat(teacherResult.getFirstName()).isEqualTo("John");
        assertThat(teacherResult.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testToDto() {
        TeacherDto teacherDtoResult = teacherMapper.toDto(teacher);

        assertThat(teacherDtoResult).isNotNull();
        assertThat(teacherDtoResult.getId()).isEqualTo(1L);
        assertThat(teacherDtoResult.getFirstName()).isEqualTo("John");
        assertThat(teacherDtoResult.getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testToEntityList() {
        List<TeacherDto> teacherDtos = new ArrayList<>();
        teacherDtos.add(teacherDto);

        List<Teacher> sessions = teacherMapper.toEntity(teacherDtos);

        assertThat(sessions).isNotNull();
        assertThat(sessions.size()).isEqualTo(1);
        assertThat(sessions.get(0).getId()).isEqualTo(1L);
        assertThat(sessions.get(0).getFirstName()).isEqualTo("John");
        assertThat(sessions.get(0).getLastName()).isEqualTo("Doe");
    }
    
    @Test
    public void testToDtoList() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        List<TeacherDto> teacherDtos = teacherMapper.toDto(teachers);

        assertThat(teacherDtos).isNotNull();
        assertThat(teacherDtos.size()).isEqualTo(1);
        assertThat(teacherDtos.get(0).getId()).isEqualTo(1L);
        assertThat(teacherDtos.get(0).getFirstName()).isEqualTo("John");
        assertThat(teacherDtos.get(0).getLastName()).isEqualTo("Doe");
    }

}
