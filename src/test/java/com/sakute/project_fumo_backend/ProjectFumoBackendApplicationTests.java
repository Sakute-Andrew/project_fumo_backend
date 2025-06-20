package com.sakute.project_fumo_backend;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.FundraisingCategory;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FundraisingDataCreationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("Створення 10 записів Fundraising з донатами")
    public void testCreateFundraisingRecords() {
        System.out.println("Початок створення тестових даних...");

        // Створюємо категорії
//        List<FundraisingCategory> categories = createCategories();
//        System.out.println("Створено категорій: " + categories.size());

//        // Створюємо користувачів (якщо потрібно)
//        List<User> users = createUsers();
//        System.out.println("Створено користувачів: " + users.size());

        // Створюємо 10 записів Fundraising
        List<Fundraising> fundraisings = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Fundraising fundraising = new Fundraising();
            fundraising.setTitle("Збір коштів на проект " + i);
            fundraising.setDescription("Опис проекту номер " + i + ". Це важлива ініціатива для допомоги нужденним. " +
                    "Проект спрямований на покращення життя людей та підтримку важливих соціальних ініціатив.");

            // Різні цільові суми
            BigDecimal goalAmount = new BigDecimal("50000.00").add(new BigDecimal(i * 10000));
            fundraising.setGoalAmount(goalAmount);

            // Поточна сума (від 10% до 80% від цільової)
            BigDecimal currentAmount = goalAmount.multiply(new BigDecimal("0.1").add(new BigDecimal(Math.random() * 0.7)));
            fundraising.setCurrentAmount(currentAmount);

            // Встановлюємо дати
            LocalDateTime now = LocalDateTime.now();
            fundraising.setStartDate(Timestamp.valueOf(now.minusDays(i)));
            fundraising.setEndDate(Timestamp.valueOf(now.plusDays(30 + i * 5)));
            fundraising.setCreatedAt(Timestamp.valueOf(now.minusDays(i)));

            // Призначаємо категорію
            Long randomCategoryId = 1L + (long)(Math.random() * 5);
            fundraising.setCategory(randomCategoryId);


            entityManager.persist(fundraising);
            fundraisings.add(fundraising);
        }

        // Очищуємо кеш для гарантії збереження
        entityManager.flush();

        System.out.println("Створено записів Fundraising: " + fundraisings.size());

        // Створюємо донати для кожного збору
        int totalDonations = createDonations(fundraisings);
        System.out.println("Створено донатів: " + totalDonations);

        // Перевіряємо, що всі записи створено
        assertEquals(10, fundraisings.size());

        // Перевіряємо, що дані збережено правильно
        for (Fundraising fundraising : fundraisings) {
            assertNotNull(fundraising.getId(), "ID не повинно бути null");
            assertNotNull(fundraising.getTitle(), "Назва не повинна бути null");
            assertNotNull(fundraising.getDescription(), "Опис не повинен бути null");
            assertTrue(fundraising.getGoalAmount().compareTo(BigDecimal.ZERO) > 0, "Цільова сума повинна бути більше 0");
            assertTrue(fundraising.getCurrentAmount().compareTo(BigDecimal.ZERO) >= 0, "Поточна сума не може бути від'ємною");
            assertNotNull(fundraising.getStartDate(), "Дата початку не повинна бути null");
            assertNotNull(fundraising.getEndDate(), "Дата закінчення не повинна бути null");
            assertNotNull(fundraising.getCreatedAt(), "Дата створення не повинна бути null");

            System.out.println(String.format("Fundraising %d: %s - Ціль: %s, Поточна: %s",
                    fundraisings.indexOf(fundraising) + 1,
                    fundraising.getTitle(),
                    fundraising.getGoalAmount(),
                    fundraising.getCurrentAmount()));
        }

        System.out.println("✅ Тест успішно завершено!");
    }

    private List<FundraisingCategory> createCategories() {
        List<FundraisingCategory> categories = new ArrayList<>();
        String[] categoryNames = {"Медицина", "Освіта", "Тварини", "Спорт", "Культура"};

        for (String name : categoryNames) {
            FundraisingCategory category = new FundraisingCategory();

            // Спробуємо різні варіанти назв методів
            try {
                category.setCategoryName(name);
            } catch (Exception e1) {
                try {
                    // Якщо setCategoryName не працює, пробуємо setName
                    // category.setName(name);
                    System.out.println("Не вдалося встановити назву категорії: " + e1.getMessage());
                    continue;
                } catch (Exception e2) {
                    System.out.println("Помилка створення категорії: " + e2.getMessage());
                    continue;
                }
            }

            entityManager.persist(category);
            categories.add(category);
        }

        entityManager.flush();
        return categories;
    }

    private List<User> createUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            User user = new User();

            user.setEmail("user" + i + "@example.com");
            user.setUsername("user" + i);
            user.setFullName("user" + i);
            user.setPassword("password" + i);
            user.setUserRole(Role.USER);
            user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

            try {
                entityManager.persist(user);
                users.add(user);
            } catch (Exception e) {
                System.out.println("Не вдалося створити користувача: " + e.getMessage());
                // Якщо User має обов'язкові поля, які не встановлені
            }
        }

        entityManager.flush();
        return users;
    }

    private int createDonations(List<Fundraising> fundraisings) {
        String[] donorNames = {"Іван Петренко", "Марія Коваленко", "Олег Шевченко",
                "Анна Мельник", "Дмитро Бондаренко", "Петро Сидоренко", "Оксана Іваненко"};
        String[] donorEmails = {"ivan@example.com", "maria@example.com", "oleg@example.com",
                "anna@example.com", "dmitro@example.com", "petro@example.com", "oksana@example.com"};

        int totalDonations = 0;

        for (Fundraising fundraising : fundraisings) {
            // Створюємо 3-6 донатів для кожного збору
            int donationsCount = 3 + (int)(Math.random() * 4);

            for (int i = 0; i < donationsCount; i++) {
                Donation donation = new Donation();
                // Не встановлюємо ID - він буде згенерований автоматично
                donation.setFundraisingId(fundraising.getId());

                // Генеруємо реалістичні суми донатів (від 50 до 5000)
                double randomAmount = 50 + (Math.random() * 4950);
                donation.setAmount(BigDecimal.valueOf(Math.round(randomAmount * 100.0) / 100.0));

                donation.setDonorName(donorNames[i % donorNames.length]);
                donation.setDonorEmail(donorEmails[i % donorEmails.length]);
                donation.setTransactionId("TXN_" + System.currentTimeMillis() + "_" + i);
                donation.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusHours(i * 3)));
                donation.setIsAnonymous(Math.random() > 0.75); // 25% анонімних донатів


                entityManager.persist(donation);
                totalDonations++;
            }
        }

        entityManager.flush();
        return totalDonations;
    }
}