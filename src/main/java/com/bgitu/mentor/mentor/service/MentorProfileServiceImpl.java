package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorUpdateRequestDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.model.Speciality;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.mentor.data.repository.SpecialityRepository;
import com.bgitu.mentor.mentorship.service.MentorshipLifecycleService;
import com.bgitu.mentor.student.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.user.service.AbstractBaseUserService;
import com.bgitu.mentor.user.service.UserFinder;
import com.bgitu.mentor.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class MentorProfileServiceImpl extends AbstractBaseUserService<Mentor, MentorRepository> implements MentorProfileService {

    private final SpecialityRepository specialityRepository;

    private final UserFinder userFinder;
    private final MentorshipLifecycleService mentorshipLifecycleService;


    public MentorProfileServiceImpl(MentorRepository mentorRepository, PasswordEncoder passwordEncoder,
                                    FileStorageService fileStorageService,
                                    SpecialityRepository specialityRepository,
                                    UserService userService, UserFinder userFinder,
                                    MentorshipLifecycleService mentorshipLifecycleService) {
        super(mentorRepository, passwordEncoder, fileStorageService, "Ментор", userService);

        this.specialityRepository = specialityRepository;

        this.userFinder = userFinder;
        this.mentorshipLifecycleService = mentorshipLifecycleService;
    }


    @Override
    public UserCredentialsResponseDto updateProfile(Long mentorId, UserCredentialsUpdateRequestDto dto) {

        Mentor updatedMentor = super.updateProfileInternal(mentorId, dto);

        return new UserCredentialsResponseDto(updatedMentor);
    }



    @Override
    public MentorDetailsResponseDto updateCard(Long mentorId, MentorUpdateRequestDto dto, MultipartFile avatarFile) {

        Mentor mentor = userFinder.findMentorById(mentorId);

        updateCardInternal(mentor, dto, avatarFile);

        if (dto.getSpecialityId() != null) {
            Speciality speciality = specialityRepository.findById(dto.getSpecialityId())
                    .orElseThrow(() -> new EntityNotFoundException("Специальность с id=" + dto.getSpecialityId() + " не найдена"));
            mentor.setSpeciality(speciality);
        }

        return new MentorDetailsResponseDto(repository.save(mentor));
    }




    @Override
    public MentorDetailsResponseDto getMyCard(Long id) {
        Mentor mentor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ментор не найден"));
        return new MentorDetailsResponseDto(mentor);
    }



    @Override
    public List<ArticleSummaryResponseDto> getMyArticles(Long mentorId) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        List<Article> articles = mentor.getArticles();

        return articles.stream()
                .map(ArticleSummaryResponseDto::new)
                .toList();
    }

    @Override
    public UserCredentialsResponseDto getPersonalInfo(Long mentorId) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        return new UserCredentialsResponseDto(mentor);
    }

    @Override
    public List<StudentDetailsResponseDto> getMyStudents(Long mentorId) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        return mentor.getStudents().stream()
                .map(StudentDetailsResponseDto::new)
                .toList();
    }

    @Override
    @Transactional
    public void terminateMentorshipWithStudent(Long mentorId, Long studentId) {
        Mentor mentor = userFinder.findMentorById(mentorId);
        Student student = userFinder.findStudentById(studentId);

        // Проверка безопасности остается в этом сервисном методе, так как
        // она относится к бизнес-правилу этого конкретного use-case.
        if (student.getMentor() == null || !student.getMentor().getId().equals(mentor.getId())) {
            throw new SecurityException("Студент не является вашим подопечным.");
        }

        // Делегируем фактическое действие нашему новому, специализированному сервису.
        mentorshipLifecycleService.terminateLink(mentor, student);
    }


}



