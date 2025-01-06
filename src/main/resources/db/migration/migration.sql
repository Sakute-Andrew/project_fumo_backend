-- Sequences
CREATE SEQUENCE IF NOT EXISTS public.post_tag_topic_post_topic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tables
CREATE TABLE IF NOT EXISTS role (
                                    role_id   BIGSERIAL NOT NULL
                                        CONSTRAINT role_pk
                                            PRIMARY KEY,
                                    role_name VARCHAR(50) NOT NULL
);

ALTER TABLE role
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS user_t (
                                      user_id             uuid NOT NULL
                                          CONSTRAINT user_t_pk
                                              PRIMARY KEY,
                                      username            VARCHAR(254) NOT NULL,
                                      email               VARCHAR(254) NOT NULL,
                                      role                BIGINT NOT NULL
                                          CONSTRAINT user_t_role_role_id_fk
                                              REFERENCES role ON DELETE CASCADE,
                                      last_login_datetime TIMESTAMP NOT NULL,
                                      created_at          TIMESTAMP NOT NULL

);

ALTER TABLE user_t
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS post_tag_topic (
                                              post_topic_id BIGSERIAL NOT NULL
                                                  CONSTRAINT post_tag_topic_pk
                                                      PRIMARY KEY,
                                              post_topic_name     VARCHAR(50)
);


ALTER TABLE post_tag_topic
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS user_post (
                                         user_post_id     uuid NOT NULL
                                             CONSTRAINT user_post_pk
                                                 PRIMARY KEY,
                                         user_id          uuid NOT NULL
                                             CONSTRAINT user_post_user_user_id_fk
                                                 REFERENCES user_t ON DELETE CASCADE,
                                         photo            BYTEA,
                                         post_header      VARCHAR(300) NOT NULL,
                                         post_description VARCHAR(255),
                                         created_at       TIMESTAMP NOT NULL,
                                         modified_at      TIMESTAMP NOT NULL,
                                         post_topic       BIGINT NOT NULL
                                             CONSTRAINT user_post_post_tag_topic_post_topic_id_fk
                                                 REFERENCES post_tag_topic ON DELETE CASCADE
);

ALTER TABLE user_post
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS user_post_like (
                                              like_id      BIGSERIAL NOT NULL
                                                  CONSTRAINT user_post_like_pk
                                                      PRIMARY KEY,
                                              user_id      uuid NOT NULL
                                                  CONSTRAINT user_post_like_user_user_id_fk
                                                      REFERENCES user_t ON DELETE CASCADE,
                                              user_post_id uuid NOT NULL
                                                  CONSTRAINT user_post_like_user_post_user_post_id_fk
                                                      REFERENCES user_post ON DELETE CASCADE,
                                              created_at   TIMESTAMP NOT NULL
);

ALTER TABLE user_post_like
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS comment (
                                       comment_id           BIGSERIAL NOT NULL
                                           CONSTRAINT comment_pk
                                               PRIMARY KEY,

                                       user_post_id         uuid NOT NULL
                                           CONSTRAINT comment_user_post_user_post_id_fk
                                               REFERENCES user_post ON DELETE CASCADE,
                                       user_id              uuid NOT NULL
                                           CONSTRAINT comment_user_user_id_fk
                                               REFERENCES user_t ON DELETE CASCADE,
                                       content              VARCHAR(255) NOT NULL,
                                       created_at           TIMESTAMP NOT NULL


);

ALTER TABLE comment
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS intellectual_property_category (
                                                              category_id   BIGSERIAL NOT NULL
                                                                  CONSTRAINT intellectual_property_category_pk
                                                                      PRIMARY KEY,
                                                              category_name VARCHAR(50) NOT NULL
);

ALTER TABLE intellectual_property_category
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS intellectual_property (
                                                     ip_id        uuid NOT NULL
                                                         CONSTRAINT ip_pk
                                                             PRIMARY KEY,
                                                     name        VARCHAR(255) NOT NULL,
                                                     description VARCHAR,
                                                     type_ip     VARCHAR(50) NOT NULL,
                                                     user_id    uuid NOT NULL
                                                         CONSTRAINT ip_owner_user_id_fk
                                                             REFERENCES user_t ON DELETE SET NULL,
                                                     created_at  TIMESTAMP NOT NULL,
                                                     price       NUMERIC(38, 2),
                                                     category_id    BIGINT NOT NULL
                                                         CONSTRAINT intellectual_id_fk
                                                             REFERENCES intellectual_property_category ON DELETE CASCADE,
                                                     file_ip     BYTEA,
                                                     status      VARCHAR(50) NOT NULL
);

