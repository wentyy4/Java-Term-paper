package com.medireminder.repository;

import com.medireminder.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByMedicationUserUsername(String username);
}
