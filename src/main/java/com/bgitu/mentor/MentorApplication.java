package com.bgitu.mentor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MentorApplication {

  public static void main(String[] args) {
    SpringApplication.run(MentorApplication.class, args);
  }
}
