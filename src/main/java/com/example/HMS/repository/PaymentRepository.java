package com.example.HMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.HMS.model.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

	List<Payment> findByPatientId(Long patientId);
}
