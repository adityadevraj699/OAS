package com.myproject.OAS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.OAS.Model.Users;
import com.myproject.OAS.Model.Users.UserRole;
import com.myproject.OAS.Model.Users.UserStatus;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByEmail(String userID);

	Users findByEmail(String userID);

	boolean existsByRollNo(String userID);

	Users findByRollNo(String userID);

	List<Users> findAllByRoleAndStatus(UserRole student, UserStatus pending);


	List<Users> findAllByRoleAndStatusOrStatus(UserRole student, UserStatus approved, UserStatus disabled);

	long countByRole(UserRole student);

	long countByRoleAndStatus(UserRole student, UserStatus pending);


}
