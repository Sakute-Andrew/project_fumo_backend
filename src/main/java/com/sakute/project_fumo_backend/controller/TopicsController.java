package com.sakute.project_fumo_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopicsController {


    @GetMapping("/topics?q={query}")
    public ResponseEntity<List<?>> getTopicsByName(@RequestParam String query) {
        return null;
    }

    @GetMapping("/explore-topics")
    public ResponseEntity<List<?>>  getAllTopics() {
        return null;
    }


}
