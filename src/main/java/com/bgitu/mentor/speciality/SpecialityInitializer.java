package com.bgitu.mentor.speciality;

import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.speciality.data.repository.SpecialityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecialityInitializer {

  private final SpecialityRepository specialityRepository;

  private static final List<String> DEFAULT_SPECIALITIES =
      List.of(
          "Backend Development", // Разработка серверной части приложений (Java, Python, Node.js и
          // т.д.)
          "Frontend Development", // Разработка клиентской части (React, Angular, Vue.js)
          "Full-Stack Development", // Полный цикл разработки (Frontend + Backend)
          "Mobile Development", // Разработка мобильных приложений (iOS, Android, Flutter, React
          "Java Development", // Разработка на Java (Spring, Hibernate)
          "Python Development", // Разработка на Python (Django, Flask)
          "JavaScript Development", // Разработка на JavaScript (Node.js, Express)
          "C#/.NET Development", // Разработка на C# (.NET Core, ASP.NET)
          "Go Development", // Разработка на Go (Golang)
          "Data Science", // Анализ данных, машинное обучение, статистика
          "Machine Learning Engineering", // Разработка и внедрение ML-моделей
          "Artificial Intelligence", // Искусственный интеллект, нейронные сети
          "DevOps", // CI/CD, инфраструктура, облачные технологии
          "Cloud Architecture", // Проектирование облачных решений (AWS, Azure, GCP)
          "UI/UX Design", // Дизайн интерфейсов и пользовательского опыта
          "Product Design", // Проектирование продуктов, прототипирование
          "Graphic Design", // Графический дизайн, создание визуальных материалов
          "Game Development", // Разработка игр (Unity, Unreal Engine)
          "Blockchain Development", // Разработка на блокчейне (Ethereum, Solidity)
          "Cybersecurity", // Кибербезопасность, защита данных
          "Quality Assurance (QA)", // Тестирование ПО (мануальное и автоматизированное)
          "Database Administration", // Управление базами данных (SQL, NoSQL)
          "Data Engineering", // Построение ETL-процессов, обработка больших данных
          "Embedded Systems", // Разработка для встроенных систем и IoT
          "AR/VR Development", // Разработка приложений для дополненной и виртуальной реальности
          "Product Management", // Управление продуктами, планирование и стратегия
          "Project Management", // Управление проектами (Agile, Scrum, Kanban)
          "Business Analysis", // Анализ бизнес-процессов и требований
          "Big Data", // Работа с большими данными (Hadoop, Spark)
          "System Architecture" // Проектирование сложных систем и архитектур
          );

  @PostConstruct
  public void init() {
    for (String name : DEFAULT_SPECIALITIES) {
      if (!specialityRepository.existsByName(name)) {
        Speciality speciality = new Speciality();
        speciality.setName(name);
        specialityRepository.save(speciality);
      }
    }
  }
}
