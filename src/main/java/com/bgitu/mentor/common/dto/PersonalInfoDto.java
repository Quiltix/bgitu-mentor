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

    private String firstName;
    private String lastName;

    public PersonalInfoDto(Mentor mentor) {
        this.email = mentor.getEmail();
        this.firstName = mentor.getFirstName();
        this.lastName = mentor.getLastName();
    }
    public PersonalInfoDto(Student student) {
        this.email = student.getEmail();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
    }
}
