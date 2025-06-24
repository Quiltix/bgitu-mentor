package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.article.dto.ArticleShortDto;
import com.bgitu.mentor.article.model.Article;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
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
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorVoteRepository mentorVoteRepository;

    private final StudentService studentService;


    private final MentorRepository mentorRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;
    private final SpecialityRepository specialityRepository;



    public Mentor getMentorByAuth(Authentication authentication){
        String email = authentication.getName();

        return mentorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Mentor not found"));

    }

    public Mentor updateMentorProfile(Authentication authentication, UpdatePersonalInfo dto) {

        Mentor mentor = getMentorByAuth(authentication);

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            mentor.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            mentor.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return mentorRepository.save(mentor);
    }
    public Mentor updateMentorCard(Authentication authentication, UpdateMentorCardDto dto, MultipartFile avatarFile) {


        Mentor mentor = getMentorByAuth(authentication);

        if (dto.getDescription() != null) {
            mentor.setDescription(dto.getDescription());
        }
        if (dto.getFirstName() != null) {
            mentor.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            mentor.setLastName(dto.getLastName());
        }

        if (dto.getVkUrl() != null) {
            mentor.setVkUrl(dto.getVkUrl());
        }

        if (dto.getTelegramUrl() != null) {
            mentor.setTelegramUrl(dto.getTelegramUrl());
        }

        if (dto.getSpecialityId() != null) {
            Speciality speciality = specialityRepository.findById(dto.getSpecialityId())
                    .orElseThrow(() -> new IllegalArgumentException("Специальность не найдена"));
            mentor.setSpeciality(speciality);
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileStorageService.storeAvatar(avatarFile, "mentor_" + mentor.getId());
            mentor.setAvatarUrl(avatarUrl);
        }

        return mentorRepository.save(mentor);
    }

    public List<CardMentorDto> getTopMentors() {
        return mentorRepository.findTop3ByOrderByRankDesc()
                .stream()
                .map(CardMentorDto::new)
                .toList();
    }



    public List<MentorShortDto> getAllShort(Optional<Long> specialityId) {
        List<Mentor> mentors = specialityId
                .map(mentorRepository::findBySpecialityIdOrderByRankDesc)
                .orElseGet(mentorRepository::findAll);

        return mentors.stream()
                .filter(mentor -> mentor.getVkUrl() != null)
                .map(MentorShortDto::new)
                .collect(Collectors.toList());
    }


    public CardMentorDto getById(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ментор не найден"));
        return new CardMentorDto(mentor);
    }



    @Transactional
    public void voteMentor(Long mentorId, boolean upvote, Authentication auth) {
        Student student = studentService.getStudentByAuth(auth);
        Mentor mentor = mentorRepository.findById(mentorId)
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
        mentorRepository.save(mentor);
    }

    public List<MentorShortDto> searchMentors(String query) {
        if (query.length()>250){
            throw new IllegalStateException("Строка для поиска слишком длинная");
        }
        List<Mentor> mentors = mentorRepository.searchByNameOrDescription(query);
        return mentors.stream()
                .map(MentorShortDto::new)
                .collect(Collectors.toList());
    }


    public List<ArticleShortDto> getMentorArticles(Authentication authentication) {
        Mentor mentor = getMentorByAuth(authentication);

        List<Article> articles = mentor.getArticles();

        return articles.stream()
                .map(ArticleShortDto::new)
                .collect(Collectors.toList());
    }


}



