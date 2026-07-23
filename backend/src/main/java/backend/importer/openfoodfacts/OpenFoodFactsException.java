package backend.importer.openfoodfacts;

public class OpenFoodFactsException extends RuntimeException {

    public OpenFoodFactsException(String message) {
        super(message);
    }

    public OpenFoodFactsException(String message, Throwable cause) {
        super(message, cause);
    }
}