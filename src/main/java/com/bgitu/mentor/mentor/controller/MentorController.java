package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import com.bgitu.mentor.mentor.service.MentorDirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Mentor", description = "Методы для взаимодействия с карточками ментора")
@RequestMapping("/api/mentors")
public class MentorController {

    private final MentorDirectoryService mentorService;


    @Operation(summary = "Получение списка менторов (короткое описание) с фильтрацией и пагинацией", description = "Возвращает пагинированный список менторов. " +
            "Поддерживает фильтры: `specialityId`, `query`. " +
            "Поддерживает сортировку: `sort=rank,desc` (для топ-3). Первый параметр - поле по которому сортировать, второй - desc (по убыванию) или asc (по возрастанию)." +
            "Поддерживает пагинацию: `page=0`, `size=10`."
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<Page<MentorSummaryResponseDto>>  getAllMentorsShort(
            @RequestParam(required = false) Long specialityId,
            @RequestParam(required = false) String query,
            Pageable pageable) {
        return ResponseEntity.ok().body(mentorService.findMentors(specialityId, query, pageable));
    }

    @Operation(summary = "Получение полной карточки ментора", description = "Доступно для ролей STUDENT и MENTOR. Возвращает полную информацию по ментору по id.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<MentorDetailsResponseDto> getMentorDetails(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getMentorDetails(id));
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{id}/vote")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Лайк/дизлайк ментора", description = "Голосование за ментора (1 раз на студента)")
    public void voteMentor(
            @PathVariable Long id,
            @RequestParam boolean upvote,
            Authentication authentication
    ) {
        Long userId = SecurityUtils.getCurrentUserId(authentication);
        mentorService.voteForMentor(id, upvote, userId);
    }

}








