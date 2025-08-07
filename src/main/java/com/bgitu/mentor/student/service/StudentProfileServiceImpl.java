package com.bgitu.mentor.student.service;



import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.mentorship.data.repository.ApplicationRepository;
import com.bgitu.mentor.mentorship.service.MentorshipLifecycleService;
import com.bgitu.mentor.student.data.StudentMapper;
import com.bgitu.mentor.student.data.dto.ApplicationOfStudentResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsUpdateRequestDto;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.data.repository.StudentRepository;
import com.bgitu.mentor.user.service.AbstractBaseUserService;
import com.bgitu.mentor.user.service.UserFinder;
import com.bgitu.mentor.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StudentProfileServiceImpl extends AbstractBaseUserService<Student,StudentRepository> implements StudentProfileService {


    private final ApplicationRepository applicationRepository;
    private final UserFinder userFinder;
    private final MentorshipLifecycleService mentorshipLifecycleService;
    private final StudentMapper studentMapper;


    public StudentProfileServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder,
                                     FileStorageService fileStorageService, ApplicationRepository applicationRepository,
                                     UserService userService, UserFinder userFinder,
                                     MentorshipLifecycleService mentorshipLifecycleService,
                                     StudentMapper studentMapper) {
        super(studentRepository, passwordEncoder, fileStorageService, "Студент",userService);
        this.applicationRepository = applicationRepository;
        this.userFinder = userFinder;
        this.mentorshipLifecycleService = mentorshipLifecycleService;
        this.studentMapper = studentMapper;
    }



    @Override
    public StudentDetailsResponseDto updateCard(Long studentId, StudentDetailsUpdateRequestDto dto, MultipartFile avatarFile) {
        Student student = userFinder.findStudentById(studentId);

        updateCardInternal(student, dto, avatarFile);

        return studentMapper.toDetailsDto(repository.save(student));
    }

    @Override
    public UserCredentialsResponseDto updateProfile(Long studentId, UserCredentialsUpdateRequestDto dto) {

        Student updatedStudent = super.updateProfileInternal(studentId, dto);

        return studentMapper.toCredentialsDto(updatedStudent);
    }


    @Override
    public MentorDetailsResponseDto getMentorOfStudent(Long studentId) {
        Student student = userFinder.findStudentById(studentId);

        Mentor mentor = student.getMentor();
        if (mentor == null) {
            throw new IllegalStateException("У студента пока нет назначенного ментора");
        }

        return new MentorDetailsResponseDto(mentor);
    }

    @Override
    public List<ApplicationOfStudentResponseDto> getStudentApplications(Long studentId) {

        Student student = userFinder.findStudentById(studentId);

        List<Application> applications = applicationRepository.findAllByStudent(student);

        return applications.stream()
                .map(ApplicationOfStudentResponseDto::new)
                .toList();
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
