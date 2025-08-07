package com.bgitu.mentor.common.dto;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsResponseDto {

    @Email(message = "Email должен быть корректным")
    private String email;
}
