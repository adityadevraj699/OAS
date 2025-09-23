package com.myproject.OAS.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/Admin")
public class AdminController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EnquiryRepository enquiryRepo;

	
	
	@GetMapping("/Dashboard")
	public String showDashboard() {
		if(session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/login";
		}
		return "Admin/Dashboard";
	}
	
	@GetMapping("/NewStudents")
	public String showNewStudents(Model model) {
		if(session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/login";
		}
		List<Users> newStudent = userRepo.findAllByRoleAndStatus(UserRole.STUDENT, UserStatus.PENDING);
		model.addAttribute("newStudent", newStudent);
	    return "Admin/NewStudents";
	}
	
	@GetMapping("/AddStudent")
	public String showAddStudent(Model model) {
		if(session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/login";
		}
		Users student = new Users(); 
		model.addAttribute("student", student);
		return "Admin/AddStudent";
	}
	
	@PostMapping("/AddStudent")
	public String AddStudent(@ModelAttribute("student") Users student, RedirectAttributes attr) {
		if(session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/login";
		}
		try {
			if(userRepo.existsByEmail(student.getEmail())) {
				attr.addFlashAttribute("mgs","User Already Esists"+ student.getEmail()+"!");
				return "redirect:/Admin/AddStudent";
			}
			student.setPassword("Password123");
			student.setRole(UserRole.STUDENT);
			student.setStatus(UserStatus.PENDING);
			student.setRollNo("MIT-"+System.currentTimeMillis());
			student.setRegDate(LocalDateTime.now());
			userRepo.save(student);
			attr.addFlashAttribute("mgs", "Registration Successful, Enrollment No:" +student.getRollNo()+", Password :" + student.getPassword() +" .");
			return "redirect:/Admin/AddStudent";
		} catch (Exception e) {
			attr.addFlashAttribute("mgs", "Error :"+e.getMessage());
			return "redirect:/Admin/AddStudent";
		}
	}
	
	
	@GetMapping("/Enquiry")
	public String showEnquiry(Model model) {
		if(session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/login";
		}
		
		List<Enquiry> enquiries = enquiryRepo.findAll();
		model.addAttribute("enquiries", enquiries);
		return "Admin/Enquiry";
	}
	
	
	
	@GetMapping("/DeleteEnquiry")
	public String DeleteEnquiry(@RequestParam("id") long id) {
		enquiryRepo.deleteById(id);
		return "redirect:/Admin/Enquiry";
		
	}
	
	@GetMapping("/ApproveStudent")
	public String approveStudent(@RequestParam("id") long id, RedirectAttributes attr) {
	    if (session.getAttribute("loggedInAdmin") == null) {
	        return "redirect:/login";
	    }
	    try {
	        Users student = userRepo.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + id));

	        student.setStatus(UserStatus.APPROVED);

	        userRepo.save(student);

	        attr.addFlashAttribute("mgs", "Student " + student.getName() + " approved successfully.");
	    } catch (Exception e) {
	        attr.addFlashAttribute("mgs", "Error approving student: " + e.getMessage());
	    }

	    return "redirect:/Admin/NewStudents";
	}

	
	
	@GetMapping("/ChangePassword")
	public String ShowChangePassword()
	{
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/login";
		}
		return "Admin/ChangePassword";
	}
	
	@PostMapping("/ChangePassword")
	public String changePassword(RedirectAttributes attributes, HttpServletRequest request) {
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		try {
			if (!newPassword.equals(confirmPassword)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not same.");
				return "redirect:/Admin/ChangePassword";
			}
			
			
            Users admin = (Users) session.getAttribute("loggedInAdmin");
			
			if (newPassword.equals(admin.getPassword())) {
				attributes.addFlashAttribute("msg", "New Password and Old Password can not be same.");
				return "redirect:/Admin/ChangePassword";
			}
             if (oldPassword.equals(admin.getPassword())) {
				
				admin.setPassword(confirmPassword);
				userRepo.save(admin);
				session.invalidate();
				attributes.addFlashAttribute("msg", "Password Successfully Channged");
				return "redirect:/login";
			}
			else {
				attributes.addFlashAttribute("msg", "Invalid Old Password!!!");
			}
			return "redirect:/login";
		} catch (Exception e) {
			// TODO: handle exception
			return "redirect:Admin/ChangePassword";
		}
	}
	
	
	@GetMapping("/logout")
	public String logout(RedirectAttributes attributes) {
		session.invalidate();
		attributes.addFlashAttribute("msg", "Successfully Logout!");
		return "redirect:/login";
	}
	
	
	

	
}
