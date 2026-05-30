package backend.service;

import org.springframework.stereotype.Service;

import backend.dto.UserResponseDTO;
import backend.dto.auth.LoginRequest;
import backend.dto.auth.RegisterRequest;
import backend.model.User;
import backend.repository.UserRepository;

@Service
public class AuthService {
    
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponseDTO register(RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // TODO: Hash password
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public UserResponseDTO login(LoginRequest request){
        // TODO: Find user by email
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }
        // TODO: Check password
        // TODO: return UserResponseDTO
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
