package backend.service;

import backend.dto.UpdateUserRequest;
import backend.model.User;
import backend.repository.UserRepository;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    // public User createUser(CreateUserRequest request) {
    //     if (userRepository.findByEmail(request.getEmail()).isPresent()) {
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use.");
    //     }

    //     User user = new User();
    //     user.setUsername(request.getUsername());
    //     user.setEmail(request.getEmail());
    //     user.setPassword(passwordEncoder.encode(request.getPassword()));
    //     user.setAdmin(false);

    //     return userRepository.save(user);
    // }

    public User updateUser(Long id, UpdateUserRequest request){
        return userRepository.findById(id)
        .map(user -> {
            user.setUsername(request.getUsername());
            userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use.");
                }
            });
            user.setEmail(request.getEmail());
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            return userRepository.save(user);
        }).orElse(null);
    }

    public boolean deleteUser(Long id){
        if (!userRepository.existsById(id)){
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }   
}
