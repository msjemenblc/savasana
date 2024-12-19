package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    private TeacherController teacherController;

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void beforeEach() {
        teacherController = new TeacherController(teacherService, teacherMapper);
    
        teacher1 = Teacher.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();

        teacher2 = Teacher.builder()
            .id(2L)
            .firstName("Jane")
            .lastName("Smith")
            .build();
    }

    @Test
    public void testFindById_success() {
        TeacherDto mockTeacherDto = new TeacherDto();

        when(teacherService.findById(teacher1.getId())).thenReturn(teacher1);
        when(teacherMapper.toDto(teacher1)).thenReturn(mockTeacherDto);

        ResponseEntity<?> response = teacherController.findById(String.valueOf(teacher1.getId()));
        
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(teacherService).findById(teacher1.getId());
        verify(teacherMapper).toDto(teacher1);
    }

    @Test
    public void testFindById_notFound() {
        Long teacherId = 999L;

        when(teacherService.findById(teacherId)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById(String.valueOf(teacherId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(teacherService).findById(teacherId);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    public void testFindById_badRequest() {
        String invalidTeacherId = "invalid";

        ResponseEntity<?> response = teacherController.findById(invalidTeacherId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    
        verifyNoInteractions(teacherService);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    public void testFindAll() {
        List<Teacher> teachers = List.of(
            teacher1, teacher2
        );

        List<TeacherDto> teacherDtos = List.of(
            new TeacherDto(), new TeacherDto()
        );

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);
    
        ResponseEntity<?> response = teacherController.findAll();
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDtos, response.getBody());
        
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teachers);
    }

    @Test
    public void testFindAll_withNoTeachers() {
        List<Teacher> teachers = List.of();
        List<TeacherDto> teacherDtos = List.of();
    
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);
    
        ResponseEntity<?> response = teacherController.findAll();
    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDtos, response.getBody());

        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teachers);
    }

}
