package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.user.data.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.user.data.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorUpdateRequestDto;
import com.bgitu.mentor.mentor.service.MentorProfileService;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Mentor Profile", description = "Методы для взаимодействия с профилем ментора")
@RequestMapping("/api/profiles/mentor")
@PreAuthorize("hasRole('MENTOR')")
public class MentorProfileController {

  private final MentorProfileService mentorService;

  @Operation(
      summary = "Получение карточки ментора",
      description =
          "Доступно только для роли MENTOR. Возвращает полную информацию по текущему пользователю.")
  @GetMapping()
  public MentorDetailsResponseDto getCardMentor(Authentication authentication) {
    Long mentorId = SecurityUtils.getCurrentUserId(authentication);
    return mentorService.getMyCard(mentorId);
  }

  @Operation(
      summary = "Обновление карточки ментора",
      description =
          "Доступно только для роли MENTOR. Позволяет редактировать описание, направление и аватар.")
  @PatchMapping(consumes = "multipart/form-data")
  public MentorDetailsResponseDto updateMentorCard(
      Authentication authentication,
      @RequestPart("card") MentorUpdateRequestDto dto,
      @RequestPart(value = "avatar", required = false) MultipartFile avatarFile) {

    Long mentorId = SecurityUtils.getCurrentUserId(authentication);
    return mentorService.updateCard(mentorId, dto, avatarFile);
  }

  @Operation(
      summary = "Обновление профиля ментора",
      description = "Доступно только для роли MENTOR. Обновляет имя, фамилию, пароль и email.")
  @PatchMapping("/credentials")
  public UserCredentialsResponseDto updateMentorProfile(
      Authentication authentication, @RequestBody @Valid UserCredentialsUpdateRequestDto dto) {

    Long mentorId = SecurityUtils.getCurrentUserId(authentication);
    return mentorService.updateProfile(mentorId, dto);
  }

  @Operation(
      summary = "Получение моего профиля",
      description = "MENTOR. Возвращает профиль по авторизации")
  @GetMapping("/credentials")
  public UserCredentialsResponseDto getMentorProfile(Authentication authentication) {
    Long mentorId = SecurityUtils.getCurrentUserId(authentication);
    return mentorService.getPersonalInfo(mentorId);
  }

  @Operation(summary = "Получение всех статей ментора", description = "Доступно для роли MENTOR")
  @GetMapping("/articles")
  public List<ArticleSummaryResponseDto> getArticlesByMentor(Authentication authentication) {
    Long mentorId = SecurityUtils.getCurrentUserId(authentication);
    return mentorService.getMyArticles(mentorId);
  }

  @Operation(summary = "Получение всех студентов ментора", description = "Доступно для роли MENTOR")
  @GetMapping("/students")
  public List<StudentDetailsResponseDto> getStudents(Authentication authentication) {
    Long mentorId = SecurityUtils.getCurrentUserId(authentication);
    return mentorService.getMyStudents(mentorId);
  }

  @DeleteMapping("/students/{studentId}")
  @Operation(
      summary = "Прекратить менторство со студентом",
      description = "Позволяет ментору отказаться от своего студента.")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void terminateMentorshipWithStudent(
      Authentication authentication, @PathVariable Long studentId) {
    Long mentorId = SecurityUtils.getCurrentUserId(authentication);
    mentorService.terminateMentorshipWithStudent(mentorId, studentId);
  }
}
