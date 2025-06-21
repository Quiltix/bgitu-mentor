package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.mentor.dto.SpecialityDto;
import com.bgitu.mentor.mentor.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialityService {

    private final SpecialityRepository specialityRepository;

    public List<SpecialityDto> getAllSpecialities() {
        return specialityRepository.findAll()
                .stream()
                .map(s -> new SpecialityDto(s.getId(), s.getName()))
                .toList();
    }
}
