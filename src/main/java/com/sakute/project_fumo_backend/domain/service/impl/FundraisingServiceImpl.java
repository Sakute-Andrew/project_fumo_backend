package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.service.FundraisingService;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FundraisingServiceImpl extends ServiceGeneric<Fundraising, UUID> implements FundraisingService {

    private FundraisingRepository fundraisingRepository;

    @Autowired
    public FundraisingServiceImpl(FundraisingRepository fundraisingRepository) {
        super(fundraisingRepository);
        this.fundraisingRepository = fundraisingRepository;
    }

    // WARNING bad practice
    public ResponseEntity<List<Fundraising>> getFundraisingList(){
        return Optional.of(fundraisingRepository.findAll())
                .filter(List::isEmpty)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }



}
