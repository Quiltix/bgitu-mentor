package com.bgitu.mentor.mentorship.data;



import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.student.data.StudentMapper;
import com.bgitu.mentor.student.data.dto.ApplicationOfStudentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface ApplicationMapper {



    @Mapping(source = "student",target = "student")
    ApplicationDetailsResponseDto toDetailsDto(Application application);

    List<ApplicationDetailsResponseDto> toDetailsDtoList(List<Application> applications);

    @Mapping(source = "mentor", target = "mentorFullName", qualifiedByName = "mentorToFullName")
    @Mapping(source = "mentor.speciality.name", target = "mentorSpeciality")
    ApplicationOfStudentResponseDto toStudentApplicationDto(Application application);

    List<ApplicationOfStudentResponseDto> toStudentApplicationDtoList(List<Application> applications);
    @Named("mentorToFullName") // Даем имя нашему кастомному методу
    default String mentorToFullName(Mentor mentor) {
        if (mentor == null) {
            return null;
        }
        return mentor.getFirstName() + " " + mentor.getLastName();
    }
}
