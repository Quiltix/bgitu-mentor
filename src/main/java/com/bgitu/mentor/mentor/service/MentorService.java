package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.dto.MentorShortDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.dto.ApplicationStudentDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MentorService {

    Mentor getByAuth(Authentication authentication);
    Mentor updateProfile(Authentication authentication, UpdatePersonalInfo dto);
    Mentor updateCard(Authentication authentication, UpdateStudentCardDto dto, MultipartFile avatarFile);
    List<CardMentorDto> getTopMentors();
    List<MentorShortDto> getAllShort(Optional<Long> specialityId);
    CardMentorDto getById(Long id)

}
