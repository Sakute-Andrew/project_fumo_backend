package com.sakute.project_fumo_backend.infrastructure.config.seeders;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.PayoutRequest;
import com.sakute.project_fumo_backend.domain.enteties.PermissionRequest;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Donation;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.FundraisingCategory;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualPropertyCategory;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.enteties.user.UserProfiles;
import com.sakute.project_fumo_backend.repository.jpa_repo.*;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FundraisingCategoryRepository fundraisingCategoryRepository;
    private final FundraisingRepository fundraisingRepository;
    private final IntellectualPropertyCategoryRepository ipCategoryRepository;
    private final IntellectualPropertyRepository ipRepository;
    private final PostTagTopicRepository postTagTopicRepository;
    private final UserPostRepository postRepository;
    private final PayoutRequestRepository payoutRequestRepository;
    private final PermissionRequestRepository permissionRequestRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        log.info("Seeding database...");

        List<FundraisingCategory> fundraisingCategories = seedFundraisingCategories();
        List<IntellectualPropertyCategory> ipCategories = seedIpCategories();
        List<PostTagTopic> topics = seedPostTopics();
        List<User> users = seedUsers();

        List<Fundraising> fundraisings = seedFundraisings(users, fundraisingCategories);
        seedPayoutRequests(fundraisings);

        seedIpAssets(users, ipCategories);
        seedPosts(users, topics);
        seedPermissionRequests(users);

        log.info("Database seeded successfully.");
    }

    private List<FundraisingCategory> seedFundraisingCategories() {
        return fundraisingCategoryRepository.saveAll(List.of(
                category("Допомога тваринам"),
                category("ІТ та технології"),
                category("Медицина та здоров'я")
        ));
    }

    private List<IntellectualPropertyCategory> seedIpCategories() {
        return ipCategoryRepository.saveAll(List.of(
                ipCategory("Музика"),
                ipCategory("Програмне забезпечення"),
                ipCategory("Графічний дизайн")
        ));
    }

    private List<PostTagTopic> seedPostTopics() {
        return postTagTopicRepository.saveAll(List.of(
                topic("Оголошення"),
                topic("Новини"),
                topic("Обговорення")
        ));
    }

    private List<User> seedUsers() {
        User admin = buildUser("admin", "admin@fumo.test", "Admin1234!", "Головний Адміністратор", Role.ADMIN,
                Set.of(Permission.CAN_FUNDRAISE, Permission.CAN_SELL_IP), -30);
        admin.setUserProfile(buildProfile(admin, "Ужгород", "https://github.com/sakute",
                "Адміністратор платформи Fumo", "Управління, Spring Boot"));

        User fundraiser = buildUser("kremzlyk", "kremzlyk@fumo.test", "qwerty", "Крем Злик", Role.USER,
                Set.of(Permission.CAN_FUNDRAISE), -25);
        fundraiser.setUserProfile(buildProfile(fundraiser, "Київ", "https://kremzlyk.dev",
                "Активний фандрейзер та меломан", "Фандрейзинг, Музика"));

        User ipSeller = buildUser("artmaster", "artmaster@fumo.test", "Artmaster99!", "Арт Майстер", Role.USER,
                Set.of(Permission.CAN_SELL_IP), -20);
        ipSeller.setUserProfile(buildProfile(ipSeller, "Львів", null,
                "Дизайнер та автор інтелектуальної власності", "Дизайн, Ілюстрація"));

        User devUser = buildUser("devnull", "devnull@fumo.test", "Dev1234!", "Dev Null", Role.USER,
                Set.of(Permission.CAN_FUNDRAISE, Permission.CAN_SELL_IP), -18);

        User musicUser = buildUser("beatmaker", "beatmaker@fumo.test", "Beat1234!", "Біт Мейкер", Role.USER,
                Set.of(Permission.CAN_SELL_IP), -15);
        musicUser.setUserProfile(buildProfile(musicUser, "Харків", "https://soundcloud.com/beatmaker",
                "Продюсер електронної музики", "Музика, Аранжування"));

        User volunteer = buildUser("volunteer42", "volunteer42@fumo.test", "Vol1234!", "Волонтер Надія", Role.USER,
                Set.of(Permission.CAN_FUNDRAISE), -12);

        User blogger = buildUser("writergirl", "writergirl@fumo.test", "Write1234!", "Олена Коваль", Role.USER,
                Set.of(), -10);
        blogger.setUserProfile(buildProfile(blogger, "Одеса", "https://medium.com/@writergirl",
                "Блогерка та авторка текстів", "Письменство, Контент-маркетинг"));

        User techGuy = buildUser("linus_ua", "linus_ua@fumo.test", "Linux1234!", "Лінус Петренко", Role.USER,
                Set.of(Permission.CAN_FUNDRAISE, Permission.CAN_SELL_IP), -8);

        User photographer = buildUser("photopro", "photopro@fumo.test", "Photo1234!", "Марко Фото", Role.USER,
                Set.of(Permission.CAN_SELL_IP), -6);
        photographer.setUserProfile(buildProfile(photographer, "Дніпро", null,
                "Фотограф та відеограф", "Фотографія, Відеомонтаж"));

        User gamer = buildUser("gamer_ua", "gamer_ua@fumo.test", "Gamer1234!", "Геймер Захар", Role.USER,
                Set.of(Permission.CAN_FUNDRAISE), -4);

        User newbie = buildUser("newcomer", "newcomer@fumo.test", "New1234!", "Новачок Іван", Role.USER,
                Set.of(), -1);

        List<User> all = List.of(admin, fundraiser, ipSeller, devUser, musicUser,
                volunteer, blogger, techGuy, photographer, gamer, newbie);
        return userRepository.saveAll(all);
    }

    private List<Fundraising> seedFundraisings(List<User> users, List<FundraisingCategory> cats) {
        FundraisingCategory animals = cats.get(0);
        FundraisingCategory tech    = cats.get(1);
        FundraisingCategory medical = cats.get(2);

        User admin     = users.get(0);
        User kremzlyk  = users.get(1);
        User devUser   = users.get(3);
        User volunteer = users.get(5);
        User techGuy   = users.get(7);
        User gamer     = users.get(9);

        Fundraising f1 = buildFundraising("Ліки та корм для місцевих котиків",
                "Збираємо кошти на закупівлю корму преміум-класу та вітамінів для безпритульних котів.",
                "15000.00", animals, kremzlyk, Fundraising.Status.ACTIVE, -30, 20);
        addDonation(f1, admin,    "500.00",   false, -28);
        addDonation(f1, devUser,  "1200.50",  true,  -25);
        addDonation(f1, gamer,    "300.00",   false, -20);

        Fundraising f2 = buildFundraising("Оновлення сервера для pet-проєктів",
                "Потрібен новий SSD та RAM для запуску Spring Boot і React-проєктів.",
                "8000.00", tech, admin, Fundraising.Status.ACTIVE, -20, 14);
        addDonation(f2, kremzlyk, "1500.00",  false, -19);
        addDonation(f2, techGuy,  "2000.00",  false, -17);

        Fundraising f3 = buildFundraising("Реабілітація для бійців АТО",
                "Збір коштів на протезування та психологічну реабілітацію ветеранів.",
                "50000.00", medical, volunteer, Fundraising.Status.ACTIVE, -15, 60);
        addDonation(f3, admin,    "5000.00",  false, -14);
        addDonation(f3, kremzlyk, "2500.00",  false, -13);
        addDonation(f3, gamer,    "1000.00",  true,  -12);

        Fundraising f4 = buildFundraising("Новий MacBook для відкритого навчання",
                "Допоможіть придбати ноутбук для безкоштовного онлайн-курсу з програмування для школярів.",
                "35000.00", tech, devUser, Fundraising.Status.ACTIVE, -10, 30);
        addDonation(f4, volunteer, "800.00",  true,  -9);

        Fundraising f5 = buildFundraising("Допомога притулку для собак 'Лапа'",
                "Притулок потребує ремонту будівлі та закупівлі медичних препаратів для 80 собак.",
                "25000.00", animals, volunteer, Fundraising.Status.ACTIVE, -8, 45);

        Fundraising f6 = buildFundraising("Розробка відкритої карти медзакладів",
                "Створення відкритої мапи з усіма медичними закладами України та їх профілями.",
                "12000.00", medical, techGuy, Fundraising.Status.ACTIVE, -7, 40);

        Fundraising f7 = buildFundraising("Open-source бібліотека для Spring Security",
                "Підтримка розробки open-source розширення для спрощення налаштування безпеки.",
                "5000.00", tech, admin, Fundraising.Status.COMPLETED, -60, -10);

        Fundraising f8 = buildFundraising("Вакцинація бездомних тварин у Харкові",
                "Кампанія з масової вакцинації безпритульних тварин у Харківській області.",
                "18000.00", animals, kremzlyk, Fundraising.Status.COMPLETED, -90, -30);

        Fundraising f9 = buildFundraising("Курс кібербезпеки для студентів",
                "Забезпечення безкоштовного доступу до курсів з кібербезпеки для 50 студентів.",
                "7500.00", tech, devUser, Fundraising.Status.ACTIVE, -5, 25);

        Fundraising f10 = buildFundraising("Лікування онкохворих дітей",
                "Термінова допомога сім'ям з дітьми, що проходять хіміотерапію у Львівській дитячій лікарні.",
                "100000.00", medical, volunteer, Fundraising.Status.ACTIVE, -3, 90);
        addDonation(f10, admin,    "10000.00", false, -2);

        User artmaster2   = users.get(2);
        User musicUser2   = users.get(4);
        User blogger2     = users.get(6);
        User photographer2 = users.get(8);
        User newbie2      = users.get(10);

        addDonation(f1,  volunteer,    "750.00",   false, -22);
        addDonation(f2,  artmaster2,   "500.00",   false, -16);
        addDonation(f3,  techGuy,      "1500.00",  false, -11);
        addDonation(f3,  photographer2,"800.00",   true,  -10);
        addDonation(f4,  kremzlyk,     "2000.00",  false, -8);
        addDonation(f5,  admin,        "3000.00",  false, -7);
        addDonation(f5,  musicUser2,   "250.00",   true,  -6);
        addDonation(f6,  blogger2,     "150.00",   false, -5);
        addDonation(f9,  newbie2,      "100.00",   false, -4);
        addDonation(f10, gamer,        "500.00",   false, -1);

        Fundraising f11 = buildFundraising("Онлайн-курс з UI/UX для студентів",
                "Безкоштовний курс з основ дизайну інтерфейсів для студентів українських університетів.",
                "6000.00", tech, artmaster2, Fundraising.Status.ACTIVE, -14, 30);
        addDonation(f11, admin,      "1000.00", false, -13);
        addDonation(f11, devUser,    "500.00",  true,  -12);

        Fundraising f12 = buildFundraising("Порятунок їжаків у Карпатах",
                "Фінансування зимової реабілітації їжаків, постраждалих від вирубки лісів.",
                "4500.00", animals, photographer2, Fundraising.Status.ACTIVE, -11, 20);
        addDonation(f12, kremzlyk,   "300.00",  false, -10);
        addDonation(f12, volunteer,  "900.00",  false, -9);

        Fundraising f13 = buildFundraising("Мобільна стоматологія для сільських районів",
                "Придбання обладнання для виїзного стоматологічного кабінету в 5 сільських громадах.",
                "45000.00", medical, kremzlyk, Fundraising.Status.ACTIVE, -9, 60);
        addDonation(f13, admin,      "5000.00", false, -8);
        addDonation(f13, gamer,      "700.00",  true,  -7);

        Fundraising f14 = buildFundraising("Запис дебютного альбому",
                "Збір коштів на студійний запис, мастеринг та дистрибуцію першого повноформатного альбому.",
                "20000.00", tech, musicUser2, Fundraising.Status.ACTIVE, -7, 45);
        addDonation(f14, kremzlyk,   "2000.00", false, -6);
        addDonation(f14, blogger2,   "350.00",  true,  -5);

        Fundraising f15 = buildFundraising("Обладнання для тваринного притулку у Запоріжжі",
                "Закупівля кліток, медичного обладнання та корму для нового притулку на 150 місць.",
                "30000.00", animals, admin, Fundraising.Status.COMPLETED, -120, -20);

        Fundraising f16 = buildFundraising("Турнір з кіберспорту на підтримку ЗСУ",
                "Організація онлайн-турніру з Dota 2 та CS2 — всі кошти передаються на потреби армії.",
                "15000.00", tech, gamer, Fundraising.Status.ACTIVE, -6, 14);
        addDonation(f16, devUser,    "1000.00", false, -5);
        addDonation(f16, techGuy,    "500.00",  false, -4);
        addDonation(f16, newbie2,    "200.00",  true,  -3);

        Fundraising f17 = buildFundraising("Видання незалежного літературного журналу",
                "Фінансування друку та дистрибуції першого номера щоквартального журналу авторської прози.",
                "9000.00", tech, blogger2, Fundraising.Status.ACTIVE, -5, 25);
        addDonation(f17, volunteer,  "500.00",  false, -4);

        Fundraising f18 = buildFundraising("Розумна годівниця для міських птахів",
                "Розробка та встановлення IoT-годівниць у 10 парках Харкова з моніторингом у реальному часі.",
                "11000.00", animals, techGuy, Fundraising.Status.ACTIVE, -4, 35);
        addDonation(f18, artmaster2, "400.00",  false, -3);
        addDonation(f18, musicUser2, "600.00",  true,  -2);

        Fundraising f19 = buildFundraising("Психологічна підтримка для IT-спеціалістів",
                "Безкоштовні сесії з психологом для розробників, що пережили вигорання або стрес від воєнного часу.",
                "25000.00", medical, devUser, Fundraising.Status.ACTIVE, -2, 50);
        addDonation(f19, admin,      "3000.00", false, -1);
        addDonation(f19, kremzlyk,   "1000.00", false, -1);

        Fundraising f20 = buildFundraising("Фото-виставка 'Незламна Україна'",
                "Організація пересувної фотовиставки з 80 знімків документальної фотографії для 6 міст.",
                "13000.00", tech, photographer2, Fundraising.Status.ACTIVE, -1, 60);

        return fundraisingRepository.saveAll(
                List.of(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10,
                        f11, f12, f13, f14, f15, f16, f17, f18, f19, f20));
    }

    private void seedPayoutRequests(List<Fundraising> fundraisings) {
        PayoutRequest pr1 = new PayoutRequest();
        pr1.setAmount(new BigDecimal("3200.00"));
        pr1.setCardNumber("4111111111111111");
        pr1.setCreatedAt(ts(-5));
        pr1.setStatus(RequestStatus.PENDING);
        pr1.setFundraising(fundraisings.get(6));

        PayoutRequest pr2 = new PayoutRequest();
        pr2.setAmount(new BigDecimal("15000.00"));
        pr2.setCardNumber("5500005555555559");
        pr2.setCreatedAt(ts(-3));
        pr2.setStatus(RequestStatus.APPROVED);
        pr2.setFundraising(fundraisings.get(7));

        PayoutRequest pr3 = new PayoutRequest();
        pr3.setAmount(new BigDecimal("800.00"));
        pr3.setCardNumber("4111111111111111");
        pr3.setCreatedAt(ts(-1));
        pr3.setStatus(RequestStatus.PENDING);
        pr3.setFundraising(fundraisings.get(0));

        payoutRequestRepository.saveAll(List.of(pr1, pr2, pr3));
    }

    private void seedIpAssets(List<User> users, List<IntellectualPropertyCategory> cats) {
        IntellectualPropertyCategory music   = cats.get(0);
        IntellectualPropertyCategory software = cats.get(1);
        IntellectualPropertyCategory design  = cats.get(2);

        User artmaster   = users.get(2);
        User musicUser   = users.get(4);
        User techGuy     = users.get(7);
        User photographer = users.get(8);
        User admin       = users.get(0);
        User devUser2    = users.get(3);

        ipRepository.saveAll(List.of(
                buildIp("Ambient Soundscape Vol.1", "Збірка із 12 ембієнт-треків для медитації та релаксу.",
                        "MUSIC", musicUser, music, IpStatus.AVAILABLE, -40),
                buildIp("UI Kit — Fumo Design System", "Повний набір компонентів інтерфейсу у стилі Fumo: кнопки, картки, форми.",
                        "DESIGN", artmaster, design, IpStatus.AVAILABLE, -35),
                buildIp("Spring Security OAuth2 Starter", "Готовий стартер для налаштування OAuth2 в Spring Boot 3+.",
                        "SOFTWARE", techGuy, software, IpStatus.AVAILABLE, -30),
                buildIp("Urban Photography Pack", "Колекція з 200 RAW-знімків міської архітектури для комерційного використання.",
                        "PHOTO", photographer, design, IpStatus.SOLD, -25),
                buildIp("Lo-Fi Hip-Hop Beats Pack", "30 унікальних lo-fi бітів у форматі WAV/MP3 з ліцензією на YouTube.",
                        "MUSIC", musicUser, music, IpStatus.AVAILABLE, -22),
                buildIp("React Component Library", "Бібліотека з 60+ React-компонентів на TypeScript із Storybook-документацією.",
                        "SOFTWARE", techGuy, software, IpStatus.AVAILABLE, -20),
                buildIp("Brand Identity — EcoStart", "Повний брендинг для еко-стартапу: логотип, палітра, типографіка, mockups.",
                        "DESIGN", artmaster, design, IpStatus.AVAILABLE, -18),
                buildIp("Synthwave Album 'Retro Drive'", "Альбом із 10 треків у стилі synthwave, повні права передачі.",
                        "MUSIC", musicUser, music, IpStatus.UNAVAILABLE, -15),
                buildIp("Kotlin Coroutines Cheat Sheet", "Візуальний довідник по Kotlin Coroutines у форматі PDF та Notion.",
                        "SOFTWARE", admin, software, IpStatus.AVAILABLE, -10),
                buildIp("Social Media Icon Set", "Набір із 150 іконок для соцмереж у форматах SVG, PNG, Figma.",
                        "DESIGN", artmaster, design, IpStatus.LEGAL_TROUBLES, -8),
                buildIp("JavaScript Animations Cookbook", "Збірка з 50 готових CSS/JS анімацій для вебсайтів з документацією.",
                        "SOFTWARE", devUser2, software, IpStatus.AVAILABLE, -7),
                buildIp("Nature Sounds Collection Vol.2", "60 звукових ефектів природи у форматі WAV/FLAC для медіапроєктів.",
                        "MUSIC", musicUser, music, IpStatus.AVAILABLE, -6),
                buildIp("Minimalist Logo Pack", "25 мінімалістичних логотипів у векторному форматі для стартапів.",
                        "DESIGN", artmaster, design, IpStatus.AVAILABLE, -5),
                buildIp("PostgreSQL Performance Guide", "Практичний посібник з оптимізації запитів та індексів у PostgreSQL.",
                        "SOFTWARE", admin, software, IpStatus.AVAILABLE, -4),
                buildIp("Street Art Photography Bundle", "Добірка з 500 вуличних фотографій зі Львова та Одеси.",
                        "PHOTO", photographer, design, IpStatus.AVAILABLE, -3),
                buildIp("Dark Techno Beats Vol.1", "20 треків темного техно у форматі WAV з повними правами ліцензії.",
                        "MUSIC", musicUser, music, IpStatus.AVAILABLE, -2),
                buildIp("Vue.js Dashboard Template", "Готовий адмін-дашборд на Vue 3 + Vite з 30+ компонентами.",
                        "SOFTWARE", techGuy, software, IpStatus.SOLD, -10),
                buildIp("Illustrated Sticker Pack — Ukrainian Folk", "48 стікерів у стилі народної графіки для Telegram.",
                        "DESIGN", artmaster, design, IpStatus.AVAILABLE, -9),
                buildIp("E-book: Linux для розробників", "Практичний посібник з налаштування Linux-середовища для backend-розробників.",
                        "SOFTWARE", techGuy, software, IpStatus.AVAILABLE, -8),
                buildIp("Wedding Photo Presets Pack", "Набір з 30 Lightroom-пресетів для весільної фотографії.",
                        "PHOTO", photographer, design, IpStatus.LEGAL_TROUBLES, -12)
        ));
    }

    private void seedPosts(List<User> users, List<PostTagTopic> topics) {
        PostTagTopic announcements = topics.get(0);
        PostTagTopic news          = topics.get(1);
        PostTagTopic discussion    = topics.get(2);

        User admin        = users.get(0);
        User kremzlyk     = users.get(1);
        User artmaster    = users.get(2);
        User devUser      = users.get(3);
        User musicUser    = users.get(4);
        User volunteer    = users.get(5);
        User blogger      = users.get(6);
        User techGuy      = users.get(7);
        User photographer = users.get(8);
        User gamer        = users.get(9);
        User newbie       = users.get(10);

        UserPost p1 = buildPost("Ласкаво просимо на платформу Fumo!",
                "Офіційний запуск нашої платформи творчої спільноти.",
                "Fumo — це місце, де творці та донори зустрічаються. Ми раді вітати перших учасників!",
                admin, announcements, -30);
        addComment(p1, kremzlyk, "Чудова ідея! Вже зареєструвався.", -29);
        addComment(p1, artmaster, "Нарешті місце для авторів!", -28);

        UserPost p2 = buildPost("Як почати збір коштів за 5 кроків",
                "Практичний гайд для новачків у фандрейзингу.",
                "Описуємо покроково: від ідеї до першого донату. Створіть переконливий опис, встановіть реалістичну ціль...",
                kremzlyk, discussion, -28);
        addComment(p2, devUser, "Корисно, дякую за деталі!", -27);
        addComment(p2, blogger, "Зберегла в закладки.", -26);

        UserPost p3 = buildPost("Мій новий UI Kit вже доступний",
                "Презентую Fumo Design System — безкоштовно для спільноти.",
                "Після 3 місяців роботи я завершив перший публічний дизайн-кіт. Завантажуйте у розділі IP.",
                artmaster, announcements, -25);
        addComment(p3, kremzlyk, "Виглядає чудово, використаю у проєкті!", -24);

        UserPost p4 = buildPost("Чому я вибрав Spring Boot для Fumo Backend",
                "Розбір технологічного стеку проєкту.",
                "Spring Boot, JPA, PostgreSQL, Spring Security — класичний та надійний стек для корпоративних додатків...",
                devUser, discussion, -22);
        addComment(p4, techGuy, "Kotlin + Spring — ще краще комбо!", -21);
        addComment(p4, admin, "Гарна стаття, дякую за розбір.", -20);

        UserPost p5 = buildPost("Оновлення платформи: нові функції для адмінів",
                "Додали дашборд, управління дозволами та нові звіти.",
                "Тепер адміни мають доступ до детальної статистики, фільтрації користувачів та управління заявками на виплати.",
                admin, news, -18);

        UserPost p6 = buildPost("Топ-5 помилок початківців у фандрейзингу",
                "Поширені помилки та як їх уникнути.",
                "Нереалістична ціль, відсутність оновлень для донорів, слабкий опис — це лише частина проблем...",
                blogger, discussion, -15);
        addComment(p6, kremzlyk, "Сам допускав помилку №3!", -14);

        UserPost p7 = buildPost("Як я продав перший beat на Fumo",
                "Особиста історія успіху від Біт Мейкера.",
                "Все почалося з одного завантаженого треку. Через тиждень перший продаж, і це лише початок...",
                musicUser, news, -12);

        UserPost p8 = buildPost("Відкрита карта медзакладів: статус проєкту",
                "Звіт про хід розробки та поточні результати.",
                "Зібрали базу з 1200 закладів, додали фільтр по регіонах та спеціалізації. Бета-тест скоро!",
                techGuy, news, -10);
        addComment(p8, devUser, "Коли публічний бета-тест?", -9);

        UserPost p9 = buildPost("Геймери теж можуть змінювати світ",
                "Як ігрова спільнота допомагає у фандрейзингу.",
                "Стрими-марафони, турніри на благодійність — геймери збирають мільйони щороку. Долучайтесь!",
                gamer, discussion, -7);
        addComment(p9, kremzlyk, "Підтримую! Вже беру участь у благодійному турнірі.", -6);
        addComment(p9, volunteer, "Дякую за підтримку спільноти!", -5);

        UserPost p10 = buildPost("Правила платформи: що нового у 2026",
                "Оновлені умови використання та правила модерації.",
                "Ми оновили правила щодо IP-контенту, фандрейзингу та поведінки у спільноті. Будь ласка, ознайомтесь.",
                admin, announcements, -3);

        UserPost p11 = buildPost("Перші кроки в дизайні: з чого почати",
                "Гайд для майбутніх дизайнерів-початківців.",
                "Figma, Canva, Adobe XD — обираємо інструмент, вивчаємо базові принципи і починаємо практику разом.",
                artmaster, discussion, -20);
        addComment(p11, blogger, "Figma назавжди змінила мій підхід до дизайну!", -19);

        UserPost p12 = buildPost("Як я написав свою першу бібліотеку на Kotlin",
                "Особистий досвід публікації open-source бібліотеки.",
                "Ідея виникла під час написання чергового boilerplate-коду. Через два тижні — перша зірка на GitHub!",
                techGuy, news, -17);
        addComment(p12, devUser, "Надихнув на власний проєкт, дякую!", -16);
        addComment(p12, admin, "Kotlin дійсно спрощує багато речей.", -15);

        UserPost p13 = buildPost("Волонтерство у 2026: тренди та можливості",
                "Огляд сучасного волонтерського руху в Україні.",
                "Цифрове волонтерство, краудфандинг, допомога ветеранам — волонтери стають потужною силою змін.",
                volunteer, news, -13);
        addComment(p13, kremzlyk, "Горджуся нашою спільнотою!", -12);

        UserPost p14 = buildPost("Музика як бізнес: монетизація творчості",
                "Як незалежний музикант може заробляти в Україні.",
                "Стримінг, продаж семплів, саундтреки для ігор і реклами — ось реальні канали монетизації.",
                musicUser, discussion, -11);
        addComment(p14, kremzlyk, "Доповнив би ще концерти та мерч!", -10);

        UserPost p15 = buildPost("Кейс: фандрейзинг для притулку 'Лапа'",
                "Детальний звіт про перебіг кампанії з допомоги тваринам.",
                "За 3 тижні зібрали 18 000 грн. Розповідаємо, які методи просування спрацювали найкраще.",
                volunteer, news, -9);
        addComment(p15, gamer, "Приєднався до збору, бажаю успіху!", -8);

        UserPost p16 = buildPost("5 безкоштовних ресурсів для вивчення кібербезпеки",
                "Список платформ для старту в інформаційній безпеці.",
                "TryHackMe, HackTheBox, OWASP, Cybrary та Coursera — де і як починати вивчати безпеку без бюджету.",
                devUser, discussion, -6);
        addComment(p16, techGuy, "HackTheBox для мене найкращий!", -5);

        UserPost p17 = buildPost("Фото-репортаж: Одеса у травні",
                "Добірка фотографій міста для спільноти.",
                "Ділюся серією знімків, зроблених під час останньої поїздки до Одеси. Золоте світло, архітектура та люди.",
                photographer, announcements, -5);
        addComment(p17, artmaster, "Неймовірні кольори! Яка камера?", -4);

        UserPost p18 = buildPost("Ігровий стрим на благодійність: підсумки",
                "Результати 48-годинного ігрового марафону.",
                "Зібрали 7 300 грн за 48 годин! Всі кошти передано до фонду підтримки дітей. Дякуємо глядачам!",
                gamer, news, -4);
        addComment(p18, volunteer, "Дякую за ініціативу, ви молодці!", -3);

        UserPost p19 = buildPost("Чому я написала свій перший блог-пост",
                "Особиста історія авторки платформи.",
                "Спочатку було страшно ділитись думками публічно. Але перший відгук від незнайомця змінив все.",
                blogger, discussion, -2);
        addComment(p19, kremzlyk, "Дуже щира стаття, надихнула!", -1);

        UserPost p20 = buildPost("Мій перший тиждень на Fumo",
                "Враження новачка від платформи.",
                "Зареєструвався, переглянув кілька фандрейзингів і вже хочеться зробити внесок. Класна атмосфера!",
                newbie, discussion, -1);

        postRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
                p11, p12, p13, p14, p15, p16, p17, p18, p19, p20));
    }

    private void seedPermissionRequests(List<User> users) {
        User blogger  = users.get(6);
        User gamer    = users.get(9);
        User newbie   = users.get(10);

        PermissionRequest pr1 = new PermissionRequest();
        pr1.setUser(blogger);
        pr1.setRequestedPermission(Permission.CAN_FUNDRAISE);
        pr1.setStatus(RequestStatus.PENDING);
        pr1.setMessage("Хочу зібрати кошти на видання власної книги.");
        pr1.setCreatedAt(ts(-5));

        PermissionRequest pr2 = new PermissionRequest();
        pr2.setUser(gamer);
        pr2.setRequestedPermission(Permission.CAN_SELL_IP);
        pr2.setStatus(RequestStatus.PENDING);
        pr2.setMessage("Розробляю ігрові активи та хочу їх продавати на платформі.");
        pr2.setCreatedAt(ts(-3));

        PermissionRequest pr3 = new PermissionRequest();
        pr3.setUser(newbie);
        pr3.setRequestedPermission(Permission.CAN_FUNDRAISE);
        pr3.setStatus(RequestStatus.PENDING);
        pr3.setMessage("Хочу зібрати кошти на лікування домашньої тварини.");
        pr3.setCreatedAt(ts(-1));

        permissionRequestRepository.saveAll(List.of(pr1, pr2, pr3));
    }

    // --- builders ---

    private User buildUser(String username, String email, String rawPassword,
                            String fullName, Role role, Set<Permission> permissions, int daysOffset) {
        User u = new User();
        u.setUserId(UUID.randomUUID());
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setFullName(fullName);
        u.setRole(role);
        u.setPermissions(permissions);
        u.setCreatedAt(ts(daysOffset));
        return u;
    }

    private UserProfiles buildProfile(User user, String location, String website, String bio, String expertise) {
        UserProfiles p = new UserProfiles();
        p.setUser(user);
        p.setLocation(location);
        p.setWebsite(website);
        p.setBio(bio);
        p.setAreasOfExpertise(expertise);
        return p;
    }

    private Fundraising buildFundraising(String title, String description, String goal,
                                          FundraisingCategory category, User owner,
                                          Fundraising.Status status, int startOffset, int endOffset) {
        Fundraising f = new Fundraising();
        f.setTitle(title);
        f.setDescription(description);
        f.setGoalAmount(new BigDecimal(goal));
        f.setStartDate(ts(startOffset));
        f.setEndDate(ts(endOffset));
        f.setCreatedAt(ts(startOffset));
        f.setStatus(status);
        f.setCategory(category);
        f.setOwner(owner);
        return f;
    }

    private void addDonation(Fundraising f, User donor, String amount, boolean anonymous, int daysOffset) {
        Donation d = new Donation();
        d.setAmount(new BigDecimal(amount));
        d.setTransactionId(UUID.randomUUID().toString());
        d.setCreatedAt(ts(daysOffset));
        d.setIsAnonymous(anonymous);
        d.setDonor(donor);
        d.setFundraising(f);
        f.getDonations().add(d);
    }

    private IntellectualProperty buildIp(String name, String description, String type,
                                          User owner, IntellectualPropertyCategory category,
                                          IpStatus status, int daysOffset) {
        IntellectualProperty ip = new IntellectualProperty();
        ip.setName(name);
        ip.setDescription(description);
        ip.setTypeIp(type);
        ip.setOwner(owner);
        ip.setIntellectualPropertyCategory(category);
        ip.setStatus(status);
        ip.setCreatedAt(ts(daysOffset));
        return ip;
    }

    private UserPost buildPost(String header, String description, String text,
                                User author, PostTagTopic topic, int daysOffset) {
        UserPost p = new UserPost();
        p.setPostHeader(header);
        p.setPostDescription(description);
        p.setPostText(text);
        p.setAuthor(author);
        p.setTopic(topic);
        p.setCreatedAt(ts(daysOffset));
        return p;
    }

    private void addComment(UserPost post, User author, String content, int daysOffset) {
        Comment c = new Comment();
        c.setContent(content);
        c.setAuthor(author);
        c.setCreatedAt(ts(daysOffset));
        c.setPost(post);
        post.getComments().add(c);
    }

    private FundraisingCategory category(String name) {
        FundraisingCategory c = new FundraisingCategory();
        c.setCategoryName(name);
        return c;
    }

    private IntellectualPropertyCategory ipCategory(String name) {
        IntellectualPropertyCategory c = new IntellectualPropertyCategory();
        c.setCategoryName(name);
        return c;
    }

    private PostTagTopic topic(String name) {
        PostTagTopic t = new PostTagTopic();
        t.setPostName(name);
        return t;
    }

    private Timestamp ts(int daysOffset) {
        return Timestamp.from(Instant.now().plus(daysOffset, ChronoUnit.DAYS));
    }
}
