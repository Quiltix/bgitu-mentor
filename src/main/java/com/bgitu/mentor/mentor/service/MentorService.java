package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.article.data.dto.ArticleShortDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.data.dto.CardMentorDto;
import com.bgitu.mentor.mentor.data.dto.MentorShortDto;
import com.bgitu.mentor.mentor.data.dto.UpdateMentorCardDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.dto.StudentCardDto;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MentorService {

    //Методы для профиля
    Mentor updateProfile(Long mentorId, UpdatePersonalInfo dto);
    CardMentorDto updateCard(Long mentorId, UpdateMentorCardDto dto, MultipartFile avatarFile);
    void terminateMentorshipWithStudent(Long mentorId, Long studentId);
    List<ArticleShortDto> getMentorArticles(Long mentorId);

    //Для взаимодействия
    void voteMentor(Long mentorId, boolean upvote, Long userId);
    List<StudentCardDto> getAllStudentsForMentor(Long mentorId);

    //Для публичного каталога
    CardMentorDto getPublicCardById(Long id);
    Page<MentorShortDto> findMentors(Long specialityId, String query, Pageable pageable);

}
