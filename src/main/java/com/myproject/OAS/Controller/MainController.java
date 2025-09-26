package com.myproject.OAS.Controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    
    @Autowired
	private HttpSession session;
    
    @Autowired
    private Cloudinary cloudinary;


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
        				session.setAttribute("loggedInStudent", student);
            			//System.out.println(" Valid Student");
            			return "redirect:/Student/Dashboard";
        			}
        			else if(student.getStatus().equals(UserStatus.PENDING)) {
        				session.setAttribute("NewStudent", student);
        				return "redirect:register";
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
    public String showRegister(Model model) {
        if (session.getAttribute("NewStudent") == null) {
            return "redirect:/login";
        }

        Users student = (Users) session.getAttribute("NewStudent");
        if(student.getUtrNo()!=null) {
        	return "redirect:/Successful";
        }
        model.addAttribute("student", student);
        return "register";
    }

   @PostMapping("/register")
public String submitRegister(HttpServletRequest request,
                             @RequestParam("paymentImage") MultipartFile file,
                             Model model, RedirectAttributes attributes) {

    // Fetch session student
    Users existingStudent = (Users) session.getAttribute("NewStudent");
    if (existingStudent == null) {
        return "redirect:/login";
    }

    Users user = existingStudent;

    try {
        // Update fields from form
        user.setName(request.getParameter("name"));
        user.setProgram(request.getParameter("program"));
        user.setBranch(request.getParameter("branch"));
        user.setEmail(request.getParameter("email"));
        user.setContactNo(request.getParameter("contactNo"));
        user.setFatherName(request.getParameter("fatherName"));
        user.setMotherName(request.getParameter("motherName"));
        user.setAddress(request.getParameter("address"));
        user.setUtrNo(request.getParameter("utrNo"));
        user.setGender(request.getParameter("gender"));
        user.setYear(request.getParameter("year")); // readonly

        // Handle file upload
        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "payment_images"));
            String imageUrl = uploadResult.get("secure_url").toString();
            user.setPaymentImage(imageUrl);
        }

        // Save the updated user
        userRepo.save(user);

        // Update session
        session.setAttribute("NewStudent", user);

        // Success message (optional)
        attributes.addAttribute("msg", "Registration completed successfully!");
        return "redirect:/Successful";

    } catch (Exception e) {
        e.printStackTrace();
        // Error message to display in form
        attributes.addAttribute("msg", "Something went wrong during registration. Please try again.");
        model.addAttribute("student", user); // keep form filled
        return "register"; // return back to form page
    }
}


   @GetMapping("/Successful")
   public String showSuccessful(HttpSession session) {
	   if(session.getAttribute("NewStudent") == null) {
		   return "redirect:/login";
	   }
	   return "Successful";
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
