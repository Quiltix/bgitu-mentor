package com.bgitu.mentor.mentor.dto;

import com.bgitu.mentor.mentor.model.Mentor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.bgitu.mentor.mentor.model.Mentor}
 */
@Getter
@Setter
public class CardMentorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String description;
    private String avatarUrl;
    private String vkUrl;
    private String telegramUrl;
    private String speciality;
    Integer rank;

    public CardMentorDto(Mentor mentor) {
        this.id = mentor.getId();
        this.firstName = mentor.getFirstName();
        this.lastName = mentor.getLastName();
        this.description = mentor.getDescription();
        this.avatarUrl = mentor.getAvatarUrl();
        this.vkUrl = mentor.getVkUrl();
        this.telegramUrl = mentor.getTelegramUrl();
        this.speciality = mentor.getSpeciality() != null ? mentor.getSpeciality().getName() : null;
        this.rank = mentor.getRank();
    }
}
