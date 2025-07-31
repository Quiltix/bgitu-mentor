package com.bgitu.mentor.user.service;


import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.user.model.BaseUser;

public interface UserFinder {

    BaseUser findUserById(Long userId);

    Mentor findMentorById(Long mentorId);

    Student findStudentById(Long studentId);
}