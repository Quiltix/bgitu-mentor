package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.testsupport.AbstractIntegrationTest;
import com.bgitu.mentor.user.data.dto.UserCredentialsUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MentorProfileControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired private MentorRepository mentorRepository;
  @Autowired private PasswordEncoder passwordEncoder;

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

  @Test
  @DisplayName("GET api/mentors/{id} | should return 404 Not Found for non-existing mentor")
  @WithMockUser(roles = "STUDENT")
  void getMentorProfile_shouldReturn404NotFoundForNonExistingMentor() throws Exception {
    Long nonExistingMentorId = 999L;

    ResultActions resultActions = mockMvc.perform(get("/api/mentors/{id}", nonExistingMentorId));

    resultActions
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Пользователь не найден"));
  }

  @Test
  @DisplayName("GET api/mentors/credentials | should return 200 and updated data")
  @WithMockUser(roles = "MENTOR")
  void updateMentorProfile_shouldReturn200AndUpdatedData() throws Exception {
    Mentor fakeMentor =
        createMentor("Ivan", "Ivanovich", "123@test.com", null, null, null, "12345678");
    mentorRepository.save(fakeMentor);

    UserCredentialsUpdateRequestDto fakeDto = new UserCredentialsUpdateRequestDto();
    fakeDto.setEmail("new@test.com");
    fakeDto.setPassword("newPassword");

    String fakeDtoString = objectMapper.writeValueAsString(fakeDto);

    mockMvc
        .perform(
            patch("api/mentors/credentials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fakeDtoString))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(fakeDto.getEmail()));

    Mentor updatedMentor =
        mentorRepository
            .findById(0L)
            .orElseThrow(() -> new AssertionError("Mentor should exist in database"));

    assertThat(updatedMentor.getEmail()).isEqualTo(fakeDto.getEmail());

    assertThat(passwordEncoder.matches(fakeDto.getPassword(), updatedMentor.getPassword()))
        .isTrue();
  }
}
