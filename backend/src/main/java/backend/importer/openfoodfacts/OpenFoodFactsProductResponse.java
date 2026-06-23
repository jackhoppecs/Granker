package backend.importer.openfoodfacts;
// In Java, the package line tells Java where this class logically lives inside your project.
// Packages help java organize classes and avoid naming conflicts

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenFoodFactsProductResponse {

    private String code;

    @JsonProperty("product_name")
    private String productName;

    private String brands;

    private String categories;

    @JsonProperty("image_url")
    private String imageUrl;

    private String url;

    private OpenFoodFactsNutrimentsResponse nutriments;

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public String getBrands(){
        return brands;
    }

    public void setBrands(String brands){
        this.brands = brands;
    }

    public String getCategories(){
        return categories;
    }

    public void setCategories(String categories){
        this.categories = categories;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public OpenFoodFactsNutrimentsResponse getNutriments(){
        return nutriments;
    }

    public void setNutriments(OpenFoodFactsNutrimentsResponse nutriments){
        this.nutriments = nutriments;
    }   
}