package com.example.giscord.controller;

import com.example.giscord.dto.UserLoginRequestDto;
import com.example.giscord.dto.UserRegisterRequestDto;
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
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) { 
        this.userService = userService; 
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody UserRegisterRequestDto req) {
        if (req.username() == null || req.username().isBlank()
                || req.password() == null || req.password().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password required"));
        }

        try {
            User created = userService.registerUser(req.username(), req.password());
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

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto req) {
        if (req.username() == null || req.password() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password required"));
        }

        // authenticate using AuthenticationManager or manual check + token creation
        boolean ok = userService.verifyPassword(req.username(), req.password());
        if (!ok) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
        }

        // load user and create token
        var userOpt = userService.findByUserName(req.username()); // add this helper in service
        if (userOpt.isEmpty()) return ResponseEntity.status(500).body(Map.of("error", "unexpected"));

        var user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUserId(), user.getUserName());

        // TODO: cleanup wrap into a toUserResponseDto (in UserService ??)
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