ALTER TABLE intellectual_property
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS fundraising_category (
                                                    category_id   BIGSERIAL NOT NULL
                                                        CONSTRAINT fundraising_category_pk
                                                            PRIMARY KEY,
                                                    category_name VARCHAR(50) NOT NULL
);

ALTER TABLE fundraising_category
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS fundraising (
                                           fundraising_id  uuid NOT NULL
                                               CONSTRAINT fundraising_pk
                                                   PRIMARY KEY,
                                           user_id         uuid NOT NULL
                                               CONSTRAINT fundraising_user_user_id_fk
                                                   REFERENCES user_t ON DELETE CASCADE,
                                           category_id      BIGINT NOT NULL
                                               CONSTRAINT fundraising_fundraising_category_category_id_fk
                                                   REFERENCES fundraising_category ON DELETE CASCADE,
                                           title          VARCHAR(255) NOT NULL,
                                           description    VARCHAR(255) NOT NULL,
                                           goal_amount    NUMERIC(38, 2) NOT NULL,
                                           current_amount NUMERIC(38, 2) DEFAULT 0 NOT NULL,
                                           start_date     TIMESTAMP NOT NULL,
                                           end_date       TIMESTAMP NOT NULL,
                                           created_at     TIMESTAMP NOT NULL

);

ALTER TABLE fundraising
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS fundraising_likes (
                                                 like_id        BIGSERIAL
                                                     CONSTRAINT fundraising_likes_pk
                                                         PRIMARY KEY,
                                                 user_id        uuid NOT NULL
                                                     CONSTRAINT fundraising_likes_user_user_id_fk
                                                         REFERENCES user_t ON DELETE CASCADE,
                                                 fundraising_id  uuid NOT NULL
                                                     CONSTRAINT fundraising_likes_fundraising_fundraising_id_fk
                                                         REFERENCES fundraising ON DELETE CASCADE,
                                                 created_at     TIMESTAMP NOT NULL
);

ALTER TABLE fundraising_likes
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS fundraising_comments (
                                                    comment_id     BIGSERIAL
                                                        CONSTRAINT fundraising_comments_pk
                                                            PRIMARY KEY,
                                                    user_id         uuid NOT NULL
                                                        CONSTRAINT fundraising_comments_user_user_id_fk
                                                            REFERENCES user_t ON DELETE CASCADE,
                                                    fundraising_id  uuid NOT NULL
                                                        CONSTRAINT fundraising_comments_fundraising_fundraising_id_fk
                                                            REFERENCES fundraising ON DELETE CASCADE,
                                                    content        VARCHAR(255) NOT NULL,
                                                    created_at     TIMESTAMP NOT NULL
);

ALTER TABLE fundraising_comments
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS intellectual_property_likes (
                                                           like_id    BIGSERIAL
                                                               CONSTRAINT intellectual_property_likes_pk
                                                                   PRIMARY KEY,
                                                           user_id     uuid NOT NULL
                                                               CONSTRAINT intellectual_property_likes_user_user_id_fk
                                                                   REFERENCES user_t ON DELETE CASCADE,
                                                           ip_id       uuid NOT NULL
                                                               CONSTRAINT intellectual_property_likes_intellectual_property_ip_id_fk
                                                                   REFERENCES intellectual_property ON DELETE CASCADE,
                                                           created_at TIMESTAMP NOT NULL
);

ALTER TABLE intellectual_property_likes
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS intellectual_property_comments (
                                                              comment_id BIGSERIAL
                                                                  CONSTRAINT intellectual_property_comments_pk
                                                                      PRIMARY KEY,
                                                              user_id     uuid NOT NULL
                                                                  CONSTRAINT intellectual_property_comments_user_user_id_fk
                                                                      REFERENCES user_t ON DELETE CASCADE,
                                                              ip_id       uuid NOT NULL
                                                                  CONSTRAINT intellectual_property_comments_intellectual_property_ip_id_fk
                                                                      REFERENCES intellectual_property ON DELETE CASCADE,
                                                              content    VARCHAR(255) NOT NULL,
                                                              created_at TIMESTAMP NOT NULL
);

ALTER TABLE intellectual_property_comments
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS user_profiles (
                                             user_settings_id    BIGSERIAL
                                                 CONSTRAINT user_profiles_pk
                                                     PRIMARY KEY,
                                             user_id             uuid NOT NULL
                                                 CONSTRAINT user_profiles_user_user_id_fk
                                                     REFERENCES user_t ON DELETE CASCADE,
                                             bio                VARCHAR(255),
                                             areas_of_expertise VARCHAR(255),
                                             profile_picture    BYTEA,
                                             website            VARCHAR(255),
                                             location           VARCHAR(255)
);

