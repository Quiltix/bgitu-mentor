package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.dto.ArticleCreateDto;
import com.bgitu.mentor.article.dto.ArticleResponseDto;
import com.bgitu.mentor.article.dto.ArticleShortDto;
import com.bgitu.mentor.article.model.Article;
import com.bgitu.mentor.article.model.ArticleVote;
import com.bgitu.mentor.article.repository.ArticleRepository;
import com.bgitu.mentor.article.repository.ArticleVoteRepository;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.model.Speciality;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import com.bgitu.mentor.mentor.repository.SpecialityRepository;
import com.bgitu.mentor.mentor.service.MentorService;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MentorService mentorService;
    private final SpecialityRepository specialityRepository;
    private final FileStorageService fileStorageService;
    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;
    private final ArticleVoteRepository articleVoteRepository;

    public ArticleResponseDto createArticle(Authentication auth, ArticleCreateDto dto, MultipartFile image) {
        Mentor author = mentorService.getMentorByAuth(auth);
        Speciality speciality = specialityRepository.findById(dto.getSpecialityId())
                .orElseThrow(() -> new IllegalArgumentException("Специальность не найдена"));

        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setAuthor(author);
        article.setSpeciality(speciality);
        article.setRank(0); // начальный ранг

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeAvatar(image, "article_" + System.currentTimeMillis());
            article.setImageUrl(imageUrl);
        }

        Article saved = articleRepository.save(article);
        return new ArticleResponseDto(saved);
    }

    public ArticleResponseDto getById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Статья не найдена"));
        return new ArticleResponseDto(article);
    }

    @Transactional
    public List<ArticleShortDto> getAllArticles(Optional<Long> specialityId) {
        List<Article> articles = specialityId
                .map(articleRepository::findBySpecialityIdOrderByRankDesc)
                .orElseGet(articleRepository::findAllByOrderByRankDesc);

        return articles.stream()
                .map(ArticleShortDto::new)
                .collect(Collectors.toList());
    }

    public void deleteArticle(Long articleId, Authentication authentication) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Статья не найдена"));

        Mentor currentMentor = mentorService.getMentorByAuth(authentication);

        if (!article.getAuthor().getId().equals(currentMentor.getId())) {
            throw new AccessDeniedException("Вы можете удалять только свои статьи");
        }

        articleRepository.delete(article);
    }

    public void changeArticleRank(Long articleId, boolean like, Authentication auth) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Статья не найдена"));

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
    @Transactional
    public List<ArticleShortDto> getTop3Articles() {
        List<Article> topArticles = articleRepository.findTop3ByOrderByRankDesc();
        return topArticles.stream()
                .map(ArticleShortDto::new)
                .collect(Collectors.toList());
    }

    public List<ArticleShortDto> searchArticles(String query) {
        if (query.length()>250){
            throw new IllegalStateException("Сократите строку поиска до 250 символов");
        }
        return articleRepository.searchByTitleOrContent(query)
                .stream()
                .map(ArticleShortDto::new)
                .collect(Collectors.toList());
    }

    public ArticleResponseDto getById(Long id, Authentication auth) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Статья не найдена"));

        boolean canVote = true;

        if (auth != null) {
            String email = auth.getName();

            if (studentRepository.existsByEmail(email)) {
                Student student = studentRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("Студент не найден"));

                canVote = articleVoteRepository.findByArticleAndStudent(article, student).isEmpty();
            } else if (mentorRepository.existsByEmail(email)) {
                Mentor mentor = mentorRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("Ментор не найден"));

                canVote = articleVoteRepository.findByArticleAndMentor(article, mentor).isEmpty();
            }
        }

        return new ArticleResponseDto(article, canVote);
    }





}

