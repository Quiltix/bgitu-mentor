package com.bgitu.mentor.mentorship.service;


import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import com.bgitu.mentor.mentor.service.MentorService;
import com.bgitu.mentor.mentorship.dto.UpdateApplicationStatusDto;
import com.bgitu.mentor.mentorship.dto.ApplicationResponseDto;
import com.bgitu.mentor.mentorship.dto.MentorshipRequestDto;
import com.bgitu.mentor.mentorship.dto.StudentPreviewDto;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import com.bgitu.mentor.mentorship.repository.ApplicationRepository;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import com.bgitu.mentor.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {

    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final ApplicationRepository applicationRepository;
    private final StudentService studentService;
    private final MentorService mentorServiceImpl;

    @Override
    public void requestMentorship(Authentication authentication, MentorshipRequestDto dto) {
        Student student = studentService.getByAuth(authentication);

        Mentor mentor = mentorRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));


        boolean alreadyApplied = applicationRepository.existsByStudentIdAndMentorId(student.getId(), mentor.getId());
        if (alreadyApplied) {
            throw new IllegalArgumentException("Вы уже отправляли заявку этому ментору.");
        }
        Application app = new Application();
        app.setStudent(student);
        app.setMentor(mentor);
        app.setMessage(dto.getMessage());
        app.setStatus(ApplicationStatus.PENDING);

        applicationRepository.save(app);
    }

    @Override
    @Transactional
    public void respondToApplication(UpdateApplicationStatusDto dto) {
        Application app = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (dto.getAccepted()) {
            // 1. Принятие заявки
            app.setStatus(ApplicationStatus.ACCEPTED);

            // 2. Привязка студента к ментору
            Student student = app.getStudent();
            student.setMentor(app.getMentor());
            studentRepository.save(student);

            // 3. Поиск всех других заявок этого студента
            List<Application> otherApplications = applicationRepository.findByStudentId(student.getId()).stream()
                    .filter(a -> !a.getId().equals(app.getId()) && a.getStatus() == ApplicationStatus.PENDING)
                    .toList();

            // 4. Установка статуса EXPIRED
            for (Application otherApp : otherApplications) {
                otherApp.setStatus(ApplicationStatus.EXPIRED);
            }

            // 5. Сохранение всех изменённых заявок
            applicationRepository.saveAll(otherApplications);
        } else {
            // Отклонение заявки
            app.setStatus(ApplicationStatus.REJECTED);
        }

        applicationRepository.save(app);
    }

    @Override
    public List<ApplicationResponseDto> getApplicationsForMentor(Authentication authentication, ApplicationStatus status) {
        Long mentorId = mentorServiceImpl.getByAuth(authentication).getId();

        List<Application> applications;
        if (status != null) {
            applications = applicationRepository.findByMentorIdAndStatus(mentorId, status);
        } else {
            applications = applicationRepository.findByMentorId(mentorId);
        }

        return applications.stream().map(app -> {
            Student student = app.getStudent();
            StudentPreviewDto studentDto = new StudentPreviewDto(
                    student.getId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getDescription(),
                    student.getAvatarUrl(),
                    student.getVkUrl(),
                    student.getTelegramUrl()
            );

            return new ApplicationResponseDto(
                    app.getId(),
                    app.getMessage(),
                    app.getStatus(),
                    studentDto
            );
        }).toList();
    }
}
