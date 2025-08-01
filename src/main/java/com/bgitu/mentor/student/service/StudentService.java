package com.bgitu.mentor.student.service;


import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.student.dto.ApplicationOfStudentResponseDto;
import com.bgitu.mentor.student.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.dto.StudentDetailsUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    UserCredentialsResponseDto updateProfile(Long studentId, UserCredentialsUpdateRequestDto dto);
    StudentDetailsResponseDto updateCard(Long studentId, StudentDetailsUpdateRequestDto dto, MultipartFile avatarFile);
    MentorDetailsResponseDto getMentorOfStudent(Long studentId);
    List<ApplicationOfStudentResponseDto> getStudentApplications(Long studentId);
    void terminateCurrentMentorship(Long studentId);

    StudentDetailsResponseDto getPublicCardById(Long studentId);
    UserCredentialsResponseDto getPersonalInfo(Long studentId);
}