package backend.controller;

import backend.dto.auth.LoginRequest;
import backend.dto.auth.RegisterRequest;
import backend.dto.UserResponseDTO;
import backend.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequest request){
        UserResponseDTO user = authService.register(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequest request, HttpSession session){
        UserResponseDTO user = authService.login(request);

        //When login succeeds, store this user's id in the browser's session.
        session.setAttribute("userId", user.getId());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null){
            return ResponseEntity.status(401).build();
        }

        UserResponseDTO user = authService.getCurrentUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
