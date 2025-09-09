package com.bgitu.mentor.student.service;

import com.bgitu.mentor.user.data.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.user.data.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.service.MentorDirectoryService;
import com.bgitu.mentor.mentorship.service.MentorshipLifecycleService;
import com.bgitu.mentor.student.data.StudentMapper;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsUpdateRequestDto;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.data.repository.StudentRepository;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.service.BaseUserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

  // --- Основные зависимости ---
  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  // --- Зависимости от других доменных сервисов (Фасад) ---
  private final MentorDirectoryService mentorDirectoryService;
  private final MentorshipLifecycleService mentorshipLifecycleService;

  private final BaseUserManagementService baseUserManagementService; // <-- КОМПОЗИЦИЯ

  @Override
  @Transactional
  public StudentDetailsResponseDto updateCard(
      Long studentId, StudentDetailsUpdateRequestDto dto, MultipartFile avatarFile) {
    Student student = findStudentById(studentId);

    baseUserManagementService.updateCard(student, dto, avatarFile);

    return studentMapper.toDetailsDto(studentRepository.save(student));
  }

  @Override
  public UserCredentialsResponseDto updateProfile(
      Long studentId, UserCredentialsUpdateRequestDto dto) {
    BaseUser updatedUser = baseUserManagementService.updateProfile(studentId, dto);

    return studentMapper.toCredentialsDto((Student) updatedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public MentorDetailsResponseDto getMentorOfStudent(Long studentId) {
    Student student = findStudentById(studentId);
    Mentor mentor = student.getMentor();
    if (mentor == null) {
      throw new ResourceNotFoundException("У студента нет ментора");
    }

    return mentorDirectoryService.getMentorDetails(mentor.getId(), studentId);
  }

  @Override
  public void terminateCurrentMentorship(Long studentId) {

    mentorshipLifecycleService.terminateLinkByStudent(studentId);
  }

  @Override
  @Transactional(readOnly = true)
  public StudentDetailsResponseDto getPublicCardById(Long studentId) {

    Student student = findStudentById(studentId);
    return studentMapper.toDetailsDto(student);
  }

  @Override
  @Transactional(readOnly = true)
  public UserCredentialsResponseDto getPersonalInfo(Long studentId) {
    Student student = findStudentById(studentId);
    return studentMapper.toCredentialsDto(student);
  }

  private Student findStudentById(Long studentId) {
    return studentRepository
        .findById(studentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Студент с id=" + studentId + " не найден"));
  }
}
