package com.example.HMS.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HMS.model.entities.DoctorSchedule;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long>{

	DoctorSchedule findByDoctorId(Long id);
	
	DoctorSchedule findByDoctorIdAndDate(Long doctorId, LocalDate date);
	
	List<DoctorSchedule> findAllByDoctorId(Long doctorId);
	
	List<DoctorSchedule> findByIsAvailableTrue();

}