ALTER TABLE user_profiles
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS orders (
                                      order_id     uuid NOT NULL
                                          CONSTRAINT orders_pk
                                              PRIMARY KEY,
                                      customer_id  uuid NOT NULL
                                          CONSTRAINT orders_user_user_id_fk
                                              REFERENCES user_t ON DELETE CASCADE,
                                      ip_id        uuid NOT NULL
                                          CONSTRAINT orders_intellectual_property_ip_id_fk
                                              REFERENCES intellectual_property ON DELETE CASCADE,
                                      order_date   TIMESTAMP NOT NULL,
                                      total_amount NUMERIC(38, 2) NOT NULL,
                                      order_status VARCHAR(50) NOT NULL
);

ALTER TABLE orders
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS licensing_agreements (
                                                    agreement_id uuid NOT NULL
                                                        CONSTRAINT licensing_agreements_pk
                                                            PRIMARY KEY,
                                                    ip_id          uuid NOT NULL
                                                        CONSTRAINT licensing_agreements_intellectual_property_ip_id_fk
                                                            REFERENCES intellectual_property ON DELETE CASCADE,
                                                    license_type VARCHAR(50) NOT NULL,
                                                    terms        VARCHAR(50) NOT NULL,
                                                    start_date   TIMESTAMP NOT NULL,
                                                    end_date     TIMESTAMP NOT NULL
);

ALTER TABLE licensing_agreements
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS reporting (
                                         report_id    uuid NOT NULL
                                             CONSTRAINT reporting_pk
                                                 PRIMARY KEY,
                                         report_name  VARCHAR(100) NOT NULL,
                                         report_type  VARCHAR(50) NOT NULL,
                                         start_period TIMESTAMP NOT NULL,
                                         end_period   TIMESTAMP NOT NULL,
                                         report_data  JSONB,
                                         generated_at TIMESTAMP NOT NULL
);

ALTER TABLE reporting
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS donations (
                                         donation_id    Bigint NOT NULL
                                             CONSTRAINT donations_pk
                                                 PRIMARY KEY,
                                         fundraising_id uuid NOT NULL
                                             CONSTRAINT donations_fundraising_fundraising_id_fk
                                                 REFERENCES fundraising ON DELETE CASCADE,
                                         user_id       uuid NOT NULL
                                             CONSTRAINT donations_user_user_id_fk
                                                 REFERENCES user_t ON DELETE CASCADE,
                                         amount         NUMERIC(38, 2) NOT NULL,
                                         donation_date  TIMESTAMP NOT NULL,
                                         payment_method VARCHAR(50) NOT NULL,
                                         anonymous      BOOLEAN DEFAULT FALSE NOT NULL
);

ALTER TABLE donations
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS fundraising_updates (
                                                   update_id      BIGSERIAL
                                                       CONSTRAINT fundraising_updates_pk
                                                           PRIMARY KEY,
                                                   fundraising_id uuid NOT NULL
                                                       CONSTRAINT fundraising_updates_fundraising_fundraising_id_fk
                                                           REFERENCES fundraising ON DELETE CASCADE,
                                                   content        VARCHAR(255) NOT NULL,
                                                   update_date    TIMESTAMP NOT NULL
);

ALTER TABLE fundraising_updates
    OWNER TO postgres;

CREATE TABLE fundraising_goals (
                                   goal_id        BIGSERIAL
                                       CONSTRAINT fundraising_goals_pk
                                           PRIMARY KEY,
                                   fundraising_id uuid NOT NULL
                                       CONSTRAINT fundraising_goals_fundraising_fundraising_id_fk
                                           REFERENCES fundraising ON DELETE CASCADE,
                                   target_amount  DOUBLE PRECISION NOT NULL,
                                   description    VARCHAR(255) NOT NULL,
                                   achieved       BOOLEAN DEFAULT FALSE NOT NULL
);

ALTER TABLE fundraising_goals
    OWNER TO postgres;

CREATE TABLE user_settings (
                               user_settings_id      uuid NOT NULL
                                   CONSTRAINT user_settings_pk
                                       PRIMARY KEY,
                               user_id               uuid NOT NULL
                                   CONSTRAINT user_settings_user_user_id_fk
                                       REFERENCES user_t ON DELETE CASCADE,
                               email_notifications   BOOLEAN DEFAULT TRUE NOT NULL,
                               marketing_opt_in      BOOLEAN DEFAULT FALSE NOT NULL,
                               language              VARCHAR(50),
                               timezone              VARCHAR(50),
                               theme                 VARCHAR(50),
                               accessibility_options VARCHAR(50)
);

ALTER TABLE user_settings
    OWNER TO postgres;


