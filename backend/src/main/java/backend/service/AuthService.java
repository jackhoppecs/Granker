package backend.service;

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

        //TODO: Check duplicate email

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
        // TODO: Check password
        // TODO: return UserResponseDTO
        return null;
    }
}
