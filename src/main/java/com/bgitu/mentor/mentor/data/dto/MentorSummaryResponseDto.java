package com.bgitu.mentor.mentor.data.dto;

import com.bgitu.mentor.mentor.data.model.Mentor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorSummaryResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String speciality;
    private Integer rank;
    private String shortDescription;

    public MentorSummaryResponseDto(Mentor mentor) {
        this.id = mentor.getId();
        this.firstName = mentor.getFirstName();
        this.lastName = mentor.getLastName();
        this.avatarUrl = mentor.getAvatarUrl();
        this.speciality = mentor.getSpeciality() != null ? mentor.getSpeciality().getName() : null;
        this.rank = mentor.getRank();
        this.shortDescription = trimDescription(mentor.getDescription());
    }

    private String trimDescription(String description) {
        if (description == null) return null;
        return description.length() > 100 ? description.substring(0, 97) + "..." : description;
    }
}
