package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.data.ArticleSpecifications;
import com.bgitu.mentor.article.data.dto.ArticleCreateDto;
import com.bgitu.mentor.article.data.dto.ArticleResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleShortDto;
import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.article.data.model.ArticleVote;
import com.bgitu.mentor.article.data.repository.ArticleRepository;
import com.bgitu.mentor.article.data.repository.ArticleVoteRepository;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.model.Speciality;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.mentor.data.repository.SpecialityRepository;
import com.bgitu.mentor.mentor.service.MentorService;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private static final String ARTICLE_NOT_FOUND_TEXT = "Статья не найдена";
    private final ArticleRepository articleRepository;
    private final MentorService mentorServiceImpl;
    private final SpecialityRepository specialityRepository;
    private final FileStorageService fileStorageService;
    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;
    private final ArticleVoteRepository articleVoteRepository;

    @Override
    public ArticleResponseDto createArticle(Authentication auth, ArticleCreateDto dto, MultipartFile image) {
        Mentor author = mentorServiceImpl.getByAuth(auth);
        Speciality speciality = specialityRepository.findById(dto.getSpecialityId())
                .orElseThrow(() -> new IllegalArgumentException("Специальность не найдена"));

        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setAuthor(author);
        article.setSpeciality(speciality);
        article.setRank(0);

        if (image != null && !image.isEmpty()) {
            String storedRelativePath = fileStorageService.store(image, "articles");
            String publicUrl = "/api/uploads/image/" + storedRelativePath.replace("\\", "/");
            article.setImageUrl(publicUrl);
        }

        Article saved = articleRepository.save(article);
        return new ArticleResponseDto(saved);
    }

    @Override
    public ArticleResponseDto getById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ARTICLE_NOT_FOUND_TEXT));
        return new ArticleResponseDto(article);
    }

    @Override
    public void deleteArticle(Long articleId, Authentication authentication) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND_TEXT));

        Mentor currentMentor = mentorServiceImpl.getByAuth(authentication);

        if (!article.getAuthor().getId().equals(currentMentor.getId())) {
            throw new AccessDeniedException("Вы можете удалять только свои статьи");
        }

        articleVoteRepository.deleteAllByArticleId(articleId); // <== удаление голосов
        articleRepository.delete(article);
    }

    @Override
    public void changeArticleRank(Long articleId, boolean like, Authentication auth) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ARTICLE_NOT_FOUND_TEXT));

        Optional<Mentor> mentorOpt = mentorRepository.findByEmail(auth.getName());
        Optional<Student> studentOpt = studentRepository.findByEmail(auth.getName());

        if (mentorOpt.isPresent()) {
            Mentor mentor = mentorOpt.get();
            if (articleVoteRepository.existsByArticleIdAndMentorId(articleId, mentor.getId())) {
                throw new IllegalStateException("Вы уже голосовали за эту статью");
            }

            ArticleVote vote = new ArticleVote();
            vote.setArticle(article);
            vote.setMentor(mentor);
            vote.setUpvote(like);
            articleVoteRepository.save(vote);

        } else if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (articleVoteRepository.existsByArticleIdAndStudentId(articleId, student.getId())) {
                throw new IllegalStateException("Вы уже голосовали за эту статью");
            }

            ArticleVote vote = new ArticleVote();
            vote.setArticle(article);
            vote.setStudent(student);
            vote.setUpvote(like);
            articleVoteRepository.save(vote);

        } else {
            throw new AccessDeniedException("Пользователь не найден");
        }

        article.setRank(article.getRank() + (like ? 1 : -1));
        articleRepository.save(article);
    }

    @Override
    public Page<ArticleShortDto> findArticles(Long specialityId, String query, Pageable pageable){

        Specification<Article> specification = Specification.not(null);

        if (specialityId != null){
            specification.and(ArticleSpecifications.hasSpeciality(specialityId));
        }

        if (query != null && !query.isBlank()) {
            if (query.length() > 250) {
                throw new IllegalStateException("Сократите строку поиска до 250 символов");
            }
            specification.and(ArticleSpecifications.titleOrContentContains(query));
        }
        Page<Article> articlePages = articleRepository.findAll(specification, pageable);

        return articlePages.map(ArticleShortDto::new);
    }

}

