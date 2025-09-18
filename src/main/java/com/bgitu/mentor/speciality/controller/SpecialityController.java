package com.bgitu.mentor.speciality.controller;

import com.bgitu.mentor.speciality.data.dto.SpecialityDto;
import com.bgitu.mentor.speciality.service.SpecialityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "specialities", description = "Методы для взаимодействия со специальностями")
@RestController
@RequestMapping("/api/specialities")
@RequiredArgsConstructor
public class SpecialityController {

  private final SpecialityService specialityService;

  @Operation(
      summary = "Получение полного списка специальностей",
      description = "Доступно для всех ролей")
  @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
  @GetMapping
  public List<SpecialityDto> getAllSpecialities() {
    return specialityService.getAllSpecialities();
  }
}
