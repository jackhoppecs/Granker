package backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

// Entity tells spring that this class should be stored in the DB
// AKA map this class to a table
@Entity
// Table defines the table name otherwise it would default to something else otherwise not required
@Table(name = "products")
public class Product {
    
    // Id defines the primary key
    @Id
    // Generated value tells postgres to autogenerate in this case the ID (auto-increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // this variable is a column in the db with nullable = false has to be filled
    // Column used for manual control like "TEXT" or nullable equals false (customization)
    // Otherwise it will make a default based on data type
    @Column(nullable = false)
    @NotBlank
    private String name;

    // Application-level validation handled by Spring Validation (Before saving to DB)
    // The @Column(nullabe = false) is a database-level constraint handled by PostgreSQL
    // NotBlank protects application/API
    // nullable = false protects Database itself
    @Column(nullable = false)
    @NotBlank
    private String brand;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Product(){

    }

    public Product(String name, String brand, String description, LocalDateTime createdAt){
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getBrand(){
        return brand;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public LocalDateTime setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
}
