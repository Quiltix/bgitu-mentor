package com.bgitu.mentor.mentorship.service;

public interface MentorshipLifecycleService {

  void establishLink(Long mentorId, Long studentId);

  void terminateLinkByMentor(Long mentorId, Long studentId);

  void terminateLinkByStudent(Long studentId);
}
