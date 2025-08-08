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
import com.bgitu.mentor.user.service.UserFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
  @Mock private UserFinder userFinder;
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

  @Test
  @DisplayName(
      "terminateMentorship | Должен вызвать LifecycleService, если ментор является владельцем студента")
  void terminateMentorshipWithStudent_shouldCallLifecycleService_whenMentorIsOwner() {

    long mentorId = 0L;
    long studentId = 1L;

    Mentor mentor = createMentor(mentorId);

    Student student = createStudent(studentId, mentor);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);

    when(userFinder.findStudentById(studentId)).thenReturn(student);

    mentorProfileService.terminateMentorshipWithStudent(mentorId, studentId);

    verify(mentorshipLifecycleService, times(1)).terminateLink(mentor, student);
  }

  @Test
  @DisplayName(
      "terminateMentorship | Должен выбросить SecurityException, если у студента нет ментора")
  void terminateMentorshipWithStudent_shouldThrowSecurityException_whenStudentHasNoMentor() {

    long mentorId = 0L;
    long studentId = 1L;

    Mentor mentor = createMentor(mentorId);

    Student student = new Student();
    student.setId(studentId);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);

    when(userFinder.findStudentById(studentId)).thenReturn(student);

    assertThrows(
        SecurityException.class,
        () -> mentorProfileService.terminateMentorshipWithStudent(mentorId, studentId));

    verify(mentorshipLifecycleService, never()).terminateLink(any(), any());
  }

  @Test
  @DisplayName(
      "terminateMentorship | Должен выбросить SecurityException, если у студента другой ментор")
  void terminateMentorshipWithStudent_shouldThrowSecurityException_whenStudentHasAnotherMentor() {

    long mentorId = 0L;
    long mentorOwnerId = 20L;
    long studentId = 1L;

    Mentor mentor = createMentor(mentorId);

    Mentor mentorOwner = createMentor(mentorOwnerId);

    Student student = createStudent(studentId, mentorOwner);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);

    when(userFinder.findStudentById(studentId)).thenReturn(student);

    assertThrows(
        SecurityException.class,
        () -> mentorProfileService.terminateMentorshipWithStudent(mentorId, studentId));

    verify(mentorshipLifecycleService, never()).terminateLink(any(), any());
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

    when(userFinder.findMentorById(mentorId)).thenReturn(existingMentor);
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
