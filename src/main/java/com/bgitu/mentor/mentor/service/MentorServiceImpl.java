package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.article.dto.ArticleShortDto;
import com.bgitu.mentor.article.model.Article;
import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.data.MentorSpecifications;
import com.bgitu.mentor.mentor.data.dto.CardMentorDto;
import com.bgitu.mentor.mentor.data.dto.MentorShortDto;
import com.bgitu.mentor.mentor.data.dto.UpdateMentorCardDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.model.MentorVote;
import com.bgitu.mentor.mentor.data.model.Speciality;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.mentor.data.repository.MentorVoteRepository;
import com.bgitu.mentor.mentor.data.repository.SpecialityRepository;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import com.bgitu.mentor.student.service.StudentService;
import com.bgitu.mentor.user.repository.BaseUserRepository;
import com.bgitu.mentor.user.service.AbstractBaseUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class MentorServiceImpl extends AbstractBaseUserService<Mentor, MentorRepository> implements MentorService {

    // Специфичные зависимости для ментора
    private final MentorVoteRepository mentorVoteRepository;
    private final SpecialityRepository specialityRepository;
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public MentorServiceImpl(MentorRepository mentorRepository, PasswordEncoder passwordEncoder,
                             FileStorageService fileStorageService, MentorVoteRepository mentorVoteRepository,
                             SpecialityRepository specialityRepository, StudentService studentService,
                             BaseUserRepository baseUserRepository,StudentRepository studentRepository) {
        super(mentorRepository, passwordEncoder, fileStorageService, "Ментор", baseUserRepository);
        this.mentorVoteRepository = mentorVoteRepository;
        this.specialityRepository = specialityRepository;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }



    @Override
    public Mentor updateCard(Authentication authentication, UpdateMentorCardDto dto, MultipartFile avatarFile) {

        Mentor mentor = getByAuth(authentication);

        updateCardInternal(mentor, dto, avatarFile);

        if (dto.getSpecialityId() != null) {
            Speciality speciality = specialityRepository.findById(dto.getSpecialityId())
                    .orElseThrow(() -> new EntityNotFoundException("Специальность с id=" + dto.getSpecialityId() + " не найдена"));
            mentor.setSpeciality(speciality);
        }

        return repository.save(mentor);
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
    public CardMentorDto getById(Long id) {
        Mentor mentor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ментор не найден"));
        return new CardMentorDto(mentor);
    }


    @Override
    @Transactional
    public void voteMentor(Long mentorId, boolean upvote, Authentication auth) {
        Student student = studentService.getByAuth(auth);
        Mentor mentor = repository.findById(mentorId)
                .orElseThrow(() ->  new EntityNotFoundException("Ментор не найден"));

        if (mentorVoteRepository.existsByMentorAndStudent(mentor, student)) {
            throw new IllegalStateException("Вы уже голосовали за этого ментора");
        }

        MentorVote vote = new MentorVote();
        vote.setMentor(mentor);
        vote.setStudent(student);
        vote.setUpvote(upvote);
        mentorVoteRepository.save(vote);

        int change = upvote ? 1 : -1;
        mentor.setRank(mentor.getRank() + change);
        repository.save(mentor);
    }


    @Override
    public List<ArticleShortDto> getMentorArticles(Authentication authentication) {
        Mentor mentor = getByAuth(authentication);

        List<Article> articles = mentor.getArticles();

        return articles.stream()
                .map(ArticleShortDto::new)
                .toList();
    }

    @Override
    public List<StudentCardDto> getAllStudentsForMentor(Authentication authentication) {
        Mentor mentor = getByAuth(authentication);

        return mentor.getStudents().stream()
                .map(StudentCardDto::new)
                .toList();
    }


    @Override
    public Mentor findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ментор с id=" + id + " не найден"));
    }

    @Override
    @Transactional
    public void terminateMentorshipWithStudent(Authentication authentication, Long studentId) {

        Mentor mentor = getByAuth(authentication);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Студент не найден."));

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



