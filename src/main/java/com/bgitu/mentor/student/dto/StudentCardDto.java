package com.bgitu.mentor.student.dto;


import com.bgitu.mentor.student.model.Student;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class StudentCardDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String description;

    private String avatarUrl;

    private String vkUrl;
    private String telegramUrl;

    public StudentCardDto(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.description = student.getDescription();
        this.avatarUrl = student.getAvatarUrl();
        this.vkUrl = student.getVkUrl();
        this.telegramUrl = student.getTelegramUrl();
    }
}
