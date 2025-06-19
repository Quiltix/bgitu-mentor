package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Mentor> mentorOpt = mentorRepository.findByEmail(email);
        if (mentorOpt.isPresent()) {
            Mentor mentor = mentorOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    mentor.getEmail(),
                    mentor.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_MENTOR"))
            );
        }

        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            return new org.springframework.security.core.userdetails.User(
                    student.getEmail(),
                    student.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
