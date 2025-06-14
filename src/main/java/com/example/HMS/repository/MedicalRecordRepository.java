package com.example.HMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HMS.model.entities.MedicalRecord;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long>{
	
	List<MedicalRecord> findByPatientId(long id);

}
