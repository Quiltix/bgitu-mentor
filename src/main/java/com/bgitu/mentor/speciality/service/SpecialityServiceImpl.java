package com.bgitu.mentor.speciality.service;

import com.bgitu.mentor.speciality.data.dto.SpecialityDto;
import com.bgitu.mentor.speciality.data.SpecialityMapper;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.speciality.data.repository.SpecialityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialityServiceImpl implements SpecialityService {

  private final SpecialityRepository specialityRepository;
  private final SpecialityMapper specialityMapper;

  @Override
  public Speciality getById(Long specialityId) {
    return specialityRepository
        .findById(specialityId)
        .orElseThrow(() -> new EntityNotFoundException("Специальность не найдена"));
  }

  public List<SpecialityDto> getAllSpecialities() {
    return specialityMapper.toDtoList(specialityRepository.findAll());
  }
}
