package com.servicehub.controller;

import com.servicehub.service.BookingService;
import com.servicehub.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    private final UserService userService;
    private final BookingService bookingService;

    public PageController(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model m) {
        m.addAttribute("activePage", "home");
        return "index";
    }

    @GetMapping("/services")
    public String services(Model m) {
        m.addAttribute("activePage", "services");
        return "services";
    }

    @GetMapping("/booking")
    public String booking(@RequestParam(required = false) String service, Model m, HttpSession s) {
        if (s.getAttribute("userId") == null) {
            return "redirect:/login?required=true";
        }
        m.addAttribute("activePage", "booking");
        m.addAttribute("selectedService", service);
        return "booking";
    }

    @PostMapping("/booking")
    public String handleBooking(@RequestParam String service,
                                @RequestParam String customerName,
                                @RequestParam String phone,
                                @RequestParam String date,
                                @RequestParam String time,
                                @RequestParam(required = false) String description,
                                HttpSession s,
                                RedirectAttributes ra) {
        Long userId = (Long) s.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login?required=true";
        }
        try {
            bookingService.createBooking(userId, customerName, phone, service, date, time, description);
            ra.addFlashAttribute("successMessage", "Service booking requested successfully! Your request is pending admin approval.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error submitting booking: " + e.getMessage());
            return "redirect:/booking";
        }
        return "redirect:/my-bookings";
    }

    @GetMapping("/my-bookings")
    public String bookings(Model m, HttpSession s) {
        Long userId = (Long) s.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login?required=true";
        }
        m.addAttribute("activePage", "bookings");
        m.addAttribute("bookings", bookingService.getUserBookings(userId));
        return "my-bookings";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        Long userId = (Long) s.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login?required=true";
        }
        boolean cancelled = bookingService.cancelBooking(id, userId);
        if (cancelled) {
            ra.addFlashAttribute("successMessage", "Booking has been cancelled.");
        } else {
            ra.addFlashAttribute("errorMessage", "Unable to cancel booking.");
        }
        return "redirect:/my-bookings";
    }

    @GetMapping("/admin")
    public String admin(Model m, HttpSession s) {
        if (s.getAttribute("userId") == null) {
            return "redirect:/login?required=true";
        }
        if (!"ADMIN".equals(s.getAttribute("userRole"))) {
            return "redirect:/my-bookings";
        }
        m.addAttribute("activePage", "admin");
        m.addAttribute("totalUsers", userService.countUsers());
        m.addAttribute("totalServices", 3);
        m.addAttribute("totalBookings", bookingService.countTotalBookings());
        m.addAttribute("pendingBookings", bookingService.countPendingBookings());
        m.addAttribute("bookings", bookingService.getAllBookings());
        return "admin";
    }

    @PostMapping("/admin/bookings/{id}/accept")
    public String acceptBooking(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        if (s.getAttribute("userId") == null || !"ADMIN".equals(s.getAttribute("userRole"))) {
            return "redirect:/login?required=true";
        }
        boolean accepted = bookingService.acceptBooking(id);
        if (accepted) {
            ra.addFlashAttribute("successMessage", "Booking request accepted successfully!");
        } else {
            ra.addFlashAttribute("errorMessage", "Booking not found.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/bookings/{id}/reject")
    public String rejectBooking(@PathVariable Long id, HttpSession s, RedirectAttributes ra) {
        if (s.getAttribute("userId") == null || !"ADMIN".equals(s.getAttribute("userRole"))) {
            return "redirect:/login?required=true";
        }
        boolean rejected = bookingService.rejectBooking(id);
        if (rejected) {
            ra.addFlashAttribute("successMessage", "Booking request rejected.");
        } else {
            ra.addFlashAttribute("errorMessage", "Booking not found.");
        }
        return "redirect:/admin";
    }
}
