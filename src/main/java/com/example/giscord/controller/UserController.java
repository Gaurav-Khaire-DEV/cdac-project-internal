/*
package com.example.giscord.controller;


import com.example.giscord.entity.User;
import com.example.giscord.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        return userService.registerUser(username, password);
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        boolean valid = userService.verifyPassword(username, password);
        return valid ? "✅ Login successful" : "❌ Invalid credentials";
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}




 */



package com.example.giscord.controller;

import com.example.giscord.dto.UserLoginRequest;
import com.example.giscord.dto.UserRegisterRequest;
import com.example.giscord.dto.UserResponseDto;
import com.example.giscord.entity.User;
import com.example.giscord.security.JwtUtil;
import com.example.giscord.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil = new JwtUtil("change-me-super-secret-and-long-enough", 300000);

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()
                || req.getPassword() == null || req.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password required"));
        }

        try {
            User created = userService.registerUser(req.getUsername(), req.getPassword());
            UserResponseDto dto = new UserResponseDto(
                    created.getUserId(),
                    created.getUserName(),
                    created.getCreatedAt(),
                    created.getUpdatedAt()
            );
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dto);
        } catch (Exception e) {
            // handle duplicate username or DB constraint violations
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /*
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserLoginRequest req) {
        if (req.getUsername() == null || req.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password required"));
        }

        boolean ok = userService.verifyPassword(req.getUsername(), req.getPassword());
        if (!ok) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
        }

        // Minimal for now: return a success message + user info.
        // Later: replace with JWT token and remove password-based auth in headers.
        var userOpt = userService.getAllUsers().stream()
                .filter(u -> req.getUsername().equals(u.getUserName()))
                .findFirst();

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(500).body(Map.of("error", "unexpected error"));
        }

        User user = userOpt.get();
        UserResponseDto dto = new UserResponseDto(user.getUserId(), user.getUserName(), user.getCreatedAt(), user.getUpdatedAt());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Map.of("message", "login successful", "user", dto));
    }

     */


    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserLoginRequest req) {
        if (req.getUsername() == null || req.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password required"));
        }

        // authenticate using AuthenticationManager or manual check + token creation
        boolean ok = userService.verifyPassword(req.getUsername(), req.getPassword());
        if (!ok) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
        }

        // load user and create token
        var userOpt = userService.findByUserName(req.getUsername()); // add this helper in service
        if (userOpt.isEmpty()) return ResponseEntity.status(500).body(Map.of("error", "unexpected"));

        var user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserName());

        UserResponseDto dto = new UserResponseDto(user.getUserId(), user.getUserName(), user.getCreatedAt(), user.getUpdatedAt());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Map.of("token", token, "user", dto));
    }


    // Optional helper endpoint (dev): get basic user info
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(u -> ResponseEntity.ok().body(new UserResponseDto(u.getUserId(), u.getUserName(), u.getCreatedAt(), u.getUpdatedAt())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
