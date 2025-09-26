package com.myproject.OAS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.OAS.Model.StudyMaterial.MaterialType;
import com.myproject.OAS.Model.StudyMaterial;
import com.myproject.OAS.Model.Users;
import com.myproject.OAS.Repository.StudyMaterialRepository;
import com.myproject.OAS.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Student")
public class StudentController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StudyMaterialRepository materialRepo;

	@GetMapping("/Dashboard")
	public String showDashboard(Model model) {
		if(session.getAttribute("loggedInStudent") == null) {
		    return "redirect:/login";
		}
		
		Users student = (Users) session.getAttribute("loggedInStudent");
		long assignment = materialRepo.countByMaterialTypeAndProgramAndBranchAndYear(MaterialType.Assignment, student.getProgram(), student.getBranch(), student.getYear());
		long studyMaterial = materialRepo.countByMaterialTypeAndProgramAndBranchAndYear(MaterialType.Study_Material, student.getProgram(), student.getBranch(), student.getYear());
		
		model.addAttribute("student", student);
		model.addAttribute("assignments", assignment);
		model.addAttribute("studyMaterial", studyMaterial);
		return "Student/Dashboard";
	}
	
	
	@GetMapping("/StudyMaterial")
	public String showStudyMaterial(Model model) {
		if(session.getAttribute("loggedInStudent") == null) {
		    return "redirect:/login";
		}
        Users student = (Users) session.getAttribute("loggedInStudent");
		
		List<StudyMaterial> studyMaterials = materialRepo.findAllByMaterialTypeAndProgramAndBranchAndYear(MaterialType.Study_Material, student.getProgram(), student.getBranch(), student.getYear());				
		model.addAttribute("studyMaterials", studyMaterials);

		return "Student/StudyMaterial";
	}
	
	
	@GetMapping("/Assignment")
	public String showAssignment(Model model) {
	    if(session.getAttribute("loggedInStudent") == null) {
	        return "redirect:/login";
	    }

	    Users student = (Users) session.getAttribute("loggedInStudent");

	    List<StudyMaterial> assignments = materialRepo
	        .findAllByMaterialTypeAndProgramAndBranchAndYear(
	            StudyMaterial.MaterialType.Assignment,
	            student.getProgram(),
	            student.getBranch(),
	            student.getYear()
	        );				

	    model.addAttribute("assignments", assignments); // ✅ same as Thymeleaf
	    return "Student/Assignment";
	}

	
	
	@GetMapping("/Profile")
	public String showProfile(Model model) {
		if(session.getAttribute("loggedInStudent") == null) {
		    return "redirect:/login";
		}

		Users student = (Users) session.getAttribute("loggedInStudent");
		model.addAttribute("student", student);
		return "Student/Profile";
	}
	
	@GetMapping("/ChangePassword")
	public String showChangePassword() {
		if(session.getAttribute("loggedInStudent") == null) {
		    return "redirect:/login";
		}

		return "Student/ChangePassword";
	}
	
	
	@PostMapping("/ChangePassword")
	public String changePassword(RedirectAttributes attributes, HttpServletRequest request) {
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		try {
			if (!newPassword.equals(confirmPassword)) {
				attributes.addFlashAttribute("msg", "New Password and Confirm Password are not same.");
				return "redirect:/Student/ChangePassword";
			}
			
			
            Users student = (Users) session.getAttribute("loggedInStudent");
			
			if (newPassword.equals(student.getPassword())) {
				attributes.addFlashAttribute("msg", "New Password and Old Password can not be same.");
				return "redirect:/Student/ChangePassword";
			}
             if (oldPassword.equals(student.getPassword())) {
				
            	 student.setPassword(confirmPassword);
				userRepo.save(student);
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
			return "redirect:/Student/ChangePassword";
		}
	}
	
	
	
	
	
	
	@GetMapping("/logout")
	public String logout(RedirectAttributes attributes) {
		session.invalidate();
		attributes.addFlashAttribute("msg", "Successfully Logout!");
		return "redirect:/login";
	}
	
	
	
	
	
}
