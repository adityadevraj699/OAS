package com.myproject.OAS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myproject.OAS.Model.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
}
