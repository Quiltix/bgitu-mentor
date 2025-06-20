package com.bgitu.mentor.mentor.dto;

import com.bgitu.mentor.mentor.model.Mentor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

/**
 * DTO for {@link com.bgitu.mentor.mentor.model.Mentor}
 */
@Getter
@Setter
public class CardMentorDto {
    Long id;
    String firstName;
    String lastName;
    String description;
    String avatarUrl;
    String speciality;
    String vkUrl;
    String telegramUrl;
    Integer rank;

    public CardMentorDto(Mentor mentor) {
        this.id = mentor.getId();
        this.firstName = mentor.getFirstName();
        this.lastName = mentor.getLastName();
        this.description = mentor.getDescription();
        this.avatarUrl = mentor.getAvatarUrl();
        this.speciality = mentor.getSpeciality();
        this.vkUrl = mentor.getVkUrl();
        this.telegramUrl = mentor.getTelegramUrl();
        this.rank = mentor.getRank();
    }
}