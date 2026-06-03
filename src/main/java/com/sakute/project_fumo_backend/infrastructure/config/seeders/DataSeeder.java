package com.sakute.project_fumo_backend.infrastructure.config.seeders;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.FundraisingCategory;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.enteties.user.UserProfiles;

import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingCategoryRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FundraisingCategoryRepository categoryRepository;
    private final FundraisingRepository fundraisingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Перевіряємо, чи база вже має дані, щоб не дублювати при кожному запуску
        if (userRepository.count() == 0) {
            log.info("Наповнення бази даних тестовими даними...");

            // Створюємо користувачів і отримуємо їх для подальшого використання
            User[] users = seedUsers();
            User admin = users[0];
            User standardUser = users[1];

            // Створюємо категорії та збори, прив'язуючи їх до користувачів
            seedFundraisings(admin, standardUser);

            log.info("Тестові дані успішно завантажено!");
        }
    }

    private User[] seedUsers() {
        // 1. Створюємо адміна
        User admin = new User();
        admin.setUserId(UUID.randomUUID());
        admin.setUsername("admin");
        admin.setEmail("admin@fumo.test");
        admin.setPassword(passwordEncoder.encode("12345"));
        admin.setFullName("Головний Адміністратор");
        admin.setRole(Role.ADMIN);
        admin.setCreatedAt(Timestamp.from(Instant.now()));

        UserProfiles adminProfile = new UserProfiles();
        adminProfile.setLocation("Ужгород");
        adminProfile.setWebsite("https://github.com");

        adminProfile.setUser(admin);
        admin.setUserProfile(adminProfile);

        admin = userRepository.save(admin);

        // 2. Створюємо звичайного користувача
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername("kremzlyk");
        user.setEmail("kremzlyk@fumo.test");
        user.setPassword(passwordEncoder.encode("qwerty"));
        user.setRole(Role.USER);
        user.setCreatedAt(Timestamp.from(Instant.now()));

        user = userRepository.save(user);

        return new User[]{admin, user};
    }

    private void seedFundraisings(User admin, User standardUser) {
        // 1. Створюємо дві категорії
        FundraisingCategory animalCategory = new FundraisingCategory();
        animalCategory.setCategoryName("Допомога тваринам");
        animalCategory = categoryRepository.save(animalCategory);

        FundraisingCategory techCategory = new FundraisingCategory();
        techCategory.setCategoryName("ІТ проєкти");
        techCategory = categoryRepository.save(techCategory);

        // 2. Створюємо перший збір (від звичайного юзера)
        Fundraising catFundraising = new Fundraising();
        catFundraising.setTitle("На смаколики та ліки для місцевих котиків");
        catFundraising.setDescription("Збираємо кошти на закупівлю корму преміум класу та вітамінів для пухнастих.");
        catFundraising.setGoalAmount(new BigDecimal("15000.00"));
        catFundraising.setStartDate(Timestamp.from(Instant.now()));
        catFundraising.setEndDate(Timestamp.from(Instant.now().plus(30, ChronoUnit.DAYS)));
        catFundraising.setCreatedAt(Timestamp.from(Instant.now()));
        catFundraising.setStatus(Fundraising.Status.ACTIVE);
        catFundraising.setOwner(standardUser);
        catFundraising.setCategory(animalCategory);

        // Додаємо донати до першого збору
        Donation donation1 = new Donation();
        donation1.setAmount(new BigDecimal("500.00"));
        donation1.setTransactionId(UUID.randomUUID().toString());
        donation1.setCreatedAt(Timestamp.from(Instant.now()));
        donation1.setIsAnonymous(false);
        donation1.setDonor(admin);
        donation1.setFundraising(catFundraising);

        Donation donation2 = new Donation();
        donation2.setAmount(new BigDecimal("1200.50"));
        donation2.setTransactionId(UUID.randomUUID().toString());
        donation2.setCreatedAt(Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS)));
        donation2.setIsAnonymous(true); // Анонімний донат
        donation2.setDonor(standardUser);
        donation2.setFundraising(catFundraising);

        // Додаємо донати в список збору (через CascadeType.ALL вони збережуться разом зі збором)
        catFundraising.getDonations().add(donation1);
        catFundraising.getDonations().add(donation2);

        fundraisingRepository.save(catFundraising);

        // 3. Створюємо другий збір (від адміна)
        Fundraising serverFundraising = new Fundraising();
        serverFundraising.setTitle("Оновлення заліза для Fedora-сервера");
        serverFundraising.setDescription("Потрібен новий SSD та додаткова оперативна пам'ять для тестування нових пет-проєктів на Spring Boot та React.");
        serverFundraising.setGoalAmount(new BigDecimal("8000.00"));
        serverFundraising.setStartDate(Timestamp.from(Instant.now()));
        serverFundraising.setEndDate(Timestamp.from(Instant.now().plus(14, ChronoUnit.DAYS)));
        serverFundraising.setCreatedAt(Timestamp.from(Instant.now()));
        serverFundraising.setStatus(Fundraising.Status.ACTIVE);
        serverFundraising.setOwner(admin);
        serverFundraising.setCategory(techCategory);

        // Додаємо донат до другого збору
        Donation donation3 = new Donation();
        donation3.setAmount(new BigDecimal("1500.00"));
        donation3.setTransactionId(UUID.randomUUID().toString());
        donation3.setCreatedAt(Timestamp.from(Instant.now()));
        donation3.setIsAnonymous(false);
        donation3.setDonor(standardUser);
        donation3.setFundraising(serverFundraising);

        serverFundraising.getDonations().add(donation3);

        fundraisingRepository.save(serverFundraising);
    }
}