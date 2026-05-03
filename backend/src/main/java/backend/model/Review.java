package backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    private String text;

    // A review belongs to one product but a product can have many reviews
    @ManyToOne
    // Join Column tells DB what foreign key colum to create in reviews table
    @JoinColumn(name = "product_id")
    private Product product;

    // A review belongs to one user but a user can have many reviews
    @ManyToOne
    // Join Column tells DB what foreign key colum to create in reviews table
    @JoinColumn(name = "user_id")
    private User user;

    public Review(){

    }

    public Review(int rating, String text){
        this.rating = rating;
        this.text = text;
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

    public Product getProduct(){
        return product;
    }
    public User getUser(){
        return user;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setText(String text){
        this.text = text;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public void setUser(User user){
        this.user = user;
    }
}
