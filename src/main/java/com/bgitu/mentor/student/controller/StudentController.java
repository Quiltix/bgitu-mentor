package com.bgitu.mentor.student.controller;

import com.bgitu.mentor.student.service.StudentProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Student", description = "Методы для взаимодействия с карточками и профилем студента")
@RequestMapping("/api/students")
public class StudentController {

  private final StudentProfileService studentService;
}
