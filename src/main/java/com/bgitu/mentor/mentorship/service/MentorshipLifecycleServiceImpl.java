package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MentorshipLifecycleServiceImpl implements  MentorshipLifecycleService{

    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;


    @Override
    @Transactional
    public void establishLink(Mentor mentor, Student student) {
        // Здесь можно будет добавить сложную логику, например, проверку,
        // что у студента нет другого ментора, или что у ментора есть свободные слоты.
        student.setMentor(mentor);
        // Важно поддерживать обе стороны связи для консистентности кэша Hibernate
        mentor.getStudents().add(student);

        // Сохранять не обязательно, если @Transactional управляет сессией,
        // но явное сохранение делает код более понятным.
        studentRepository.save(student);
        mentorRepository.save(mentor);
    }

    @Override
    @Transactional
    public void terminateLink(Mentor mentor, Student student) {
        student.setMentor(null);
        mentor.getStudents().remove(student);

        studentRepository.save(student);
        mentorRepository.save(mentor);
    }
}
