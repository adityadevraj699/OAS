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
import com.myproject.OAS.Model.Users;
import com.myproject.OAS.Model.Users.UserRole;
import com.myproject.OAS.Model.Users.UserStatus;
import com.myproject.OAS.Repository.EnquiryRepository;
import com.myproject.OAS.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private EnquiryRepository enquiryRepository;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }
    
    @GetMapping("/login")
    public String showlogin() {
        return "login";
    }
    
    @PostMapping("/login")
    public String Login(RedirectAttributes attributes, HttpServletRequest request, HttpSession session) 
    {
    	try {
    		
    		String userType = request.getParameter("userType");
    		String userID = request.getParameter("userID");
    		String password = request.getParameter("password");
    		
    		if(userType.equals("ADMIN") && userRepo.existsByEmail(userID)) {
    		
    			Users admin = userRepo.findByEmail(userID);
    			if(password.equals(admin.getPassword()) && admin.getRole().equals(UserRole.ADMIN)) {
    				
    				session.setAttribute("loggedInAdmin", admin);
    				return "redirect:/Admin/Dashboard";
//    				System.out.println(" Valid Admin");
    			}
    			else {
    				attributes.addFlashAttribute("msg", "Invalid User or Password");
    			}
    		}
    		else if (userType.equals("STUDENT") && userRepo.existsByRollNo(userID)) {
        		Users student = userRepo.findByRollNo(userID);
        		if(password.equals(student.getPassword()) && student.getRole().equals(UserRole.STUDENT)) {
        			if( student.getStatus().equals(UserStatus.APPROVED)) {
        				session.setAttribute("loggedInstudent", student);
            			System.out.println(" Valid Student");
            			//return "redirect:/Student/Dashboard";
        			}
        			else if(student.getStatus().equals(UserStatus.PENDING)) {
        				session.setAttribute("NewStudent", student);
        				return "redirect:/Registration";
        			}
        			else if(student.getStatus().equals(UserStatus.DISABLED)) {
        				attributes.addFlashAttribute("msg", "Login DIsabled, Please Contact Admin");
        			}
        			
        			
        		}
        		else {
    				attributes.addFlashAttribute("msg", "Invalid User or Password");
    			}
    		}
    		else {
    			attributes.addFlashAttribute("msg", "User not Found!!");
    		}
    		
			return "redirect:/login";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg",e.getMessage());
			return "redirect:/login";
		}
    }
    
    @GetMapping("/register")
    public String showregister() {
        return "register";
    }
    @GetMapping("/forgot-password")
    public String showforgotPassword() {
        return "forgot-password";
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
