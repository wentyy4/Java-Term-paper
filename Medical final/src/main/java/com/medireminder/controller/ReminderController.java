package com.medireminder.controller;

import com.medireminder.entity.Reminder;
import com.medireminder.entity.Medication;
import com.medireminder.repository.MedicationRepository;
import com.medireminder.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderRepository reminderRepository;
    private final MedicationRepository medicationRepository;

    @GetMapping
    public ResponseEntity<List<Reminder>> getUserReminders(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(401).build();
        }

        String username = jwt.getClaimAsString("sub");
        List<Reminder> reminders = reminderRepository.findByMedicationUserUsername(username);
        return ResponseEntity.ok(reminders);
    }

    @PostMapping
    public ResponseEntity<?> createReminder(
            @RequestParam("medicationId") Long medicationId,
            @RequestParam("time") String time,
            @RequestParam(name = "active", defaultValue = "true") boolean active) {

        Optional<Medication> medicationOpt = medicationRepository.findById(medicationId);
        if (medicationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Medication not found");
        }

        Reminder reminder = Reminder.builder()
                .medication(medicationOpt.get())
                .time(LocalTime.parse(time))
                .active(active)
                .build();

        reminderRepository.save(reminder);
        return ResponseEntity.ok(reminder);
    }
}
