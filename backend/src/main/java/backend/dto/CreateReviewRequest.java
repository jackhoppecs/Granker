package backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateReviewRequest {
    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String text;

    public CreateReviewRequest(){

    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }
}
