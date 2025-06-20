package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.enteties.dto.IntellectualPropertyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<IntellectualPropertyDto> create(IntellectualPropertyDto dto) {
        return null;
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<IntellectualPropertyDto> update(IntellectualPropertyDto dto) {
        return null;
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or @intprop")
    public ResponseEntity<IntellectualPropertyDto> delete(Long id) {
        return null;
    }

}
