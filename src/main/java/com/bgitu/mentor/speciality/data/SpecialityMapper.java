package com.bgitu.mentor.speciality.data;

import com.bgitu.mentor.speciality.data.dto.SpecialityDto;
import com.bgitu.mentor.speciality.data.model.Speciality;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {

  SpecialityDto toDto(Speciality speciality);

  List<SpecialityDto> toDtoList(List<Speciality> specialities);
}
