package backend.controller;

import backend.model.User;
import backend.service.UserService;
import backend.dto.CreateUserRequest;
import backend.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers(){
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> dtos = new ArrayList<>();
        for (User user : users){
            UserResponseDTO addUser = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());
            dtos.add(addUser);
        }

        return dtos;

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        UserResponseDTO dto = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequest request){
        User newUser = new User(request.getUsername(), request.getEmail(), request.getPassword());
        User createdUser = userService.createUser(newUser);
        UserResponseDTO dto = new UserResponseDTO(createdUser.getId(), createdUser.getUsername(), createdUser.getEmail(), createdUser.isAdmin());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser){
        User user = userService.updateUser(id, updatedUser);

        if (user == null){
            return ResponseEntity.notFound().build();
        }

        UserResponseDTO dto = new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        if (userService.deleteUser(id)){
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}
