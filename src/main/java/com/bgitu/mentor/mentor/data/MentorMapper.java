package com.bgitu.mentor.mentor.data;

import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MentorMapper {

  @Mapping(source = "speciality.name", target = "speciality")
  MentorDetailsResponseDto toDetailsDto(Mentor mentor);

  @Mapping(source = "description", target = "shortDescription", qualifiedByName = "trimDescription")
  @Mapping(source = "speciality.name", target = "specialityName")
  MentorSummaryResponseDto toSummaryDto(Mentor mentor);

  UserCredentialsResponseDto toCredentialsDto(Mentor mentor);

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
