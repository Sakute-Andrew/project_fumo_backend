package com.sakute.project_fumo_backend.domain.enteties.post;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "post_tag_topic")
public class PostTagTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_topic_id")
    private Long postTopicId;

    @Column(name = "post_topic_name", nullable = false)
    private String postName;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

