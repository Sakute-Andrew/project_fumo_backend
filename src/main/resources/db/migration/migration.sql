-- Sequences
CREATE SEQUENCE public.post_tag_topic_post_topic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tables
CREATE TABLE public.comment (
                                comment_id bigint PRIMARY KEY NOT NULL,
                                content text NOT NULL,
                                user_id bigint NOT NULL,
                                created_at timestamp without time zone NOT NULL,
                                user_post_id bigint NOT NULL,
                                user_post_comment_id bigint,
                                comment_post_id bigint NOT NULL,
                                FOREIGN KEY (comment_post_id) REFERENCES public.user_post (user_post_id)
                                    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.donations (
                                  donation_id bigint PRIMARY KEY NOT NULL,
                                  fundraising_id bigint NOT NULL,
                                  donor_id bigint NOT NULL,
                                  amount numeric(10,2) NOT NULL,
                                  donation_date timestamp without time zone NOT NULL,
                                  payment_method character varying(50) NOT NULL,
                                  anonymous boolean NOT NULL DEFAULT false,
                                  FOREIGN KEY (donor_id) REFERENCES public."user" (user_id)
                                      MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                  FOREIGN KEY (fundraising_id) REFERENCES public.fundraising (fundraising_id)
                                      MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.fundraising (
                                    fundraising_id bigint PRIMARY KEY NOT NULL,
                                    title character varying(255) NOT NULL,
                                    description text NOT NULL,
                                    goal_amount numeric(10,2) NOT NULL,
                                    current_amount numeric(10,2) NOT NULL DEFAULT 0,
                                    start_date timestamp without time zone NOT NULL,
                                    end_date timestamp without time zone NOT NULL,
                                    user_id bigint NOT NULL,
                                    created_at timestamp without time zone NOT NULL,
                                    category bigint NOT NULL,
                                    FOREIGN KEY (category) REFERENCES public.fundraising_category (category_id)
                                        MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                    FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                        MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.fundraising_category (
                                             category_id bigint PRIMARY KEY NOT NULL,
                                             category_name character varying
);

CREATE TABLE public.fundraising_comments (
                                             comment_id bigint PRIMARY KEY NOT NULL,
                                             user_id bigint NOT NULL,
                                             fundraising_id bigint NOT NULL,
                                             content text NOT NULL,
                                             created_at timestamp without time zone NOT NULL,
                                             FOREIGN KEY (fundraising_id) REFERENCES public.fundraising (fundraising_id)
                                                 MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                             FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                                 MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.fundraising_goals (
                                          goal_id bigint PRIMARY KEY NOT NULL,
                                          fundraising_id bigint NOT NULL,
                                          target_amount numeric(10,2) NOT NULL,
                                          description text,
                                          achieved boolean NOT NULL DEFAULT false,
                                          FOREIGN KEY (fundraising_id) REFERENCES public.fundraising (fundraising_id)
                                              MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.fundraising_likes (
                                          like_id bigint PRIMARY KEY NOT NULL,
                                          user_id bigint NOT NULL,
                                          fundraising_id bigint NOT NULL,
                                          created_at timestamp without time zone NOT NULL,
                                          FOREIGN KEY (fundraising_id) REFERENCES public.fundraising (fundraising_id)
                                              MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                          FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                              MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.fundraising_updates (
                                            update_id bigint PRIMARY KEY NOT NULL,
                                            fundraising_id bigint NOT NULL,
                                            content text NOT NULL,
                                            update_date timestamp without time zone NOT NULL,
                                            FOREIGN KEY (fundraising_id) REFERENCES public.fundraising (fundraising_id)
                                                MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.intellectual_property (
                                              ip_id bigint PRIMARY KEY NOT NULL,
                                              name character varying(255) NOT NULL,
                                              description character varying,
                                              type_ip character varying(50) NOT NULL,
                                              owner_id bigint,
                                              created_at timestamp without time zone NOT NULL,
                                              price numeric(10,2),
                                              category bigint NOT NULL,
                                              file_ip bytea,
                                              status character varying(50) NOT NULL,
                                              FOREIGN KEY (category) REFERENCES public.intellectual_property_category (category_id)
                                                  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                              FOREIGN KEY (owner_id) REFERENCES public."user" (user_id)
                                                  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.intellectual_property_category (
                                                       category_id bigint PRIMARY KEY NOT NULL,
                                                       category_name character varying NOT NULL
);

CREATE TABLE public.intellectual_property_comments (
                                                       comment_id bigint PRIMARY KEY NOT NULL,
                                                       user_id bigint NOT NULL,
                                                       ip_id bigint NOT NULL,
                                                       content text NOT NULL,
                                                       created_at timestamp without time zone NOT NULL,
                                                       FOREIGN KEY (ip_id) REFERENCES public.intellectual_property (ip_id)
                                                           MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                                       FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                                           MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.intellectual_property_likes (
                                                    like_id bigint PRIMARY KEY NOT NULL,
                                                    user_id bigint NOT NULL,
                                                    ip_id bigint NOT NULL,
                                                    created_at timestamp without time zone NOT NULL,
                                                    FOREIGN KEY (ip_id) REFERENCES public.intellectual_property (ip_id)
                                                        MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                                    FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                                        MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.licensing_agreements (
                                             agreement_id bigint PRIMARY KEY NOT NULL,
                                             ip_id bigint NOT NULL,
                                             license_type character varying(50) NOT NULL,
                                             terms text NOT NULL,
                                             start_date timestamp without time zone NOT NULL,
                                             end_date timestamp without time zone,
                                             FOREIGN KEY (ip_id) REFERENCES public.intellectual_property (ip_id)
                                                 MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.orders (
                               order_id bigint PRIMARY KEY NOT NULL,
                               customer_id bigint NOT NULL,
                               ip_id bigint NOT NULL,
                               order_date timestamp without time zone NOT NULL,
                               total_amount numeric(10,2) NOT NULL,
                               order_status character varying(50) NOT NULL,
                               FOREIGN KEY (customer_id) REFERENCES public."user" (user_id)
                                   MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                               FOREIGN KEY (ip_id) REFERENCES public.intellectual_property (ip_id)
                                   MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.post_tag_topic (
                                       post_topic_id bigint PRIMARY KEY NOT NULL DEFAULT nextval('post_tag_topic_post_topic_id_seq'::regclass),
                                       post_name character varying
);

CREATE TABLE public.reporting (
                                  report_id bigint PRIMARY KEY NOT NULL,
                                  report_name character varying(100) NOT NULL,
                                  report_type character varying(50) NOT NULL,
                                  start_period timestamp without time zone NOT NULL,
                                  end_period timestamp without time zone NOT NULL,
                                  report_data jsonb,
                                  generated_at timestamp without time zone NOT NULL
);

CREATE TABLE public.role (
                             role_id bigint PRIMARY KEY NOT NULL,
                             role_name character varying(50) NOT NULL
);

CREATE TABLE public."user" (
                               user_id bigint PRIMARY KEY NOT NULL,
                               username character varying(254) NOT NULL,
                               email character varying(254) NOT NULL,
                               role bigint NOT NULL,
                               last_login_datetime timestamp without time zone NOT NULL,
                               created_at timestamp without time zone NOT NULL,
                               FOREIGN KEY (role) REFERENCES public.role (role_id)
                                   MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.user_post (
                                  user_post_id bigint PRIMARY KEY NOT NULL,
                                  user_id bigint NOT NULL,
                                  photo bytea,
                                  post_header character(300) NOT NULL,
                                  post_description character(1),
                                  created_at timestamp without time zone NOT NULL,
                                  modified_at timestamp without time zone NOT NULL,
                                  post_topic bigint,
                                  FOREIGN KEY (post_topic) REFERENCES public.post_tag_topic (post_topic_id)
                                      MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                  FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                      MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.user_post_like (
                                       like_id bigint PRIMARY KEY NOT NULL,
                                       user_id bigint NOT NULL,
                                       user_post_id bigint NOT NULL,
                                       created_at timestamp without time zone NOT NULL,
                                       FOREIGN KEY (user_post_id) REFERENCES public.user_post (user_post_id)
                                           MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
                                       FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                           MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.user_profiles (
                                      user_id bigint PRIMARY KEY NOT NULL,
                                      bio text,
                                      areas_of_expertise text,
                                      profile_picture bytea,
                                      website character varying(255),
                                      location character varying(255),
                                      FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                          MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.user_settings (
                                      user_id bigint PRIMARY KEY NOT NULL,
                                      email_notifications boolean NOT NULL DEFAULT true,
                                      marketing_opt_in boolean NOT NULL DEFAULT false,
                                      language character varying(50),
                                      timezone character varying(50),
                                      theme character varying(50),
                                      accessibility_options text,
                                      FOREIGN KEY (user_id) REFERENCES public."user" (user_id)
                                          MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
