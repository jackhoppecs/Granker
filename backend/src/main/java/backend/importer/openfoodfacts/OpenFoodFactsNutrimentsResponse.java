package backend.importer.openfoodfacts;
import com.fasterxml.jackson.annotation.JsonProperty;


// Match the shape of the Open Food facts JSON response not our app model
// Here so Spring/Jackson can deserialize the external api response into java objects
public class OpenFoodFactsNutrimentsResponse {

    @JsonProperty("energy-kcal_100g")
    private Double calories;

    @JsonProperty("proteins_100g")
    private Double proteinGrams;

    @JsonProperty("carbohydrates_100g")
    private Double carbGrams;

    @JsonProperty("fat_100g")
    private Double fatGrams;

    public Double getCalories(){
        return calories;
    }

    public void setCalories(Double calories){
        this.calories = calories;
    }

    public Double getProteinGrams(){
        return proteinGrams;
    }

    public void setProteinGrams(Double proteinGrams){
        this.proteinGrams = proteinGrams;
    }

    public Double getCarbGrams(){
        return carbGrams;
    }

    public void setCarbGrams(Double carbGrams){
        this.carbGrams = carbGrams;
    }

    public Double getFatGrams(){
        return fatGrams;
    }

    public void setFatGrams(Double fatGrams){
        this.fatGrams = fatGrams;
    }
}