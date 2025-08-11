package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.data.repository.StudentRepository;
import com.bgitu.mentor.user.service.UserFinder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MentorshipLifecycleServiceImpl implements MentorshipLifecycleService {

  private final StudentRepository studentRepository;
  private final UserFinder userFinder;

  @Override
  @Transactional
  public void establishLink(Mentor mentor, Student student) {

    student.setMentor(mentor);

    studentRepository.save(student);
  }

  private void breakLinkInternal(Student student) {

    student.setMentor(null);

    studentRepository.save(student);
  }

  @Override
  public void terminateLinkByMentor(Long mentorId, Long studentId) {
    Mentor mentor = userFinder.findMentorById(mentorId);
    Student student = userFinder.findStudentById(studentId);

    if (student.getMentor() == null || !student.getMentor().getId().equals(mentor.getId())) {
      throw new SecurityException("Студент не является вашим подопечным.");
    }

    breakLinkInternal(student);
  }

  @Override
  public void terminateLinkByStudent(Long studentId) {
    Student student = userFinder.findStudentById(studentId);

    if (student.getMentor() == null) {
      throw new ResourceNotFoundException("У вас нет активного ментора, чтобы от него отказаться.");
    }

    breakLinkInternal(student);
  }
}
