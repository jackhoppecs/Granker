// Calls Open Food Facts and maps the raw JSON response into external response classes.
package backend.importer.openfoodfacts;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;


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
        this.restClient = restClientBuilder.baseUrl("https://world.openfoodfacts.org").defaultHeader("User-Agent", "Granker/1.7.0 (GrankerDev@gmail.com)").defaultHeader("Accept", "application/json").build();
    }

    // Calls the API and returns the parsed search response
    public OpenFoodFactsSearchResponse searchProductsByCategory(String category, int pageSize){
        // .get() starts a get request
        // .uri builds the URL path and query parameters
        try{
            return restClient.get().uri(uriBuilder -> uriBuilder
                .path("/api/v2/search")
                .queryParam("categories_tags", category)
                .queryParam("countries_tags", "en:united-states")
                .queryParam("fields", "code,product_name,brands,categories,image_url,url,nutriments")
                .queryParam("page_size", pageSize)
                .build())
                .retrieve()
                .body(OpenFoodFactsSearchResponse.class);
        } catch (RestClientResponseException ex) {
            throw new OpenFoodFactsException(
                "Open Food Facts returned HTTP " + ex.getStatusCode().value()
            );
        } catch (ResourceAccessException ex) {
            throw new OpenFoodFactsException(
                "Could not reach Open Food Facts. The request may have timed out."
            );
        }
    }
}

