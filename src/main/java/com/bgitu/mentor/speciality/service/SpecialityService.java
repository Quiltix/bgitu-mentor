package com.bgitu.mentor.speciality.service;

import com.bgitu.mentor.mentor.data.dto.SpecialityDto;
import com.bgitu.mentor.speciality.data.model.Speciality;

import java.util.List;

public interface SpecialityService {

  Speciality getById(Long specialityId);

  List<SpecialityDto> getAllSpecialities();
}
