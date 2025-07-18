package com.bgitu.mentor.common.dto;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.model.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalInfoDto {

    @Email(message = "Email должен быть корректным")
    private String email;



    public PersonalInfoDto(Mentor mentor) {
        this.email = mentor.getEmail();

    }
    public PersonalInfoDto(Student student) {
        this.email = student.getEmail();

    }
}
