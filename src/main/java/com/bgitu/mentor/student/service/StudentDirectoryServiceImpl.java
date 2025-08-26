package com.bgitu.mentor.student.service;

import com.bgitu.mentor.student.data.StudentMapper;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.data.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentDirectoryServiceImpl implements StudentDirectoryService {

  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  @Override
  @Transactional(readOnly = true)
  public List<StudentDetailsResponseDto> findAllStudentsByMentor(Long mentorId) {
    return studentMapper.toDetailDtoList(studentRepository.findAllByMentorId(mentorId));
  }
}
