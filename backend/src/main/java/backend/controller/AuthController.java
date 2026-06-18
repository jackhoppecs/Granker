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
        // Spring provides the HttpSession for the current browser request.
        // AKA spring creates/uses an HttpSession
        // A session can exist before login, but it does not represent a logged-in user
        // until we store a user identifier in it.
        // When login succeeds, store this user's id in the current HTTP session.
        session.setAttribute("userId", user.getId());

        // The browser stores only the session id cookie, not the user data itself.
        // On future requests, that cookie lets Spring find this same server-side session.
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(HttpSession session){
        // Spring resolves this session from the request's session cookie, if one exists.
        // If the session has no userId, this browser is not currently logged in.
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null){
            return ResponseEntity.status(401).build();
        }

        UserResponseDTO user = authService.getCurrentUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        // Invalidate the server-side session so this session id can no longer be used
        // to access logged-in user data.
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
