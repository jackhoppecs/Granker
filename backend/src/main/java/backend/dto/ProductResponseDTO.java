package backend.dto;

public class ProductResponseDTO {
    
    private Long id;
    private String name;
    private String brand;
    private String description;

    public ProductResponseDTO(Long id, String name, String brand, String description){
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getBrand(){
        return brand;
    }

    public String getDescription(){
        return description;
    }
}
