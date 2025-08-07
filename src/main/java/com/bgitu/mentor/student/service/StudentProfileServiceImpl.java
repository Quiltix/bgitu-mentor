package com.bgitu.mentor.student.service;



import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
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
import com.bgitu.mentor.user.service.BaseUserManagementService;
import com.bgitu.mentor.user.service.UserFinder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    // --- Зависимости от инфраструктурных/общих сервисов ---
    private final UserFinder userFinder;
    private final BaseUserManagementService baseUserManagementService; // <-- КОМПОЗИЦИЯ



    @Override
    @Transactional
    public StudentDetailsResponseDto updateCard(Long studentId, StudentDetailsUpdateRequestDto dto, MultipartFile avatarFile) {
        Student student = userFinder.findStudentById(studentId);

        baseUserManagementService.updateCard(student, dto, avatarFile);

        return studentMapper.toDetailsDto(studentRepository.save(student));
    }

    @Override
    @Transactional
    public UserCredentialsResponseDto updateProfile(Long studentId, UserCredentialsUpdateRequestDto dto) {

        baseUserManagementService.updateProfile(studentId, dto);

        Student updatedStudent = userFinder.findStudentById(studentId);
        return studentMapper.toCredentialsDto(updatedStudent);
    }


    @Override
    public MentorDetailsResponseDto getMentorOfStudent(Long studentId) {
        Student student = userFinder.findStudentById(studentId);
        Mentor mentor = student.getMentor();
        if (mentor == null) {
            throw new ResourceNotFoundException("У студента нет ментора");
        }

        return mentorDirectoryService.getMentorDetails(mentor.getId());
    }
    @Override // <-- Добавляем @Override, так как метод теперь в интерфейсе
    @Transactional
    public void terminateCurrentMentorship(Long studentId) {
        Student student = userFinder.findStudentById(studentId);
        Mentor currentMentor = student.getMentor();


        if (currentMentor == null) {
            throw new ResourceNotFoundException("У вас нет активного ментора, чтобы от него отказаться.");
        }

        mentorshipLifecycleService.terminateLink(currentMentor, student);
    }

    @Override
    public StudentDetailsResponseDto getPublicCardById(Long studentId) {

        Student student = userFinder.findStudentById(studentId);
        return studentMapper.toDetailsDto(student);
    }

    @Override
    public UserCredentialsResponseDto getPersonalInfo(Long studentId) {
        Student student = userFinder.findStudentById(studentId);
        return studentMapper.toCredentialsDto(student);
    }


}
