package com.example.HMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.HMS.model.entities.Role;
import com.example.HMS.model.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);
	
	List<User> findByRole(Role role);
	
	
}