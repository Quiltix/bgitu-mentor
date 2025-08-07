package com.bgitu.mentor.mentorship.data;



import com.bgitu.mentor.mentorship.data.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.student.data.StudentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface ApplicationMapper {



    @Mapping(source = "student",target = "student")
    ApplicationDetailsResponseDto toDetailsDto(Application application);
}
