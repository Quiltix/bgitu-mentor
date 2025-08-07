package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.mentor.data.MentorMapper;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MentorDirectoryServiceImplTest {

    @Mock
    private MentorRepository mentorRepository;
    @Mock
    MentorMapper mentorMapper;

    @InjectMocks
    private MentorDirectoryServiceImpl mentorDirectoryService;


    @Test
    @DisplayName("Должен вернуть DTO ментора, если ментор найден по ID")
    void getMentorDetails_shouldReturnMentorDto_whenMentorExists(){
        long mentorId = 1L;

        Mentor fakeMentor = new Mentor();
        fakeMentor.setId(mentorId);
        fakeMentor.setFirstName("Иван");

        MentorDetailsResponseDto fakeDto = new MentorDetailsResponseDto();
        fakeDto.setId(mentorId);
        fakeDto.setFirstName("Иван");

        when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(fakeMentor));
        when(mentorMapper.toDetailsDto(fakeMentor)).thenReturn(fakeDto);


        MentorDetailsResponseDto resultDto = mentorDirectoryService.getMentorDetails(mentorId);


        assertNotNull(resultDto);
        assertEquals(fakeDto.getId(), resultDto.getId());
        assertEquals(fakeDto.getFirstName(), resultDto.getFirstName());

        verify(mentorRepository, times(1)).findById(mentorId);
        verify(mentorMapper, times(1)).toDetailsDto(fakeMentor);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если ментор не существует")
    void getMentorDetails_shouldThrowException_whenMentorDoesNotExist() {

        when(mentorRepository.findById(99L)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class,()-> {
            mentorDirectoryService.getMentorDetails(99L);
        });
    }
}
