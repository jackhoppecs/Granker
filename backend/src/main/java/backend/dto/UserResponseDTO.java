package backend.dto;

public class UserResponseDTO{

    private Long id;
    private String username;
    private String email;
    private boolean admin;

    public UserResponseDTO(Long id, String username, String email, boolean admin){
        this.id = id;
        this.username = username;
        this.email = email;
        this.admin = admin;
    }

    // Need getters to convert java objects to JSON
    // json.put("id", dto.getID()) etc

    public Long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public boolean isAdmin(){
        return admin;
    }
}