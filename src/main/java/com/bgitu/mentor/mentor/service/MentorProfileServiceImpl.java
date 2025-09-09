package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.service.ArticleService;
import com.bgitu.mentor.user.data.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.user.data.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.exception.dto.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.MentorMapper;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorUpdateRequestDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.mentorship.service.MentorshipLifecycleService;
import com.bgitu.mentor.speciality.service.SpecialityService;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.service.StudentDirectoryService;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.service.BaseUserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorProfileServiceImpl implements MentorProfileService {

  private final MentorRepository mentorRepository;
  private final MentorMapper mentorMapper;

  private final ArticleService articleService;
  private final StudentDirectoryService studentDirectoryService;
  private final MentorshipLifecycleService mentorshipLifecycleService;

  private final BaseUserManagementService baseUserManagementService;
  private final SpecialityService specialityService;

  @Override
  public UserCredentialsResponseDto updateProfile(
      Long mentorId, UserCredentialsUpdateRequestDto dto) {

    BaseUser updatedUser = baseUserManagementService.updateProfile(mentorId, dto);
    return mentorMapper.toCredentialsDto((Mentor) updatedUser);
  }

  @Override
  @Transactional
  public MentorDetailsResponseDto updateCard(
      Long mentorId, MentorUpdateRequestDto dto, MultipartFile avatarFile) {
    Mentor mentor = findMentorById(mentorId);

    baseUserManagementService.updateCard(mentor, dto, avatarFile);

    if (dto.getSpecialityId() != null) {
      Speciality speciality = specialityService.getById(dto.getSpecialityId());
      mentor.setSpeciality(speciality);
    }

    return mentorMapper.toDetailsDto(mentorRepository.save(mentor));
  }

  @Override
  @Transactional(readOnly = true)
  public MentorDetailsResponseDto getMyCard(Long id) {
    Mentor mentor = findMentorById(id);

    return mentorMapper.toDetailsDto(mentor);
  }

  @Override
  public List<ArticleSummaryResponseDto> getMyArticles(Long mentorId) {
    return articleService.findArticlesByAuthor(mentorId);
  }

  @Override
  @Transactional(readOnly = true)
  public UserCredentialsResponseDto getPersonalInfo(Long mentorId) {
    Mentor mentor = findMentorById(mentorId);

    return mentorMapper.toCredentialsDto(mentor);
  }

  @Override
  public List<StudentDetailsResponseDto> getMyStudents(Long mentorId) {
    return studentDirectoryService.findAllStudentsByMentor(mentorId);
  }

  @Override
  public void terminateMentorshipWithStudent(Long mentorId, Long studentId) {
    mentorshipLifecycleService.terminateLinkByMentor(mentorId, studentId);
  }

  private Mentor findMentorById(Long mentorId) {
    return mentorRepository
        .findById(mentorId)
        .orElseThrow(() -> new ResourceNotFoundException("Ментор не найден"));
  }
}
