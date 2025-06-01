package com.medireminder.service;

import com.medireminder.dto.MedicationDto;
import com.medireminder.entity.Medication;
import com.medireminder.entity.User;
import com.medireminder.repository.MedicationRepository;
import com.medireminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class MedicationService {
    @Autowired
    private MedicationRepository repo;
    @Autowired
    private UserRepository userRepo;

    public void addMedication(MedicationDto dto, String username) {
        Medication med = new Medication();
        med.setName(dto.name);
        med.setDosage(dto.dosage);
        med.setTimeToTake(LocalTime.parse(dto.timeToTake));

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        med.setUser(user);

        repo.save(med);
    }

    public List<Medication> getMedications(String username) {
        return repo.findByUserUsername(username);
    }
}
