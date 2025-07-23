package com.bgitu.mentor.student.service;


import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.student.dto.ApplicationStudentDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    Student getByAuth(Authentication authentication);
    Student updateProfile(Authentication authentication, UpdatePersonalInfo dto);
    Student updateCard(Authentication authentication, UpdateStudentCardDto dto, MultipartFile avatarFile);
    CardMentorDto getMentorOfStudent(Authentication auth);
    List<ApplicationStudentDto> getStudentApplications(Authentication authentication);
}