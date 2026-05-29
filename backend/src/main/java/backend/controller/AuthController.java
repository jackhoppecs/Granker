package backend.controller;

import backend.dto.auth.LoginRequest;
import backend.dto.auth.RegisterRequest;
import backend.dto.UserResponseDTO;
import backend.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody LoginRequest request){
        UserResponseDTO user = authService.login(request);
        return ResponseEntity.ok(user);
    }
}
