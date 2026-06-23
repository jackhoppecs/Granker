// Calls Open Food Facts and maps the raw JSON response into external response classes.
package backend.importer.openfoodfacts;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


// Tells spring to create an instance of this class and manage
// Client talks to external service
@Component
public class OpenFoodFactsClient {

    // RestClient is Spring's tool for making HTTP requests basically a mini postman or curl
    // Stores the configured HTTP client
    private final RestClient restClient;

    // Spring provides a RestClient.Builder which allows us to create a RestClient with a base URL
    // .build() actually creates the RestClient
    public OpenFoodFactsClient(RestClient.Builder restClientBuilder){
        this.restClient = restClientBuilder.baseUrl("https://world.openfoodfacts.org").build();
    }

    // Calls the API and returns the parsed search response
    public OpenFoodFactsSearchResponse searchFrozenFoods(){
        // .get() starts a get request
        // .uri builds the URL path and query parameters
        return restClient.get().uri(uriBuilder -> uriBuilder
            .path("/api/v2/search")
            .queryParam("categories_tags", "en:frozen-pizzas")
            .queryParam("fields", "code,product_name,brands,categories,image_url,url,nutriments")
            .queryParam("page_size", 10)
            .build())
            .retrieve()
            .body(OpenFoodFactsSearchResponse.class);
    }
}

