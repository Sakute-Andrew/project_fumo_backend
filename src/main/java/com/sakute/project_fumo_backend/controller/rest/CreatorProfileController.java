package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.dto.user_profile.CreatorProfileDTO;
import com.sakute.project_fumo_backend.domain.dto.user_profile.UpdateProfileRequest;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.impl.CreatorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class CreatorProfileController {

    private final CreatorProfileService profileService;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<CreatorProfileDTO> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal User user
    ) {
        profileService.updateProfile(user.getUserId(), request);
        return ResponseEntity.ok().build();
    }
}