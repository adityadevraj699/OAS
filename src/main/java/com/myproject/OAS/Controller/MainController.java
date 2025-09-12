package com.myproject.OAS.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.OAS.Model.Enquiry;
import com.myproject.OAS.Repository.EnquiryRepository;

@Controller
public class MainController {

    @Autowired
    private EnquiryRepository enquiryRepository;

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/ContactUs")
    public String showContactUs() {
        return "contactus";
    }

   @PostMapping("/submit-enquiry")
public String submitEnquiry(
        @RequestParam String name,
        @RequestParam String gender,
        @RequestParam String contactNo,
        @RequestParam String email,
        @RequestParam String subject,
        @RequestParam String message,
        RedirectAttributes redirectAttributes) {

    try {
        Enquiry enquiry = new Enquiry();
        enquiry.setName(name);
        enquiry.setGender(gender);
        enquiry.setContactNo(contactNo);
        enquiry.setEmail(email);
        enquiry.setSubject(subject);
        enquiry.setMessage(message);
        enquiry.setEnquiryDate(LocalDateTime.now());

        // Save to database
        enquiryRepository.save(enquiry);

        // Success message -> flash attribute
        redirectAttributes.addFlashAttribute("success", 
            "Thank you, " + name + "! Your enquiry has been submitted.");
    } catch (Exception e) {
        e.printStackTrace(); 
        redirectAttributes.addFlashAttribute("error", 
            "Something went wrong! Please try again.");
    }

    
    return "redirect:/ContactUs";
}

}
