package com.example.HMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HMS.model.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
	
	Department findByName(String name);

}
