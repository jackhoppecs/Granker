package backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    @NotBlank
    private String password;

    // Boolean can be null and boolean has to be true/false
    @Column(nullable = false)
    private boolean admin = false;

    public User(){
    }

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
        this.admin = false;
    }

    // Spring uses a library called Jackson to convert JSON to a java object
    // So for example:
    /*
        JSON:
        {
        "username": "andrew",
        "email": "andrew@example.com",
        "password": "123"
        }

        does something like:

        User user = new User();
        user.setUsername("andrew");
        user.setEmail("andrew@example.com");
        user.setPassword("123");

        and from an object back to JSON with getters

        so we need this even if they're never explicitly called.
    */

    public Long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
