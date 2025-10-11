package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.data.repository.ArticleRepository;
import com.bgitu.mentor.auth.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service(value = "articleSecurity")
@RequiredArgsConstructor
public class ArticleSecurityService {

  private final ArticleRepository articleRepository;

  public boolean isAuthor(Authentication authentication, Long articleId) {
    if (!(authentication.getPrincipal()
        instanceof com.bgitu.mentor.auth.security.AuthenticatedUser)) {
      return false;
    }
    Long userId = SecurityUtils.getCurrentUserId(authentication);
    return articleRepository.existsByIdAndAuthorId(articleId, userId);
  }
}
