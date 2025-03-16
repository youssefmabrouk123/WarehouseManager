package com.twd.Warehouse.service;

import com.twd.Warehouse.dto.UserDTO;
import com.twd.Warehouse.entity.OurUsers;
import com.twd.Warehouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserDTO createUser(OurUsers user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        OurUsers savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO updateUser(Integer id, OurUsers userDetails) {
        OurUsers user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setNom(userDetails.getNom());
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        user.setFullName(userDetails.getFullName());
        user.setRole(userDetails.getRole());
        user.setActive(userDetails.isActive());

        // Only update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        OurUsers updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(OurUsers user) {
        return UserDTO.builder()
                .id(user.getId())
                .nom(user.getNom())
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
}
