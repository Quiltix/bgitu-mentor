package com.bgitu.mentor.student.service;



import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.repository.ApplicationRepository;
import com.bgitu.mentor.student.dto.ApplicationOfStudentResponseDto;
import com.bgitu.mentor.student.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.dto.StudentDetailsUpdateRequestDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import com.bgitu.mentor.user.service.AbstractBaseUserService;
import com.bgitu.mentor.user.service.UserFinder;
import com.bgitu.mentor.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StudentServiceImpl extends AbstractBaseUserService<Student,StudentRepository> implements StudentService {


    private final ApplicationRepository applicationRepository;
    private final UserFinder userFinder;

    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder,
                              FileStorageService fileStorageService, ApplicationRepository applicationRepository,
                              UserService userService, UserFinder userFinder) {
        super(studentRepository, passwordEncoder, fileStorageService, "Студент",userService);
        this.applicationRepository = applicationRepository;
        this.userFinder = userFinder;
    }



    @Override
    public StudentDetailsResponseDto updateCard(Long studentId, StudentDetailsUpdateRequestDto dto, MultipartFile avatarFile) {
        Student student = userFinder.findStudentById(studentId);

        updateCardInternal(student, dto, avatarFile);

        return new StudentDetailsResponseDto(repository.save(student));
    }

    @Override
    public PersonalInfoDto updateProfile(Long studentId, UpdatePersonalInfo dto) {

        Student updatedStudent = super.updateProfileInternal(studentId, dto);

        return new PersonalInfoDto(updatedStudent);
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

    @Transactional
    public void terminateCurrentMentorship(Long studentId) {
        Student student = userFinder.findStudentById(studentId);
        Mentor mentor = student.getMentor();
        if (mentor == null) {
            throw new ResourceNotFoundException("У вас нет активного ментора, чтобы от него отказаться.");
        }
        breakMentorshipLink(student, mentor);
    }

    @Override
    public StudentDetailsResponseDto getPublicCardById(Long studentId) {

        Student student = userFinder.findStudentById(studentId);
        return new StudentDetailsResponseDto(student);
    }

    @Override
    public PersonalInfoDto getPersonalInfo(Long studentId) {
        Student student = userFinder.findStudentById(studentId);
        return new PersonalInfoDto(student);
    }

    private void breakMentorshipLink(Student student, Mentor mentor) {
        student.setMentor(null);
        mentor.getStudents().remove(student);
    }



}
