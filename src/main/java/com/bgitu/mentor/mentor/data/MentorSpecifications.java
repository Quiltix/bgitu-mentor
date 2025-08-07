
package com.bgitu.mentor.mentor.data;


import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.speciality.data.model.Speciality;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

public final class MentorSpecifications {

    private MentorSpecifications() {
        throw new IllegalStateException("Utility class");
    }
    // Спецификация для фильтрации по специальности
    public static Specification<Mentor> hasSpeciality(Long specialityId) {
        return (root, query, criteriaBuilder) -> {
            Join<Mentor, Speciality> specialityJoin = root.join("speciality");
            return criteriaBuilder.equal(specialityJoin.get("id"), specialityId);
        };
    }

    public static Specification<Mentor> nameOrDescriptionContains(String text) {
        String likePattern = "%" + text.toLowerCase() + "%";
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
                );
    }
}