package com.myproject.OAS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.OAS.Model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByEmail(String userID);

	Users findByEmail(String userID);

	boolean existsByRollNo(String userID);

	Users findByRollNo(String userID);


}
