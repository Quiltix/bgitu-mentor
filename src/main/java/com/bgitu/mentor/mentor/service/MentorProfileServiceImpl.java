package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.service.ArticleService;
import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.mentor.data.MentorMapper;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorUpdateRequestDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.mentorship.service.MentorshipLifecycleService;
import com.bgitu.mentor.speciality.service.SpecialityService;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.service.StudentDirectoryService;
import com.bgitu.mentor.user.service.BaseUserManagementService;
import com.bgitu.mentor.user.service.UserFinder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorProfileServiceImpl implements MentorProfileService {

    // --- Основные зависимости ---
    private final MentorRepository mentorRepository;
    private final MentorMapper mentorMapper;

    // --- Зависимости от других доменных сервисов (Фасад) ---
    private final ArticleService articleService;
    private final StudentDirectoryService studentDirectoryService;
    private final MentorshipLifecycleService mentorshipLifecycleService;

    // --- Зависимости от инфраструктурных/общих сервисов ---
    private final UserFinder userFinder;
    private final BaseUserManagementService baseUserManagementService;
    private final SpecialityService specialityService;


    @Override
    @Transactional
    public UserCredentialsResponseDto updateProfile(Long mentorId, UserCredentialsUpdateRequestDto dto) {

        baseUserManagementService.updateProfile(mentorId, dto);

        Mentor updatedMentor = userFinder.findMentorById(mentorId);
        return mentorMapper.toCredentialsDto(updatedMentor);
    }



    @Override
    @Transactional
    public MentorDetailsResponseDto updateCard(Long mentorId, MentorUpdateRequestDto dto, MultipartFile avatarFile) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        baseUserManagementService.updateCard(mentor, dto, avatarFile);

        // 2. Применяем специфичную для ментора логику
        if (dto.getSpecialityId() != null) {
            Speciality speciality = specialityService.getById(dto.getSpecialityId());
            mentor.setSpeciality(speciality);
        }

        return mentorMapper.toDetailsDto(mentorRepository.save(mentor));
    }




    @Override
    public MentorDetailsResponseDto getMyCard(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ментор не найден"));

        return mentorMapper.toDetailsDto(mentor);
    }



    @Override
    public List<ArticleSummaryResponseDto> getMyArticles(Long mentorId) {

        return articleService.findArticlesByAuthor(mentorId);
    }

    @Override
    public UserCredentialsResponseDto getPersonalInfo(Long mentorId) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        return mentorMapper.toCredentialsDto(mentor);
    }

    @Override
    public List<StudentDetailsResponseDto> getMyStudents(Long mentorId) {
        return studentDirectoryService.findAllStudentsByMentor(mentorId);
    }

    @Override
    @Transactional
    public void terminateMentorshipWithStudent(Long mentorId, Long studentId) {
        Mentor mentor = userFinder.findMentorById(mentorId);
        Student student = userFinder.findStudentById(studentId);
        if (student.getMentor() == null || !student.getMentor().getId().equals(mentor.getId())) {
            throw new SecurityException("Студент не является вашим подопечным.");
        }
        mentorshipLifecycleService.terminateLink(mentor, student);
    }



}



