package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.mentor.dto.SpecialityDto;
import com.bgitu.mentor.mentor.service.SpecialityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specialities")
@RequiredArgsConstructor
public class SpecialityController {

    private final SpecialityService specialityService;

    @Operation(summary = "Получение полного списка специальностей", description = "Доступно для всех ролей")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping
    public ResponseEntity<List<SpecialityDto>> getAllSpecialities() {
        return ResponseEntity.ok(specialityService.getAllSpecialities());
    }
}
