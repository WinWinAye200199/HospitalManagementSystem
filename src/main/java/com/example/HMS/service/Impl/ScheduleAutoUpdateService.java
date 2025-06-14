package com.example.HMS.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.HMS.model.entities.DoctorSchedule;
import com.example.HMS.repository.DoctorScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleAutoUpdateService {

    private final DoctorScheduleRepository doctorScheduleRepository;

    // Runs once a day at midnight (customize as needed)
    @Scheduled(cron = "0 0 0 * * *")
    public void updateExpiredSchedules() {
        LocalDateTime now = LocalDateTime.now();
        List<DoctorSchedule> schedules = doctorScheduleRepository.findByIsAvailableTrue();

        for (DoctorSchedule schedule : schedules) {
            LocalDateTime endDateTime = LocalDateTime.of(schedule.getDate(), schedule.getEndTime());
            if (now.isAfter(endDateTime)) {
                schedule.setAvailable(false);
                doctorScheduleRepository.save(schedule);
            }
        }
    }
}
