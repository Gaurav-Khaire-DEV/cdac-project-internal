/*
package com.example.giscord.service;

import com.example.giscord.entity.User;
import com.example.giscord.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String userName, String plainPassword) {
        String hashed = encoder.encode(plainPassword);
        User user = new User(userName, hashed);
        return userRepository.save(user);
    }

    public boolean verifyPassword(String userName, String plainPassword) {
        return userRepository.findByUserName(userName)
                .map(user -> encoder.matches(plainPassword, user.getPasswordHash()))
                .orElse(false);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}


 */









package com.example.giscord.service;

import com.example.giscord.entity.User;
import com.example.giscord.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User registerUser(String userName, String plainPassword) {
        String hashed = encoder.encode(plainPassword);
        User user = new User(userName, hashed);
        return userRepository.save(user);
    }

    public boolean verifyPassword(String userName, String plainPassword) {
        return userRepository.findByUserName(userName)
                .map(user -> encoder.matches(plainPassword, user.getPasswordHash()))
                .orElse(false);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
