package com.servicehub.controller;
import com.servicehub.model.*;
import com.servicehub.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Objects;
@Controller public class UserController {
 private final UserService service; public UserController(UserService service){this.service=service;}
 @GetMapping("/register") public String registerPage(HttpSession s,Model m){if(s.getAttribute("userId")!=null)return "ADMIN".equals(s.getAttribute("userRole"))?"redirect:/admin":"redirect:/my-bookings";if(!m.containsAttribute("registration"))m.addAttribute("registration",new RegistrationForm());m.addAttribute("activePage","register");return "register";}
 @PostMapping("/register") public String register(@Valid @ModelAttribute("registration") RegistrationForm f,BindingResult r,Model m,RedirectAttributes ra){if(!Objects.equals(f.getPassword(),f.getConfirmPassword()))r.rejectValue("confirmPassword","password.mismatch","Passwords do not match");if(r.hasErrors()){m.addAttribute("activePage","register");return "register";}try{service.register(f);}catch(IllegalArgumentException e){r.rejectValue("email","email.duplicate",e.getMessage());m.addAttribute("activePage","register");return "register";}ra.addFlashAttribute("successMessage","Account created successfully. Please sign in.");return "redirect:/login";}
 @GetMapping("/login") public String loginPage(@RequestParam(required=false) String required,HttpSession s,Model m){if(s.getAttribute("userId")!=null)return "ADMIN".equals(s.getAttribute("userRole"))?"redirect:/admin":"redirect:/my-bookings";if(!m.containsAttribute("loginForm"))m.addAttribute("loginForm",new LoginForm());if(required!=null)m.addAttribute("errorMessage","Please sign in to view this page.");m.addAttribute("activePage","login");return "login";}
 @PostMapping("/login") public String login(@Valid @ModelAttribute("loginForm") LoginForm f,BindingResult r,Model m,HttpSession s){if(r.hasErrors()){m.addAttribute("activePage","login");return "login";}User u=service.authenticate(f.getEmail(),f.getPassword()).orElse(null);if(u==null){m.addAttribute("errorMessage","Invalid email or password.");m.addAttribute("activePage","login");return "login";}s.setAttribute("userId",u.getId());s.setAttribute("userName",u.getName());s.setAttribute("userRole",u.getRole().name());if(u.getRole()==Role.ADMIN){return "redirect:/admin";}return "redirect:/my-bookings";}
 @PostMapping("/logout") public String logout(HttpSession s,RedirectAttributes ra){s.invalidate();ra.addFlashAttribute("successMessage","You have been signed out.");return "redirect:/login";}
}
