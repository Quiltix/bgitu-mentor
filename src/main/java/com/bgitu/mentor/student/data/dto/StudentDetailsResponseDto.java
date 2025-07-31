package com.bgitu.mentor.student.data.dto;


import com.bgitu.mentor.student.data.model.Student;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class StudentDetailsResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String description;

    private String avatarUrl;

    private String vkUrl;
    private String telegramUrl;

    public StudentDetailsResponseDto(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.description = student.getDescription();
        this.avatarUrl = student.getAvatarUrl();
        this.vkUrl = student.getVkUrl();
        this.telegramUrl = student.getTelegramUrl();
    }
}
