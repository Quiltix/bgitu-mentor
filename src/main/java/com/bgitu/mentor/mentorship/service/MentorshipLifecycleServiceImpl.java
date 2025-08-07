package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.data.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MentorshipLifecycleServiceImpl implements  MentorshipLifecycleService{

    private final StudentRepository studentRepository;


    @Override
    @Transactional
    public void establishLink(Mentor mentor, Student student) {

        student.setMentor(mentor);

        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void terminateLink(Mentor mentor, Student student) {

        student.setMentor(null);

        studentRepository.save(student);
    }
}
