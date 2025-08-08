package com.bgitu.mentor.student.data;

import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.data.model.Student;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

  StudentDetailsResponseDto toDetailsDto(Student student);

  UserCredentialsResponseDto toCredentialsDto(Student student);

  List<StudentDetailsResponseDto> toDetailDtoList(List<Student> students);
}
