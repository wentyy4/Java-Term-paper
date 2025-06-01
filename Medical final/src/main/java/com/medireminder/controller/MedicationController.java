package com.medireminder.controller;

import com.medireminder.entity.Medication;
import com.medireminder.entity.User;
import com.medireminder.repository.MedicationRepository;
import com.medireminder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Medication>> getUserMedications(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(401).build();
        }

        String username = jwt.getClaimAsString("sub");
        List<Medication> medications = medicationRepository.findByUserUsername(username);
        return ResponseEntity.ok(medications);
    }

    @PostMapping
    public ResponseEntity<?> addMedication(
            @RequestBody Medication medication,
            @AuthenticationPrincipal Jwt jwt) {

        if (jwt == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String username = jwt.getClaimAsString("sub");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        medication.setUser(user);

        medicationRepository.save(medication);
        return ResponseEntity.ok(medication);
    }
}
