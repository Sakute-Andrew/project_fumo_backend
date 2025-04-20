package com.sakute.project_fumo_backend.controller.rest;


import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/fundraising")
public class FundraisingController {

    @GetMapping
    public ResponseEntity<List<Fundraising>> getFundraising(){
        return null;
    }
}
