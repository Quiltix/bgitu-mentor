package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.article.dto.ArticleShortDto;
import com.bgitu.mentor.common.dto.MessageDto;
import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.dto.MentorShortDto;
import com.bgitu.mentor.mentor.dto.UpdateMentorCardDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Mentor", description = "Методы для взаимодействия с карточками и профилем ментора")

@RequestMapping("/api/mentor")
public class MentorController {

    private final MentorService mentorService;


    @Operation(summary = "Получение карточки ментора", description = "Доступно только для роли MENTOR. Возвращает полную информацию по текущему пользователю.")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/summary")
    public ResponseEntity<CardMentorDto> getCardMentor(Authentication authentication) {
        return ResponseEntity.ok(new CardMentorDto(mentorService.getMentorByAuth(authentication)));
    }

    @Operation(summary = "Обновление профиля ментора", description = "Доступно только для роли MENTOR. Обновляет имя, фамилию, пароль и email.")
    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping("/profile")
    public ResponseEntity<PersonalInfoDto> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UpdatePersonalInfo dto
    ) {

        Mentor updated = mentorService.updateMentorProfile(authentication, dto);
        return ResponseEntity.ok(new PersonalInfoDto(updated));

    }

    @Operation(summary = "Обновление карточки ментора", description = "Доступно только для роли MENTOR. Позволяет редактировать описание, направление и аватар.")
    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping(value = "/summary", consumes = "multipart/form-data")
    public ResponseEntity<CardMentorDto> updateMentorCard(
            Authentication authentication,
            @RequestPart("card") UpdateMentorCardDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {

        Mentor updated = mentorService.updateMentorCard(authentication, dto, avatarFile);
        return ResponseEntity.ok(new CardMentorDto(updated));
    }

    @Operation(summary = "Получение топ-3 менторов", description = "Доступно для ролей STUDENT и MENTOR. Возвращает менторов с наивысшим рангом.")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/popular")
    public ResponseEntity<List<CardMentorDto>> getTopMentors() {
        return ResponseEntity.ok(mentorService.getTopMentors());
    }

    @Operation(summary = "Получение списка менторов (короткое описание)", description = "Доступно для ролей STUDENT и MENTOR. Поддерживает фильтрацию по id специальности.")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/all")
    public ResponseEntity<List<MentorShortDto>>  getAllMentorsShort(@RequestParam(required = false) Long specialityId) {
        return  ResponseEntity.ok(mentorService.getAllShort(Optional.ofNullable(specialityId)));
    }

    @Operation(summary = "Получение полной карточки ментора", description = "Доступно для ролей STUDENT и MENTOR. Возвращает полную информацию по ментору по id.")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<CardMentorDto> getMentorDetails(@PathVariable Long id) {
        return ResponseEntity.ok( mentorService.getById(id));
    }

    @Operation(summary = "Получение моего профиля", description = "MENTOR. Возвращает профиль по авторизации")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/profile")
    public ResponseEntity<PersonalInfoDto> getMentorProfile(Authentication authentication) {
        return ResponseEntity.ok(new PersonalInfoDto( mentorService.getMentorByAuth(authentication)));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/mentors/{id}/vote")
    @Operation(summary = "Лайк/дизлайк ментора", description = "Голосование за ментора (1 раз на студента)")
    public ResponseEntity<MessageDto> voteMentor(
            @PathVariable Long id,
            @RequestParam boolean upvote,
            Authentication authentication
    ) {
        mentorService.voteMentor(id, upvote, authentication);
        return ResponseEntity.ok(new MessageDto("Голос учтен"));
    }


    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/search")
    public ResponseEntity<List<MentorShortDto>> searchMentors(@RequestParam  String query) {
        return ResponseEntity.ok(mentorService.searchMentors(query));
    }

    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/articles")
    public List<ArticleShortDto> getArticlesByMentor(Authentication authentication) {
        return mentorService.getMentorArticles(authentication);
    }
}








