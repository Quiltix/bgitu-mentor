package com.bgitu.mentor.student.service;

import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;

import java.util.List;

public interface StudentDirectoryService {

  List<StudentDetailsResponseDto> findAllStudentsByMentor(Long mentorId);
}
