package backend.importer.openfoodfacts;
import java.util.List;

public class OpenFoodFactsSearchResponse {
    private List<OpenFoodFactsProductResponse> products;

    // EXAMPLE API RESPONSE
    // WE WANT TO PULL OUT PRODUCTS
    // THEN WE PULL OUT NUTRIMENTS AND OTHER INFO IN OUR NUTRIMENTS_RESPONSE AND PRODUCT_RESPONSE
    //   "count": 5000,
    //   "page": 1,
    //   "page_size": 10,
    //   "products": [
    //     {
    //       "code": "123456789",
    //       "product_name": "Frozen Pizza",
    //       "brands": "Some Brand",
    //       "nutriments": {
    //         "energy-kcal_100g": 250,
    //         "proteins_100g": 11,
    //         "carbohydrates_100g": 30,
    //         "fat_100g": 9
    //       }
    //     }
    //   ]
    //}
    public List<OpenFoodFactsProductResponse> getProducts(){
        return products;
    }

    public void setProducts(List<OpenFoodFactsProductResponse> products) {
        this.products = products;
    }
}