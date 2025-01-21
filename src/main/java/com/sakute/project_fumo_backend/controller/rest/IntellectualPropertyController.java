package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.service.dto.IntellectualPropertyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController("api/v1/ips")
public class IntellectualPropertyController {

    public ResponseEntity<List<IntellectualPropertyDto>> getIpList(){
        return null;
    }

    public ResponseEntity<Optional<IntellectualPropertyDto>> getIpbyName(String name){
        return null;
    }

    public ResponseEntity<IntellectualPropertyDto> getIpById(UUID id){
        return null;
    }

    public ResponseEntity<IntellectualPropertyDto> create(IntellectualPropertyDto dto) {
        return null;
    }
    public ResponseEntity<IntellectualPropertyDto> update(IntellectualPropertyDto dto) {
        return null;
    }
    public ResponseEntity<IntellectualPropertyDto> delete(Long id) {
        return null;
    }

}
