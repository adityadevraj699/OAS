package com.myproject.OAS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Student")
public class StudentController {
	
	@Autowired
	private HttpSession session;

	@GetMapping("/Dashboard")
	public String showDashboard() {
		if(session.getAttribute("loggedInstudent") == null) {
			return "redirect:/login";
		}
		return "Student/Dashboard";
	}
	
	
}
