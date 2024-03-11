package com.sakute.project_fumo_backend.domain.enteties;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "post_tag_topic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTagTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_topic_id")
    private Long postTopicId;

    @Column(name = "post_name")
    private String postName;

    // Додаткові конструктори, гетери та сетери можна додати за потребою
}

