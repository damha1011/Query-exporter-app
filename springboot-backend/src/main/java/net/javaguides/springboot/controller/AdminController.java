package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.*;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.UserEntity;
import net.javaguides.springboot.repository.RoleRepository;
import net.javaguides.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public List<UserEntity> getAllUser(){
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUser (@PathVariable Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserDTO data) throws Exception {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));

        user.setUsername(data.getUsername());
        user.setEmail(data.getEmail());
        user.setFullName(data.getFullName());
        user.setAddress(data.getAddress());
        user.setPhoneNumber(data.getPhoneNumber());
        List<String> roles = data.getRoles();
        List<Role> userRoles = new ArrayList<>();
        for (String role : roles) {
            Role userRole = roleRepository.findByName(role).get();
            userRoles.add(userRole);
        }
        user.setRoles(userRoles);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id :" + id));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}