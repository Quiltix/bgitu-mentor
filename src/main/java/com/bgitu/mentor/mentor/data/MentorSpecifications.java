package com.bgitu.mentor.mentor.data;

import com.bgitu.mentor.mentor.data.model.Mentor;
import org.springframework.data.jpa.domain.Specification;

public final class MentorSpecifications {

  private MentorSpecifications() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static Specification<Mentor> hasSpeciality(Long specialityId) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.join("speciality").get("id"), specialityId);
  }

  public static Specification<Mentor> nameOrDescriptionContains(String text) {
    String likePattern = "%" + text.toLowerCase() + "%";
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern));
  }
}
