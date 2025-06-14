package com.example.HMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.HMS.model.entities.Nurse;
@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long>{
	
	Optional<Nurse> findByUserId(Long id);

}
