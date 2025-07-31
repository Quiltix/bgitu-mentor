package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.article.data.dto.ArticleShortDto;
import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.data.MentorSpecifications;
import com.bgitu.mentor.mentor.data.dto.CardMentorDto;
import com.bgitu.mentor.mentor.data.dto.MentorShortDto;
import com.bgitu.mentor.mentor.data.dto.UpdateMentorCardDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.model.Speciality;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.mentor.data.repository.SpecialityRepository;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.user.service.AbstractBaseUserService;
import com.bgitu.mentor.user.service.UserFinder;
import com.bgitu.mentor.user.service.UserService;
import com.bgitu.mentor.vote.service.MentorVoteHandler;
import com.bgitu.mentor.vote.service.VotingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class MentorServiceImpl extends AbstractBaseUserService<Mentor, MentorRepository> implements MentorService {

    private final SpecialityRepository specialityRepository;
    private final VotingService votingService;
    private final MentorVoteHandler mentorVoteHandler;
    private final UserFinder userFinder;


    public MentorServiceImpl(MentorRepository mentorRepository, PasswordEncoder passwordEncoder,
                             FileStorageService fileStorageService, VotingService votingService,
                             SpecialityRepository specialityRepository, MentorVoteHandler mentorVoteHandler,
                             UserService userService, UserFinder userFinder) {
        super(mentorRepository, passwordEncoder, fileStorageService, "Ментор", userService);
        this.mentorVoteHandler = mentorVoteHandler;
        this.specialityRepository = specialityRepository;
        this.votingService = votingService;
        this.userFinder = userFinder;
    }


    @Override
    public PersonalInfoDto updateProfile(Long mentorId, UpdatePersonalInfo dto) {

        Mentor updatedMentor = super.updateProfileInternal(mentorId, dto);

        return new PersonalInfoDto(updatedMentor);
    }



    @Override
    public CardMentorDto updateCard(Long mentorId, UpdateMentorCardDto dto, MultipartFile avatarFile) {

        Mentor mentor = userFinder.findMentorById(mentorId);

        updateCardInternal(mentor, dto, avatarFile);

        if (dto.getSpecialityId() != null) {
            Speciality speciality = specialityRepository.findById(dto.getSpecialityId())
                    .orElseThrow(() -> new EntityNotFoundException("Специальность с id=" + dto.getSpecialityId() + " не найдена"));
            mentor.setSpeciality(speciality);
        }

        return new CardMentorDto(repository.save(mentor));
    }

    @Override
    public Page<MentorShortDto> findMentors(Long specialityId, String query, Pageable pageable) {

        Specification<Mentor> specification = Specification.not(null);

        if (specialityId != null) {
            specification.and(MentorSpecifications.hasSpeciality(specialityId));
        }

        if (query != null && !query.isBlank()) {
            if (query.length() > 250) {
                throw new IllegalStateException("Строка для поиска слишком длинная");
            }
            specification.and(MentorSpecifications.nameOrDescriptionContains(query));
        }

        Page<Mentor> mentorPage = repository.findAll(specification, pageable);

        return mentorPage.map(MentorShortDto::new);
    }


    @Override
    public CardMentorDto getPublicCardById(Long id) {
        Mentor mentor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ментор не найден"));
        return new CardMentorDto(mentor);
    }


    @Override
    @Transactional
    public void voteMentor(Long mentorId, boolean upvote, Long userId) {
        votingService.vote(mentorId, userId, upvote, mentorVoteHandler);
    }


    @Override
    public List<ArticleShortDto> getMentorArticles(Long mentorId) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        List<Article> articles = mentor.getArticles();

        return articles.stream()
                .map(ArticleShortDto::new)
                .toList();
    }

    @Override
    public PersonalInfoDto getPersonalInfo(Long mentorId) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        return new PersonalInfoDto(mentor);
    }

    @Override
    public List<StudentCardDto> getAllStudentsForMentor(Long mentorId) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        return mentor.getStudents().stream()
                .map(StudentCardDto::new)
                .toList();
    }

    @Override
    @Transactional
    public void terminateMentorshipWithStudent(Long mentorId, Long studentId) {

        Mentor mentor = userFinder.findMentorById(mentorId);

        Student student = userFinder.findStudentById(studentId);

        if (student.getMentor() == null || !student.getMentor().getId().equals(mentor.getId())) {
            throw new SecurityException("Студент не является вашим подопечным.");
        }

        breakMentorshipLink(student, mentor);
    }

    private void breakMentorshipLink(Student student, Mentor mentor) {
        student.setMentor(null);
        mentor.getStudents().remove(student);
    }

}



