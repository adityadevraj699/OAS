package com.myproject.OAS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.OAS.Model.Enquiry;
import com.myproject.OAS.Model.Users;
import com.myproject.OAS.Model.Users.UserRole;
import com.myproject.OAS.Model.Users.UserStatus;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

	List<Enquiry> findTop5ByOrderByEnquiryDateDesc();

}
