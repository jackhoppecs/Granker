package backend.dto;

public class ReviewResponseDTO {
    
    private Long id;
    private int rating;
    private String text;
    private String username;
    private Long productId;

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
}
