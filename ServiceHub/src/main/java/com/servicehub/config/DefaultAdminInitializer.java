package com.servicehub.config;

import com.servicehub.model.Role;
import com.servicehub.model.User;
import com.servicehub.repository.UserRepository;
import com.servicehub.service.BookingService;
import com.servicehub.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DefaultAdminInitializer {

    @Bean
    CommandLineRunner initializeData(UserService userService,
                                    UserRepository userRepo,
                                    BookingService bookingService,
                                    PasswordEncoder encoder,
                                    @Value("${servicehub.admin.name}") String name,
                                    @Value("${servicehub.admin.email}") String email,
                                    @Value("${servicehub.admin.phone}") String phone,
                                    @Value("${servicehub.admin.password}") String password) {
        return args -> {
            userService.createAdminIfMissing(name, email, phone, password);

            // Create sample user and initial demo booking if database is fresh
            if (bookingService.countTotalBookings() == 0) {
                User demoUser = userRepo.findByEmailIgnoreCase("customer@servicehub.local").orElseGet(() ->
                        userRepo.save(new User("Ananya Rao", "customer@servicehub.local", "9876543210", encoder.encode("User@12345"), Role.USER))
                );

                bookingService.createBooking(demoUser.getId(), demoUser.getName(), demoUser.getPhone(),
                        "Home cleaning", "2026-10-02", "10:00 AM", "Living room and kitchen deep clean requested");

                bookingService.createBooking(demoUser.getId(), demoUser.getName(), demoUser.getPhone(),
                        "AC service", "2026-10-05", "02:00 PM", "AC cooling inspection");
            }
        };
    }
}
