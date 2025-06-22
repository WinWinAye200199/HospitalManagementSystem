package com.example.HMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.HMS.model.entities.Doctor;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>{
	
//	Doctor findByName(String name);
	Doctor findByUserId(Long id);
	List<Doctor> findBySpecialization(String specialization);
	List<Doctor> findByDepartmentId(long id);

}
