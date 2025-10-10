package com.bgitu.mentor.mentorship.data.repository;

import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

  List<Application> findAllByMentorIdAndStatus(Long mentorId, ApplicationStatus status);

  List<Application> findAllByMentorId(Long mentorId);

  List<Application> findAllByStudentIdAndStatus(Long studentId, ApplicationStatus status);

  List<Application> findAllByStudentId(Long studentId);

  boolean existsByStudentIdAndMentorIdAndStatus(
      Long studentId, Long mentorId, ApplicationStatus status);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      """
         UPDATE Application a SET a.status = 'EXPIRED'
         WHERE a.student.id = :studentId AND a.id <> :excludedApplicationId AND a.status = 'PENDING'
         """)
  void updateOtherPendingApplicationsToExpired(Long studentId, Long excludedApplicationId);
}
