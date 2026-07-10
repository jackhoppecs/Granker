package backend.controller;

import backend.model.User;
import backend.service.UserService;
import backend.dto.UpdateUserRequest;
import backend.dto.UserResponseDTO;
import backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService){
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers(HttpSession session){
        authService.requireAdminUser(session);
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> dtos = new ArrayList<>();
        for (User user : users){
            UserResponseDTO addUser = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());
            dtos.add(addUser);
        }

        return dtos;

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id, HttpSession session){
        authService.requireAdminUser(session);
        User user = userService.getUserById(id);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        UserResponseDTO dto = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request, HttpSession session){
        authService.requireAdminUser(session);
        User user = userService.updateUser(id, request);

        if (user == null){
            return ResponseEntity.notFound().build();
        }

        UserResponseDTO dto = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpSession session){
        authService.requireAdminUser(session);
        if (userService.deleteUser(id)){
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}
