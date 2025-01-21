package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.service.dto.PostTagTopicDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("api/v1/topics")
public class TopicsController {


    @GetMapping
    public ResponseEntity<List<PostTagTopicDto>> getTopicsByName(@RequestParam(required = false, value = "name") String name) {
        return null;
    }

    @GetMapping("/explore")
    public ResponseEntity<List<PostTagTopicDto>>  getAllTopics() {
        return null;
    }


    //Admin Functionality
    @PostMapping("/add-toipic")
    public ResponseEntity<PostTagTopicDto> addTopicToPic(@RequestParam(required = false, value = "topic") String topic) {
        return null;
    }

    @DeleteMapping("/delete-topic")
    public ResponseEntity<Boolean> deleteTopic(@RequestParam(required = false, value = "topic") String topic) {
        return null;
    }

    @PostMapping("edit-topic")
    public ResponseEntity<PostTagTopicDto> editTopic(@RequestParam(required = false, value = "topic") String topic) {
        return null;
    }

}
