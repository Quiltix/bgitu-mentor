package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.mentor.data.MentorMapper;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MentorDirectoryServiceImplTest {

    private static Mentor createMentor(Long id, String firstName) {
        Mentor mentor = new Mentor();
        mentor.setId(id);
        mentor.setFirstName(firstName);

        return mentor;
    }



    @Mock
    private MentorRepository mentorRepository;
    @Mock
    private MentorMapper mentorMapper;

    @InjectMocks
    private MentorDirectoryServiceImpl mentorDirectoryService;


    @Test
    @DisplayName("Должен вернуть DTO ментора, если ментор найден по ID")
    void getMentorDetails_shouldReturnMentorDto_whenMentorExists(){
        long mentorId = 1L;

        Mentor fakeMentor = createMentor(mentorId,"Иван");


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


        assertThrows(EntityNotFoundException.class,()-> mentorDirectoryService.getMentorDetails(99L));
    }

    @Test
    @DisplayName("Должен найти и смапить менторов, применяя все фильтры")
    void findMentors_shouldApplyAllFiltersAndReturnPagedDto(){

        Long specialityId = 10L;
        String query = "java";
        Pageable pageable = PageRequest.of(0,5);

        Mentor mentor1 = createMentor(1L,"Анна");

        List<Mentor> fakeMentors = List.of(mentor1);

        Page<Mentor> fakeMentorPage = new PageImpl<>(fakeMentors,pageable,1);

        when(mentorRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(fakeMentorPage);

        when(mentorMapper.toSummaryDto(any(Mentor.class)))
                .thenReturn(new MentorSummaryResponseDto());

        ArgumentCaptor<Specification<Mentor>> argumentCaptor = ArgumentCaptor.forClass(Specification.class);


        Page<MentorSummaryResponseDto> resultPage = mentorDirectoryService.findMentors(specialityId,query,pageable);


        assertNotNull(resultPage);

        assertEquals(1,resultPage.getTotalElements());

        verify(mentorRepository).findAll(argumentCaptor.capture(),eq(pageable));

        Specification<Mentor> capturedSpec = argumentCaptor.getValue();

        assertNotNull(capturedSpec);

        verify(mentorMapper,times(fakeMentors.size())).toSummaryDto(any(Mentor.class));

    }

    @Test
    @DisplayName("Должен найти менторов без фильтров, если они не переданы")
    void findMentors_shouldWork_whenNoFiltersAreProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Mentor> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(mentorRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(emptyPage);

        ArgumentCaptor<Specification<Mentor>> specCaptor = ArgumentCaptor.forClass(Specification.class);


        mentorDirectoryService.findMentors(null, null, pageable);


        verify(mentorRepository).findAll(specCaptor.capture(), eq(pageable));

        Specification<Mentor> capturedSpec = specCaptor.getValue();


        assertNotNull(capturedSpec);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если строка больше 250")
    void findMentors_shouldThrowException_whenQueryIsLong() {
        // Arrange
        String query = "1".repeat( 251);

        assertThrows(IllegalStateException.class,()-> mentorDirectoryService.findMentors(null,query,null));
    }
}
