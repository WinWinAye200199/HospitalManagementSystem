package com.example.HMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.HMS.model.entities.Staff;
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>{

}
