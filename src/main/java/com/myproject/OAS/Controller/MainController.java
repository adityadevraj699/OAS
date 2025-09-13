package com.myproject.OAS.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
@ResponseBody
public Map<String,String> submitEnquiry(@RequestParam String name,
                                        @RequestParam String gender,
                                        @RequestParam String contactNo,
                                        @RequestParam String email,
                                        @RequestParam String subject,
                                        @RequestParam String message) {
    Map<String,String> resp = new HashMap<>();
    try {
        Enquiry enquiry = new Enquiry();
        enquiry.setName(name);
        enquiry.setGender(gender);
        enquiry.setContactNo(contactNo);
        enquiry.setEmail(email);
        enquiry.setSubject(subject);
        enquiry.setMessage(message);
        enquiry.setEnquiryDate(LocalDateTime.now());
        enquiryRepository.save(enquiry);

        resp.put("status", "success");
        resp.put("message", "Thank you, " + name + "! Your enquiry has been submitted.");
    } catch (Exception e) {
        e.printStackTrace();
        resp.put("status", "error");
        resp.put("message", "Something went wrong! Please try again.");
    }
    return resp;
}



}
