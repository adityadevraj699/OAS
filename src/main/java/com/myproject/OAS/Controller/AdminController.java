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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.OAS.Model.Enquiry;
import com.myproject.OAS.Model.StudyMaterial;
import com.myproject.OAS.Model.Users;
import com.myproject.OAS.Model.Users.UserRole;
import com.myproject.OAS.Model.Users.UserStatus;
import com.myproject.OAS.Repository.EnquiryRepository;
import com.myproject.OAS.Repository.StudyMaterialRepository;
import com.myproject.OAS.Repository.UserRepository;
import com.myproject.OAS.Service.CloudinaryService;
import com.myproject.OAS.Service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Objects;

@Controller
@RequestMapping("/Admin")
public class AdminController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EnquiryRepository enquiryRepo;
	
	
	@Autowired
	private StudyMaterialRepository studyMaterialRepo;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private StudyMaterialRepository materialRepo;
	
	@Autowired
	private EmailService emailService;
	
	
	@GetMapping("/Dashboard")
	public String showDashboard(Model model) {
		if(session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/login";
		}
		
		long assignmentCount = materialRepo.countByMaterialType(StudyMaterial.MaterialType.Assignment);
        long studyMaterialCount = materialRepo.countByMaterialType(StudyMaterial.MaterialType.Study_Material);
        long totalStudentCount = userRepo.countByRole(Users.UserRole.STUDENT);
        long pendingStudentCount = userRepo.countByRoleAndStatus(Users.UserRole.STUDENT, Users.UserStatus.PENDING);
        long enquiryCount = enquiryRepo.count();

        List<Enquiry> recentEnquiries = enquiryRepo.findTop5ByOrderByEnquiryDateDesc();

        // Add attributes to model
        model.addAttribute("assignmentCount", assignmentCount);
        model.addAttribute("studyMaterialCount", studyMaterialCount);
        model.addAttribute("totalStudentCount", totalStudentCount);
        model.addAttribute("pendingStudentCount", pendingStudentCount);
        model.addAttribute("enquiryCount", enquiryCount);
        model.addAttribute("recentEnquiries", recentEnquiries);
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
	    if (session.getAttribute("loggedInAdmin") == null) {
	        return "redirect:/login";
	    }
	    try {
	        if (userRepo.existsByEmail(student.getEmail())) {
	            attr.addFlashAttribute("mgs", "User Already Exists " + student.getEmail() + "!");
	            return "redirect:/Admin/AddStudent";
	        }

	        student.setPassword("Password123");
	        student.setRole(UserRole.STUDENT);
	        student.setStatus(UserStatus.PENDING);
	        student.setRollNo("MIT-" + System.currentTimeMillis());
	        student.setRegDate(LocalDateTime.now());
	        userRepo.save(student);

	        // Email details
	        String subject = "Welcome to MIT Student Portal";

	        String body = """
	        <html>
	        <body style="font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;">
	            <div style="max-width:600px; margin:auto; background:#ffffff; border-radius:8px; padding:20px; box-shadow:0px 2px 8px rgba(0,0,0,0.1);">
	                <h2 style="color:#004080; text-align:center;">Welcome to MIT</h2>
	                <p>Dear <b>%s</b>,</p>
	                <p>We are pleased to inform you that your registration on the <b>MIT Student Portal</b> has been completed successfully.</p>
	                
	                <p>You can now log in and complete your registration and payment process using the credentials below:</p>
	                
	                <div style="background:#f0f8ff; border-left:5px solid #004080; padding:10px; margin:20px 0;">
	                    <p><b>User ID (Enrollment No):</b> %s</p>
	                    <p><b>Password:</b> %s</p>
	                </div>

	                <p>For security purposes, please change your password after your first login.</p>
	                
	                <p style="margin-top:20px;">Best Regards,<br>
	                <b>MIT Administration Team</b></p>
	                
	                <hr style="margin-top:30px;">
	                <p style="font-size:12px; color:#666;">This is an automated message from the MIT Student Portal. Please do not reply to this email.</p>
	            </div>
	        </body>
	        </html>
	        """.formatted(student.getName(), student.getRollNo(), student.getPassword());

	        // Send HTML email
	        emailService.sendHtmlEmail(student.getEmail(), subject, body);

	        attr.addFlashAttribute("mgs",
	                "Registration Successful, Enrollment No: " + student.getRollNo()
	                        + ", Password: " + student.getPassword()
	                        + ". Login details have been sent to " + student.getEmail());

	        return "redirect:/Admin/AddStudent";
	    } catch (Exception e) {
	        attr.addFlashAttribute("mgs", "Error :" + e.getMessage());
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

	        // Approve student
	        student.setStatus(UserStatus.APPROVED);
	        userRepo.save(student);

	        // Email details
	        String subject = "Your Registration is Approved - MIT Institute";

	        String body = """
	        <html>
	        <body style="font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;">
	            <div style="max-width:600px; margin:auto; background:#ffffff; border-radius:8px; padding:20px; box-shadow:0px 2px 8px rgba(0,0,0,0.1);">
	                <h2 style="color:#004080; text-align:center;">Registration Approved</h2>
	                <p>Dear <b>%s</b>,</p>
	                <p>We are pleased to inform you that your registration with <b>MIT Institute</b> has been <b>approved</b> and your verification is now complete.</p>
	                
	                <p>You can now log in to the portal to access your account and complete further formalities.</p>
	                
	                <p>We are excited to have you join our institute and look forward to supporting your educational journey!</p>
	                
	                <p style="margin-top:20px;">Best Regards,<br>
	                <b>MIT Administration Team</b></p>
	                
	                <hr style="margin-top:30px;">
	                <p style="font-size:12px; color:#666;">This is an automated message from MIT Institute. Please do not reply to this email.</p>
	            </div>
	        </body>
	        </html>
	        """.formatted(student.getName());

	        // Send email
	        emailService.sendHtmlEmail(student.getEmail(), subject, body);

	        attr.addFlashAttribute("mgs", "Student " + student.getName() + " approved successfully and email sent.");

	    } catch (Exception e) {
	        attr.addFlashAttribute("mgs", "Error approving student: " + e.getMessage());
	    }

	    return "redirect:/Admin/NewStudents";
	}
	
	
	@GetMapping("/ManageStudents")
	public String ShowManageStudents(Model model)
	{
		if (session.getAttribute("loggedInAdmin") ==null) {
			return "redirect:/login";
		}
		
		List<Users> newStudents = userRepo.findAllByRoleAndStatusOrStatus(UserRole.STUDENT, UserStatus.APPROVED, UserStatus.DISABLED);
		model.addAttribute("newStudents", newStudents);
		return "Admin/ManageStudents";
	}
	
	@GetMapping("/ViewPayment")
	public String showViewPayment(@RequestParam("id") long id, RedirectAttributes attr, Model model) {
	    if (session.getAttribute("loggedInAdmin") == null) {
	        return "redirect:/login";
	    }
	    try {
	        Users student = userRepo.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + id));
	        model.addAttribute("student", student);
	        return "Admin/ViewPayment";	    } catch (Exception e) {
	        attr.addFlashAttribute("msg", "Something Went Wrong!");
	        return "redirect:/Admin/NewStudents"; // <-- fixed typo
	    }
	}

	
	@GetMapping("/ViewPayments")
	public String showViewPayments(@RequestParam("id") long id, RedirectAttributes attr, Model model) {
	    if (session.getAttribute("loggedInAdmin") == null) {
	        return "redirect:/login";
	    }
	    try {
	        Users student = userRepo.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + id));
	        model.addAttribute("student", student);
	        return "Admin/ViewPayments";	    } catch (Exception e) {
	        attr.addFlashAttribute("msg", "Something Went Wrong!");
	        return "redirect:/Admin/ManageStudents"; // <-- fixed typo
	    }
	}

	
	@GetMapping("/UpdateStatus")
	public String updateStatus(@RequestParam long id,
	                           @RequestParam String status,
	                           RedirectAttributes redirectAttributes) {
	    Users user = userRepo.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));

	    try {
	        Users.UserStatus newStatus = Users.UserStatus.valueOf(status);
	        user.setStatus(newStatus);
	        userRepo.save(user);
	        redirectAttributes.addFlashAttribute("statusUpdate", "Status updated to " + newStatus);
	    } catch (IllegalArgumentException ex) {
	        redirectAttributes.addFlashAttribute("statusUpdate", "Invalid status value!");
	    }

	    return "redirect:/Admin/ManageStudents"; // लिस्ट पेज पर वापस जाएँ
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
	
	@GetMapping("/UploadMaterial")
	public String showUploadMaterial(Model model) {
	    if (session.getAttribute("loggedInAdmin") == null) {
	        return "redirect:/login";
	    }
	    StudyMaterial material = new StudyMaterial();
	    model.addAttribute("material", material);
	    return "Admin/UploadMaterial";
	}

	@PostMapping("/UploadMaterial")
    public String uploadMaterial(@ModelAttribute("material") StudyMaterial material,
                                 @RequestParam("file") MultipartFile file,
                                 RedirectAttributes attributes) {
        try {
            // 1️⃣ File upload to Cloudinary
            String fileUrl = cloudinaryService.uploadFile(file);
            material.setFileUrl(fileUrl);
            material.setUploadedDate(LocalDateTime.now());

            // 2️⃣ Save study material
            studyMaterialRepo.save(material);

         // 3️⃣ Find relevant students
            List<Users> students = userRepo.findAll().stream()
                    .filter(s -> s.getRole() == Users.UserRole.STUDENT &&
                                 s.getStatus() == Users.UserStatus.APPROVED)
                    .filter(s -> material.getProgram() == null ||
                                 (s.getProgram() != null && s.getProgram().equals(material.getProgram())))
                    .filter(s -> material.getBranch() == null ||
                                 (s.getBranch() != null && s.getBranch().equals(material.getBranch())))
                    .filter(s -> material.getYear() == null ||
                                 (s.getYear() != null && s.getYear().equals(material.getYear())))
                    .toList();

            // 4️⃣ Prepare material type safely
            String materialType = material.getMaterialType() != null
                                  ? material.getMaterialType().name().replace("_", " ")
                                  : "Material";

            // 5️⃣ Email template (single content for all)
            String subject = "New " + materialType + " Uploaded - MIT LMS";
            String bodyTemplate = """
            <html>
            <body style="font-family: Arial, sans-serif; background:#f9f9f9; padding:20px;">
                <div style="max-width:600px; margin:auto; background:#fff; border-radius:8px; padding:20px; box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                    <h2 style="color:#004080; text-align:center;">New %s Available</h2>
                    <p>A new <b>%s</b> has been uploaded for your course on MIT LMS.</p>
                    <p><b>Subject:</b> %s<br>
                       <b>Program:</b> %s<br>
                       <b>Branch:</b> %s<br>
                       <b>Year:</b> %s</p>
                    <p>You can access it here: <a href="%s">View Material</a></p>
                    <p>Best Regards,<br>MIT Administration Team</p>
                </div>
            </body>
            </html>
            """;

            String body = String.format(bodyTemplate,
                                        materialType,
                                        materialType,
                                        material.getSubject(),
                                        material.getProgram(),
                                        material.getBranch(),
                                        material.getYear(),
                                        fileUrl);

            // 6️⃣ Collect emails in BCC
            List<String> bccEmails = students.stream()
                                             .map(Users::getEmail)
                                             .toList();

            // 7️⃣ Send BCC email asynchronously
            emailService.sendHtmlEmailBcc(bccEmails, subject, body);

            // 8️⃣ Flash message for admin
            attributes.addFlashAttribute("mgs", "Material uploaded successfully!");
            return "redirect:/Admin/UploadMaterial";

        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("mgs", "Failed to upload material!");
            return "redirect:/Admin/UploadMaterial";
        }
    }


	
	
	
	
	
	
	

	
}
