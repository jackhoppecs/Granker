package backend.service;

import backend.dto.CreateUserRequest;
import backend.model.User;
import backend.repository.UserRepository;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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

    public User updateUser(Long id, User updatedUser){
        return userRepository.findById(id)
        .map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
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
