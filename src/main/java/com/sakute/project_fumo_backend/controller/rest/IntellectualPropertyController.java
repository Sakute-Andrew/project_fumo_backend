package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.service.dto.IntellectualPropertyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class IntellectualPropertyController {

    @GetMapping("/intprop")
    public ResponseEntity<List<IntellectualPropertyDto>> getIntpropList() {
        return null;
    }

    @GetMapping("/intprop?name={name}")
    public ResponseEntity<Optional<IntellectualPropertyDto>> getIntpropByName(
            @RequestParam(value = "name", required = false) String name) {
        return null;
    }
    @GetMapping("/intprop?id={id}")
    public ResponseEntity<IntellectualPropertyDto> getIntpropById(
            @RequestParam(value = "id", required = false)
            UUID id){
        return null;
    }
    @PostMapping("/create")
    public ResponseEntity<IntellectualPropertyDto> create(IntellectualPropertyDto dto) {
        return null;
    }
    @PostMapping("/update")
    public ResponseEntity<IntellectualPropertyDto> update(IntellectualPropertyDto dto) {
        return null;
    }
    @DeleteMapping("/delete")
    public ResponseEntity<IntellectualPropertyDto> delete(Long id) {
        return null;
    }

}
