package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.service.ArticleService;
import com.bgitu.mentor.mentor.data.MentorMapper;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.mentorship.service.MentorshipLifecycleService;
import com.bgitu.mentor.speciality.service.SpecialityService;
import com.bgitu.mentor.student.service.StudentDirectoryService;
import com.bgitu.mentor.user.service.BaseUserManagementService;
import com.bgitu.mentor.user.service.UserFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorProfileServiceImplTest {

    private static Mentor createMentor(Long id, String firstName) {
        Mentor mentor = new Mentor();
        mentor.setId(id);
        mentor.setFirstName(firstName);

        return mentor;
    }

    @Mock
    private MentorRepository mentorRepository;
    @Mock
    private MentorMapper mentorMapper;
    @Mock
    private ArticleService articleService;
    @Mock
    private StudentDirectoryService studentDirectoryService;
    @Mock
    private MentorshipLifecycleService mentorshipLifecycleService;
    @Mock
    private UserFinder userFinder;
    @Mock
    private BaseUserManagementService baseUserManagementService;
    @Mock
    private SpecialityService specialityService;

    @InjectMocks
    private MentorProfileServiceImpl mentorProfileService;



    @Test
    @DisplayName("Должен вернуть корректные статьи у ментора, если существуют")
    void getMyArticles_returnsArticlesDto(){

        Long mentorId = 1L;

        ArticleSummaryResponseDto summaryResponseDto1 = new ArticleSummaryResponseDto();
        ArticleSummaryResponseDto summaryResponseDto2 = new ArticleSummaryResponseDto();
        summaryResponseDto1.setId(0L);
        summaryResponseDto2.setId(1L);

        List<ArticleSummaryResponseDto> fakeArticles = List.of(summaryResponseDto1,summaryResponseDto2);

        when(articleService.findArticlesByAuthor(mentorId)).thenReturn(fakeArticles);


        List<ArticleSummaryResponseDto> responseDtos = mentorProfileService.getMyArticles(mentorId);


        assertNotNull(responseDtos);

        assertEquals(2,responseDtos.size());

        assertSame(fakeArticles,responseDtos);

        verify(articleService,times(1)).findArticlesByAuthor(mentorId);

    }


    @Test
    @DisplayName("Должен вернуть пустой список, если статей нет")
    void getMyArticles_returnsEmptyList(){

        Long mentorId = 1L;

        List<ArticleSummaryResponseDto> fakeArticles = List.of();

        when(articleService.findArticlesByAuthor(mentorId)).thenReturn(fakeArticles);


        List<ArticleSummaryResponseDto> responseDtos = mentorProfileService.getMyArticles(mentorId);

        assertNotNull(responseDtos);

        assertTrue(responseDtos.isEmpty());

        assertSame(fakeArticles,responseDtos);

        verify(articleService,times(1)).findArticlesByAuthor(mentorId);

        verifyNoInteractions(studentDirectoryService,mentorshipLifecycleService);

    }



}
