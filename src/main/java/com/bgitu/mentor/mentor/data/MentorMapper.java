package com.bgitu.mentor.mentor.data;

import com.bgitu.mentor.user.data.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MentorMapper {

  @Mapping(source = "mentor.speciality.name", target = "specialityName")
  @Mapping(source = "voteResult", target = "voteResult")
  MentorDetailsResponseDto toDetailsDto(Mentor mentor, Integer voteResult);

  @Mapping(source = "description", target = "shortDescription", qualifiedByName = "trimDescription")
  @Mapping(source = "speciality.name", target = "specialityName")
  MentorSummaryResponseDto toSummaryDto(Mentor mentor);

  List<MentorSummaryResponseDto> toSummaryDtoList(List<Mentor> mentors);

  UserCredentialsResponseDto toCredentialsDto(Mentor mentor);

  default MentorDetailsResponseDto toDetailsDto(Mentor mentor) {
    return toDetailsDto(mentor, null);
  }

  @Named("trimDescription")
  default String trimDescription(String description) {
    if (description == null) {
      return null;
    }
    int maxLength = 100;
    if (description.length() <= maxLength) {
      return description;
    }
    // Обрезаем до 97 символов, чтобы с "..." было ровно 100
    return description.substring(0, maxLength - 3) + "...";
  }
}
