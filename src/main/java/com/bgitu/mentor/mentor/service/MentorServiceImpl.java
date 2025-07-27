package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.article.dto.ArticleShortDto;
import com.bgitu.mentor.article.model.Article;
import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.dto.MentorShortDto;
import com.bgitu.mentor.mentor.dto.UpdateMentorCardDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.model.MentorVote;
import com.bgitu.mentor.mentor.model.Speciality;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import com.bgitu.mentor.mentor.repository.MentorVoteRepository;
import com.bgitu.mentor.mentor.repository.SpecialityRepository;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import com.bgitu.mentor.student.service.StudentService;
import com.bgitu.mentor.user.repository.BaseUserRepository;
import com.bgitu.mentor.user.service.AbstractBaseUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MentorServiceImpl extends AbstractBaseUserService<Mentor, MentorRepository> implements MentorService {

    // Специфичные зависимости для ментора
    private final MentorVoteRepository mentorVoteRepository;
    private final SpecialityRepository specialityRepository;
    private final StudentService studentService; // Внедряем интерфейс!
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

    @Cacheable(value = "topMentors")
    public List<CardMentorDto> getTopMentors() {
        return repository.findTop3ByOrderByRankDesc()
                .stream()
                .map(CardMentorDto::new)
                .toList();
    }



    @Override
    public List<MentorShortDto> getAllShort(Optional<Long> specialityId) {
        List<Mentor> mentors = specialityId
                .map(repository::findBySpecialityIdOrderByRankDesc)
                .orElseGet(repository::findAll);

        return mentors.stream()
                .filter(mentor -> mentor.getVkUrl() != null)
                .map(MentorShortDto::new)
                .collect(Collectors.toList());
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
    public List<MentorShortDto> searchMentors(String query) {
        if (query.length()>250){
            throw new IllegalStateException("Строка для поиска слишком длинная");
        }
        List<Mentor> mentors = repository.searchByNameOrDescription(query);
        return mentors.stream()
                .map(MentorShortDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleShortDto> getMentorArticles(Authentication authentication) {
        Mentor mentor = getByAuth(authentication);

        List<Article> articles = mentor.getArticles();

        return articles.stream()
                .map(ArticleShortDto::new)
                .collect(Collectors.toList());
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



