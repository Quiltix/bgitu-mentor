package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.service.ArticleService;
import com.bgitu.mentor.mentor.data.MentorMapper;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorUpdateRequestDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.mentorship.service.MentorshipLifecycleService;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.speciality.service.SpecialityService;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.service.StudentDirectoryService;
import com.bgitu.mentor.user.service.BaseUserManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorProfileServiceImplTest {

  private static Mentor createMentor(Long id) {
    Mentor mentor = new Mentor();
    mentor.setId(id);
    return mentor;
  }

  private static Student createStudent(Long id, Mentor mentor) {
    Student student = new Student();
    student.setId(id);
    student.setMentor(mentor);
    return student;
  }

  private static Speciality createSpeciality(Long id, String name) {
    Speciality speciality = new Speciality();
    speciality.setId(id);
    speciality.setName(name);
    return speciality;
  }

  @Mock private MentorRepository mentorRepository;
  @Mock private MentorMapper mentorMapper;
  @Mock private ArticleService articleService;
  @Mock private StudentDirectoryService studentDirectoryService;
  @Mock private MentorshipLifecycleService mentorshipLifecycleService;
  @Mock private BaseUserManagementService baseUserManagementService;
  @Mock private SpecialityService specialityService;

  @InjectMocks private MentorProfileServiceImpl mentorProfileService;

  @Test
  @DisplayName("getMyArticles | Должен вернуть корректные статьи у ментора, если существуют")
  void getMyArticles_returnsArticlesDto() {

    Long mentorId = 1L;

    ArticleSummaryResponseDto summaryResponseDto1 = new ArticleSummaryResponseDto();
    ArticleSummaryResponseDto summaryResponseDto2 = new ArticleSummaryResponseDto();
    summaryResponseDto1.setId(0L);
    summaryResponseDto2.setId(1L);

    List<ArticleSummaryResponseDto> fakeArticles =
        List.of(summaryResponseDto1, summaryResponseDto2);

    when(articleService.findArticlesByAuthor(mentorId)).thenReturn(fakeArticles);

    List<ArticleSummaryResponseDto> responseDtos = mentorProfileService.getMyArticles(mentorId);

    assertNotNull(responseDtos);

    assertEquals(2, responseDtos.size());

    assertSame(fakeArticles, responseDtos);

    verify(articleService, times(1)).findArticlesByAuthor(mentorId);
  }

  @Test
  @DisplayName("getMyArticles | Должен вернуть пустой список, если статей нет")
  void getMyArticles_returnsEmptyList() {

    Long mentorId = 1L;

    List<ArticleSummaryResponseDto> fakeArticles = List.of();

    when(articleService.findArticlesByAuthor(mentorId)).thenReturn(fakeArticles);

    List<ArticleSummaryResponseDto> responseDtos = mentorProfileService.getMyArticles(mentorId);

    assertNotNull(responseDtos);

    assertTrue(responseDtos.isEmpty());

    assertSame(fakeArticles, responseDtos);

    verify(articleService, times(1)).findArticlesByAuthor(mentorId);

    verifyNoInteractions(studentDirectoryService, mentorshipLifecycleService);
  }

  @DisplayName(
      "terminateMentorship | Должен вызвать terminateLinkByMentor с корректными аргументами")
  @Test
  void terminateMentorshipWithStudent_shouldCallTerminateLinkByMentor() {
    long mentorId = 0L;
    long studentId = 1L;

    mentorProfileService.terminateMentorshipWithStudent(mentorId, studentId);

    verify(mentorshipLifecycleService, times(1)).terminateLinkByMentor(mentorId, studentId);
  }

  @Test
  @DisplayName(
      "updateCard | Должен обновить все поля, включая специальность, если все данные переданы")
  void updateCard_shouldUpdateAllFields_whenAllDataProvided() {

    long mentorId = 1L;
    long specialityId = 10L;

    MentorUpdateRequestDto updateDto = new MentorUpdateRequestDto();
    updateDto.setSpecialityId(specialityId);
    MultipartFile fakeAvatar =
        new MockMultipartFile("avatar", "avatar.jpg", "image/jpeg", "some-image-bytes".getBytes());

    Mentor existingMentor = createMentor(mentorId);
    Speciality newSpeciality = createSpeciality(specialityId, "Java");

    when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(existingMentor));
    when(specialityService.getById(specialityId)).thenReturn(newSpeciality);
    when(mentorRepository.save(any(Mentor.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(mentorMapper.toDetailsDto(any(Mentor.class))).thenReturn(new MentorDetailsResponseDto());

    mentorProfileService.updateCard(mentorId, updateDto, fakeAvatar);

    verify(baseUserManagementService, times(1)).updateCard(existingMentor, updateDto, fakeAvatar);

    verify(specialityService, times(1)).getById(specialityId);

    verify(mentorRepository, times(1)).save(existingMentor);

    verify(mentorMapper, times(1)).toDetailsDto(existingMentor);

    assertEquals(newSpeciality, existingMentor.getSpeciality());
  }
}
