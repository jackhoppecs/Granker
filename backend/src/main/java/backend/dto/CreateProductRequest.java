package backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateProductRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    private String description;

    public CreateProductRequest(){

    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
