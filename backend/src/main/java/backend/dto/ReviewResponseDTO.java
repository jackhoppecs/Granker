package backend.dto;

import java.time.LocalDateTime;

public class ReviewResponseDTO {
    
    private Long id;
    private int rating;
    private String text;
    private String username;
    private Long productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponseDTO(
            Long id,
            int rating,
            String text,
            String username,
            Long productId
    ){
        this.id = id;
        this.rating = rating;
        this.text = text;
        this.username = username;
        this.productId = productId;
    }

    public Long getId(){
        return id;
    }

    public int getRating(){
        return rating;
    }

    public String getText(){
        return text;
    }

    public String getUsername(){
        return username;
    }

    public Long getProductId(){
        return productId;
    }

    public LocalDateTime getCreateAt(){
        return createdAt;
    }

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
}
