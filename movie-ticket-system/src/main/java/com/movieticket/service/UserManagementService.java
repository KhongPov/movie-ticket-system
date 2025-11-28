package com.movieticket.service;

import com.movieticket.dto.UserManagementDTO;
import com.movieticket.entity.Booking;
import com.movieticket.entity.User;
import com.movieticket.repository.BookingRepository;
import com.movieticket.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserManagementDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserManagementDTO getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    public void suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus("SUSPENDED");
        userRepository.save(user);
    }

    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus("ACTIVE");
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus("INACTIVE");
        userRepository.save(user);
    }

    private UserManagementDTO mapToDTO(User user) {
        List<Booking> userBookings = bookingRepository.findByUserId(user.getId());
        Integer totalBookings = userBookings.size();

        Double totalSpent = userBookings.stream()
                .filter(b -> b.getStatus().equals("COMPLETED"))
                .mapToDouble(Booking::getTotalAmount)
                .sum();

        return UserManagementDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .totalBookings(totalBookings)
                .totalSpent(totalSpent)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
