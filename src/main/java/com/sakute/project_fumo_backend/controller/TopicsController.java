package com.sakute.project_fumo_backend.controller;

import com.sakute.project_fumo_backend.domain.enteties.PostTagTopic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopicsController {

    @GetMapping("/search/topics?q={query}")
    public List<PostTagTopic> getTopicsByName(@RequestParam String query) {
        return null;
    }

    @GetMapping("/explore-topics")
    public List<PostTagTopic> getAllTopics() {
        return null;
    }


}
