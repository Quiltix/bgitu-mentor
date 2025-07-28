package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.common.dto.MessageDto;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.dto.MentorShortDto;
import com.bgitu.mentor.mentor.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Mentor", description = "Методы для взаимодействия с карточками ментора")
@RequestMapping("/api/mentors")
public class MentorController {

    private final MentorService mentorService;


    @Operation(summary = "Получение топ-3 менторов", description = "Доступно для ролей STUDENT и MENTOR. Возвращает менторов с наивысшим рангом.")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/popular")
    public ResponseEntity<List<CardMentorDto>> getTopMentors() {
        return ResponseEntity.ok(mentorService.getTopMentors());
    }

    @Operation(summary = "Получение списка менторов (короткое описание)", description = "Доступно для ролей STUDENT и MENTOR. Поддерживает фильтрацию по id специальности.")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping()
    public ResponseEntity<List<MentorShortDto>>  getAllMentorsShort(@RequestParam(required = false) Long specialityId) {
        return  ResponseEntity.ok(mentorService.getAllShort(Optional.ofNullable(specialityId)));
    }

    @Operation(summary = "Получение полной карточки ментора", description = "Доступно для ролей STUDENT и MENTOR. Возвращает полную информацию по ментору по id.")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<CardMentorDto> getMentorDetails(@PathVariable Long id) {
        return ResponseEntity.ok( mentorService.getById(id));
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{id}/vote")
    @Operation(summary = "Лайк/дизлайк ментора", description = "Голосование за ментора (1 раз на студента)")
    public ResponseEntity<MessageDto> voteMentor(
            @PathVariable Long id,
            @RequestParam boolean upvote,
            Authentication authentication
    ) {
        mentorService.voteMentor(id, upvote, authentication);
        return ResponseEntity.ok(new MessageDto("Голос учтен"));
    }


    @Operation(summary = "Поиск во вкладке менторов по описанию и фио", description = "Доступно для роли MENTOR")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/search")
    public ResponseEntity<List<MentorShortDto>> searchMentors(@RequestParam  String query) {
        return ResponseEntity.ok(mentorService.searchMentors(query));
    }
}








