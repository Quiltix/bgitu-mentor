// src/main/java/com/bgitu/mentor/config/DataSeeder.java
package com.bgitu.mentor.config;

import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.article.data.repository.ArticleRepository;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.speciality.data.repository.SpecialityRepository;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.data.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder {

  private final PasswordEncoder passwordEncoder;

  @Bean
  @Transactional
  CommandLineRunner initDatabase(
      MentorRepository mentorRepository,
      StudentRepository studentRepository,
      SpecialityRepository specialityRepository,
      ArticleRepository articleRepository) {
    return args -> {
      log.info("--- Запуск seeder для заполнения базы данных (профиль 'dev') ---");

      Speciality javaSpec =
          specialityRepository
              .findByName("Java Development")
              .orElseThrow(
                  () -> new RuntimeException("Специальность 'Java Development' не найдена"));
      Speciality dataScienceSpec =
          specialityRepository
              .findByName("Data Science")
              .orElseThrow(() -> new RuntimeException("Специальность 'Data Science' не найдена"));
      Speciality frontendSpec =
          specialityRepository
              .findByName("Frontend Development")
              .orElseThrow(
                  () -> new RuntimeException("Специальность 'Frontend Development' не найдена"));

      // 2. СОЗДАЕМ МЕНТОРОВ (с проверкой на существование)

      Mentor mentorJava =
          createMentorIfNotFound(
              mentorRepository,
              "m1@ya.ru",
              "Иван",
              "Петров",
              "Опытный Java-разработчик, специализируюсь на Spring и микросервисах. Помогу освоить сложные концепции и подготовиться к собеседованиям.",
              15,
              "https://vk.com/ivanpetrov",
              "https://t.me/ivanpetrov",
              javaSpec);

      Mentor mentorPython =
          createMentorIfNotFound(
              mentorRepository,
              "m2@ya.ru",
              "Анна",
              "Сидорова",
              "Data Scientist с 5-летним опытом. Работаю с Python, Pandas, Scikit-learn и нейронными сетями. Объясняю сложные вещи простым языком.",
              10,
              "https://vk.com/annasidorova",
              "https://t.me/annasidorova",
              dataScienceSpec);

      Mentor mentorFrontend =
          createMentorIfNotFound(
              mentorRepository,
              "m3@ya.ru",
              "Сергей",
              "Волков",
              "Frontend-разработчик, фанат React и TypeScript. Создаю красивые и быстрые интерфейсы. Помогу разобраться в современном фронтенде.",
              8,
              "https://vk.com/sergeyvolkov",
              "https://t.me/sergeyvolkov",
              frontendSpec);

      // 3. СОЗДАЕМ СТУДЕНТОВ

      Student student1 =
          createStudentIfNotFound(
              studentRepository,
              "s1@ya.ru",
              "Олег",
              "Кузнецов",
              "Начинающий backend-разработчик, изучаю Java и Spring. Ищу ментора для помощи с первым проектом.",
              "https://vk.com/olegkuznetsov",
              "https://t.me/olegkuznetsov");

      Student student2 =
          createStudentIfNotFound(
              studentRepository,
              "s2@ya.ru",
              "Мария",
              "Васильева",
              "Хочу стать Data Scientist. Уже знаю основы Python, но нужна помощь в применении на практике.",
              "https://vk.com/mariavasileva",
              "https://t.me/mariavasileva");

      createStudentIfNotFound(
          studentRepository,
          "sf@ya.ru",
          "Петр",
          "Николаев",
          "Студент, ищу своего первого ментора.",
          "https://vk.com/petrnikolaev",
          "https://t.me/petrnikolaev");

      // 4. СОЗДАЕМ СВЯЗИ
      // (проверяем, чтобы не привязать, если уже привязан)
      if (student1.getMentor() == null) {
        student1.setMentor(mentorJava);
        studentRepository.save(student1);
        log.info("Студент {} привязан к ментору {}", student1.getEmail(), mentorJava.getEmail());
      }

      if (student2.getMentor() == null) {
        student2.setMentor(mentorPython);
        studentRepository.save(student2);
        log.info("Студент {} привязан к ментору {}", student2.getEmail(), mentorPython.getEmail());
      }

      // 5. СОЗДАЕМ СТАТЬИ

      createArticleIfNotFound(
          articleRepository,
          "Введение в Spring Boot 3",
"""
 Lorem ipsum dolor sit amet, consectetur adipiscing elit. In imperdiet erat sed arcu bibendum venenatis. Nulla facilisi. Curabitur eget congue lorem, pretium lacinia urna. Ut feugiat augue sed leo volutpat scelerisque. Quisque nec purus a magna dapibus ultricies. Morbi scelerisque placerat nisi, id pharetra nisl finibus a. Curabitur hendrerit libero massa, eget efficitur ipsum scelerisque nec. Nunc sem ante, posuere sed eleifend in, tempor nec sapien. Suspendisse tristique lorem sed tempus viverra. Mauris congue id tellus sit amet faucibus. Proin a dignissim elit, vel vestibulum mi. Ut vel placerat dui, ut pharetra elit. Quisque pulvinar laoreet quam, ac pulvinar felis gravida et. Mauris varius et mauris vel maximus.

Proin tempor pharetra maximus. Sed venenatis, nulla id laoreet maximus, ligula risus viverra est, eget pulvinar dui libero id purus. Ut sagittis arcu nibh, et feugiat lectus lobortis scelerisque. Pellentesque pharetra ultricies lacinia. Nullam vitae ullamcorper mauris. Aenean libero dolor, mattis id suscipit sit amet, maximus eget lacus. Vestibulum dapibus velit ante, vitae porta tellus blandit quis. Donec finibus tincidunt ornare. Morbi scelerisque tellus nulla, id mollis turpis sollicitudin non. Aliquam viverra laoreet maximus. Integer vel volutpat mi, quis dapibus lorem.

Duis eu elit sed ante faucibus ultrices. Fusce maximus orci lacus. Integer eleifend rhoncus arcu, eu dictum nunc ultrices non. Aliquam tristique lacus a nunc pharetra vulputate. Sed egestas ultrices sem in tincidunt. Suspendisse mattis pretium lorem non pharetra. Pellentesque finibus, sapien et ultrices laoreet, nisi velit lobortis arcu, quis suscipit metus nulla vel dui. Vivamus lobortis tortor arcu, vel faucibus sapien pharetra ut. Maecenas in tortor faucibus, aliquet lectus commodo, pharetra magna. Cras tempus finibus tempor. Sed tellus erat, sodales at mi egestas, gravida mollis nisl. Curabitur vel elit sed elit molestie luctus vitae nec nunc.

Suspendisse gravida suscipit auctor. Maecenas a lectus eu enim tincidunt feugiat. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Duis a sapien consectetur, mollis elit sit amet, efficitur ipsum. Etiam euismod sollicitudin mi eu interdum. Vestibulum et mauris vitae libero vehicula lobortis. Maecenas fermentum est sed arcu volutpat interdum. Aenean ante erat, ullamcorper nec fringilla nec, feugiat sit amet erat. Donec porttitor sit amet sapien nec euismod. Vestibulum fringilla iaculis sodales. Suspendisse potenti. Integer felis risus, cursus vitae mauris ac, pellentesque commodo lacus. Donec nec consectetur ligula. Donec consectetur auctor vehicula.

Fusce et pellentesque libero. Duis laoreet elementum leo a mollis. Nullam varius magna at tellus vestibulum, non commodo massa consequat. Integer mattis volutpat libero in aliquam. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer vitae semper tellus. Donec et magna rutrum, tristique dolor et, tristique odio. Maecenas sit amet nibh ut nisl mollis rutrum tempor non sem. Aliquam urna tortor, placerat non dolor non, aliquam consectetur libero. In pharetra eget sem nec venenatis. Morbi ut ipsum ac lacus suscipit interdum vel id nisl. Vestibulum laoreet tortor congue dolor eleifend, at pretium nibh congue. Pellentesque porttitor aliquet nisi ut porttitor. Integer in eros vel felis malesuada varius ut nec neque. Ut sagittis tempus eleifend.

Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Nulla facilisi. Pellentesque pharetra iaculis sem, sed tempor leo lobortis non. Sed molestie lacus eu erat porta, vel condimentum velit tempus. Vestibulum commodo, lectus ut sodales ullamcorper, est tellus varius tortor, quis imperdiet magna lorem in libero. Etiam aliquet, purus a rutrum ullamcorper, metus elit laoreet felis, vitae venenatis libero risus id arcu. Vestibulum et gravida lectus, vel suscipit nulla. Curabitur non libero hendrerit, sollicitudin augue sit amet, venenatis felis. Ut vel pretium erat. Vivamus sollicitudin posuere tellus sit amet mollis. Pellentesque lectus risus, elementum sed faucibus eget, ullamcorper a ligula. In hac habitasse platea dictumst. Fusce lacinia, tortor tristique facilisis fringilla, lacus risus faucibus libero, at varius felis leo sed eros. Sed aliquet lorem id auctor varius. Mauris tincidunt eget mi sit amet consequat. Vestibulum placerat magna sit amet pretium interdum.\s""",
          8,
          javaSpec,
          mentorJava);

      createArticleIfNotFound(
          articleRepository,
          "Основы Data Science с Pandas",
          "Pandas - это ключевая библиотека для любого Data Scientist'а. Давайте разберем ее основные функции. Pandas - это ключевая библиотека для любого Data Scientist'а. Давайте разберем ее основные функции. Pandas - это ключевая библиотека для любого Data Scientist'а. Давайте разберем ее основные функции. Pandas - это ключевая библиотека для любого Data Scientist'а. Давайте разберем ее основные функции. Pandas - это ключевая библиотека для любого Data Scientist'а. Давайте разберем ее основные функции. Pandas - это ключевая библиотека для любого Data Scientist'а. Давайте разберем ее основные функции. Pandas - это ключевая библиотека для любого Data Scientist'а. Давайте разберем ее основные функции. Pandas - это ключевая библиотека для любого Data Scientist'а. Давайте разберем ее основные функции.",
          5,
          dataScienceSpec,
          mentorPython);

      createArticleIfNotFound(
          articleRepository,
          "React Hooks: Полное руководство",
          "Хуки изменили то, как мы пишем компоненты в React. useState, useEffect и другие... (полный текст статьи)",
          12,
          frontendSpec,
          mentorFrontend);

      log.info("--- Завершение работы сидера ---");
    };
  }

  // --- Приватные хелпер-методы для идемпотентности и чистоты кода ---

  private Mentor createMentorIfNotFound(
      MentorRepository repo,
      String email,
      String firstName,
      String lastName,
      String description,
      int rank,
      String vk,
      String tg,
      Speciality spec) {
    return repo.findByEmail(email)
        .orElseGet(
            () -> {
              Mentor mentor = new Mentor();
              mentor.setEmail(email);
              mentor.setPassword(passwordEncoder.encode("qweqwe"));
              mentor.setFirstName(firstName);
              mentor.setLastName(lastName);
              mentor.setDescription(description);
              mentor.setRank(rank);
              mentor.setVkUrl(vk);
              mentor.setTelegramUrl(tg);
              mentor.setSpeciality(spec);
              log.info("Создан ментор: {}", email);
              return repo.save(mentor);
            });
  }

  private Student createStudentIfNotFound(
      StudentRepository repo,
      String email,
      String firstName,
      String lastName,
      String description,
      String vk,
      String tg) {
    return repo.findByEmail(email)
        .orElseGet(
            () -> {
              Student student = new Student();
              student.setEmail(email);
              student.setPassword(passwordEncoder.encode("qweqwe"));
              student.setFirstName(firstName);
              student.setLastName(lastName);
              student.setDescription(description);
              student.setVkUrl(vk);
              student.setTelegramUrl(tg);
              log.info("Создан студент: {}", email);
              return repo.save(student);
            });
  }

  private void createArticleIfNotFound(
      ArticleRepository repo,
      String title,
      String content,
      int rank,
      Speciality spec,
      Mentor author) {
    if (!repo.existsByTitle(title)) {
      Article article = new Article();
      article.setTitle(title);
      article.setContent(content);
      article.setRank(rank);
      article.setSpeciality(spec);
      article.setAuthor(author);
      repo.save(article);
      log.info("Создана статья: '{}'", title);
    }
  }
}
