package com.bgitu.mentor.user.service;

import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import com.bgitu.mentor.user.model.BaseUser;
import com.bgitu.mentor.user.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFinderImpl implements UserFinder {

    private final BaseUserRepository baseUserRepository;
    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;

    @Override
    public BaseUser findUserById(Long userId) {
        return baseUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    @Override
    public Mentor findMentorById(Long mentorId) {
        return mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Ментор с id=" + mentorId + " не найден"));
    }

    @Override
    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id=" + studentId + " не найден"));
    }
}