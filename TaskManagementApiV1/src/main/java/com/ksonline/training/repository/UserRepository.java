package com.ksonline.training.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ksonline.training.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	
	Optional<User> findUserByemail(String email);
	Optional<User> findUserByname(String name);

}
