package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;

public interface MentorshipLifecycleService {

  void establishLink(Mentor mentor, Student student);

  void terminateLinkByMentor(Long mentorId, Long studentId);

  void terminateLinkByStudent(Long studentId);
}
