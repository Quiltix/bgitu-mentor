package com.bgitu.mentor.user.repository;

import com.bgitu.mentor.user.model.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {


    Optional<BaseUser> findByEmail(String email);


    boolean existsByEmail(String email);
}