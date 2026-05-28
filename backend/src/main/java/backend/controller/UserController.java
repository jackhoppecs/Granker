package backend.controller;

import backend.model.User;
import backend.service.UserService;
import backend.dto.CreateUserRequest;
import backend.dto.UserResponseDto;
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
    public List<UserResponseDto> getAllUsers(){
        List<User> users = userService.getAllUsers();
        List<UserResponseDto> dtos = new ArrayList<>();
        for (User user : users){
            UserResponseDto addUser = new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
            dtos.add(addUser);
        }

        return dtos;

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        UserResponseDto dto = new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserRequest request){
        User newUser = new User(request.getUsername(), request.getEmail(), request.getPassword());
        User createdUser = userService.createUser(newUser);
        UserResponseDto dto = new UserResponseDto(createdUser.getId(), createdUser.getUsername(), createdUser.getEmail());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser){
        User user = userService.updateUser(id, updatedUser);

        if (user == null){
            return ResponseEntity.notFound().build();
        }

        UserResponseDto dto = new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
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
