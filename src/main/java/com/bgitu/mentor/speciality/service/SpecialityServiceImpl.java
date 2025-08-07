package com.bgitu.mentor.speciality.service;

import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.speciality.data.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialityServiceImpl implements SpecialityService {

    private final SpecialityRepository specialityRepository;

    @Override
    public Speciality getById(Long specialityId) {
        return specialityRepository.findById(specialityId)
                .orElseThrow(() -> new IllegalArgumentException("Специальность не найдена"));
    }
}
