package com.myproject.OAS.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Admin")
public class AdminController {

	@GetMapping("/Dashboard")
	public String showDashboard() {
		return "Admin/Dashboard";
	}
	
}
