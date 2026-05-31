package backend.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.dto.UserResponseDTO;
import backend.dto.auth.LoginRequest;
import backend.dto.auth.RegisterRequest;
import backend.model.User;
import backend.repository.UserRepository;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO register(RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public UserResponseDTO login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserResponseDTO getCurrentUser(Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
            return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
