package com.bgitu.mentor.student.service;


import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;


public interface StudentProfileService {
    UserCredentialsResponseDto updateProfile(Long studentId, UserCredentialsUpdateRequestDto dto);
    StudentDetailsResponseDto updateCard(Long studentId, StudentDetailsUpdateRequestDto dto, MultipartFile avatarFile);
    void terminateCurrentMentorship(Long studentId);

    MentorDetailsResponseDto getMentorOfStudent(Long studentId);
    StudentDetailsResponseDto getPublicCardById(Long studentId);
    UserCredentialsResponseDto getPersonalInfo(Long studentId);

}