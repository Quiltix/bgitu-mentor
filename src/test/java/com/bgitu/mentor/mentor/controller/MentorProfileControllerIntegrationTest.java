package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.testsupport.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentorProfileControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired private MentorRepository mentorRepository;

  @BeforeEach
  void setUp() {
    mentorRepository.deleteAll();
  }

  private Mentor createMentor(
      String firstName,
      String lastName,
      String email,
      Speciality speciality,
      Integer rank,
      String description,
      String password) {
    Mentor mentor = new Mentor();
    mentor.setFirstName(firstName);
    mentor.setLastName(lastName);
    mentor.setEmail(email);
    mentor.setSpeciality(speciality);
    mentor.setRank(rank);
    mentor.setDescription(description);
    mentor.setPassword(password);
    return mentorRepository.save(mentor);
  }

  @Test
  @DisplayName("GET api/mentors/{id} | should return mentor profile and 200 OK")
  @WithMockUser(roles = "STUDENT")
  void getMentorProfile_shouldReturnMentorProfileAnd200OK() throws Exception {

    Mentor mentor = createMentor("Иван", "Иванов", null, null, 0, "Описание Ивана Иванова", null);

    ResultActions resultActions = mockMvc.perform(get("/api/mentors/{id}", mentor.getId()));

    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(mentor.getId()))
        .andExpect(jsonPath("$.firstName").value(mentor.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(mentor.getLastName()))
        .andExpect(jsonPath("$.speciality").doesNotExist())
        .andExpect(jsonPath("$.rank").value(mentor.getRank()))
        .andExpect(jsonPath("$.description").value(mentor.getDescription()));
  }
}
