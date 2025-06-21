package com.bgitu.mentor.mentor.service;

import lombok.RequiredArgsConstructor;

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
