package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;

import com.bgitu.mentor.mentor.data.dto.MentorUpdateRequestDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MentorProfileService {

  UserCredentialsResponseDto updateProfile(Long mentorId, UserCredentialsUpdateRequestDto dto);

  MentorDetailsResponseDto updateCard(
      Long mentorId, MentorUpdateRequestDto dto, MultipartFile avatarFile);

  UserCredentialsResponseDto getPersonalInfo(Long mentorId);

  MentorDetailsResponseDto getMyCard(Long mentorId);

  List<ArticleSummaryResponseDto> getMyArticles(Long mentorId);

  List<StudentDetailsResponseDto> getMyStudents(Long mentorId);

  void terminateMentorshipWithStudent(Long mentorId, Long studentId);
}
