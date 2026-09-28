package com.servicehub.service;

import com.servicehub.model.Booking;
import com.servicehub.model.BookingStatus;
import com.servicehub.model.User;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;

    public BookingService(BookingRepository bookingRepo, UserRepository userRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Booking createBooking(Long userId, String customerName, String phone, String serviceName, String bookingDate, String bookingTime, String description) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Booking booking = new Booking(user, customerName.trim(), phone.trim(), serviceName.trim(), bookingDate.trim(), bookingTime.trim(), description != null ? description.trim() : "");
        booking = bookingRepo.save(booking);
        booking.setBookingCode(String.format("#SH-%04d", booking.getId()));
        return bookingRepo.save(booking);
    }

    @Transactional(readOnly = true)
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepo.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public boolean acceptBooking(Long bookingId) {
        Optional<Booking> opt = bookingRepo.findById(bookingId);
        if (opt.isPresent()) {
            Booking booking = opt.get();
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepo.save(booking);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean rejectBooking(Long bookingId) {
        Optional<Booking> opt = bookingRepo.findById(bookingId);
        if (opt.isPresent()) {
            Booking booking = opt.get();
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepo.save(booking);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean cancelBooking(Long bookingId, Long userId) {
        Optional<Booking> opt = bookingRepo.findById(bookingId);
        if (opt.isPresent()) {
            Booking booking = opt.get();
            if (booking.getUser().getId().equals(userId)) {
                booking.setStatus(BookingStatus.CANCELLED);
                bookingRepo.save(booking);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public long countTotalBookings() {
        return bookingRepo.count();
    }

    @Transactional(readOnly = true)
    public long countPendingBookings() {
        return bookingRepo.countByStatus(BookingStatus.PENDING);
    }
}
