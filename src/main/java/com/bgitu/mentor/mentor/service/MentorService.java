package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorUpdateRequestDto;
import com.bgitu.mentor.student.dto.StudentDetailsResponseDto;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MentorService {

    //Методы для профиля
    PersonalInfoDto updateProfile(Long mentorId, UpdatePersonalInfo dto);
    MentorDetailsResponseDto updateCard(Long mentorId, MentorUpdateRequestDto dto, MultipartFile avatarFile);
    void terminateMentorshipWithStudent(Long mentorId, Long studentId);
    List<ArticleSummaryResponseDto> getMentorArticles(Long mentorId);
    PersonalInfoDto getPersonalInfo(Long mentorId);

    //Для взаимодействия
    void voteMentor(Long mentorId, boolean upvote, Long userId);
    List<StudentDetailsResponseDto> getAllStudentsForMentor(Long mentorId);

    //Для публичного каталога
    MentorDetailsResponseDto getPublicCardById(Long id);
    Page<MentorSummaryResponseDto> findMentors(Long specialityId, String query, Pageable pageable);

}
