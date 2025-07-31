package com.bgitu.mentor.student.service;


import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.data.dto.CardMentorDto;
import com.bgitu.mentor.student.dto.ApplicationStudentDto;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    PersonalInfoDto updateProfile(Long studentId, UpdatePersonalInfo dto);
    StudentCardDto updateCard(Long studentId, UpdateStudentCardDto dto, MultipartFile avatarFile);
    CardMentorDto getMentorOfStudent(Long studentId);
    List<ApplicationStudentDto> getStudentApplications(Long studentId);
    void terminateCurrentMentorship(Long studentId);

    StudentCardDto getPublicCardById(Long studentId);
    PersonalInfoDto getPersonalInfo(Long studentId);
}