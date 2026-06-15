package backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import backend.model.Product;
import backend.model.Review;
import backend.model.User;
import backend.repository.ProductRepository;
import backend.repository.ReviewRepository;
import backend.repository.UserRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
        ProductRepository productRepository,
        ReviewRepository reviewRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (productRepository.count() > 0 || reviewRepository.count() > 0 || userRepository.count() > 0) {
                return;
            }

            User demoUser = createUser(userRepository, passwordEncoder, "demo_user", "demo@example.com", "password");
            User alex = createUser(userRepository, passwordEncoder, "alex", "alex@example.com", "password");
            User sam = createUser(userRepository, passwordEncoder, "sam", "sam@example.com", "password");
            User jordan = createUser(userRepository, passwordEncoder, "jordan", "jordan@example.com", "password");

            Product orangeChicken = createProduct(
                    productRepository,
                    "Mandarin Orange Chicken",
                    "Trader Joe's",
                    "Crispy battered chicken pieces with a sweet and tangy orange sauce."
            );

            Product frozenPizza = createProduct(
                    productRepository,
                    "Frozen Pepperoni Pizza",
                    "HEB",
                    "Classic frozen pepperoni pizza with mozzarella cheese and a crispy crust."
            );

            Product waffles = createProduct(
                    productRepository,
                    "Homestyle Waffles",
                    "Eggo",
                    "Frozen toaster waffles with a light, crisp texture."
            );

            Product burrito = createProduct(
                    productRepository,
                    "Bean and Cheese Burrito",
                    "Amy's",
                    "Organic bean and cheese burrito wrapped in a soft flour tortilla."
            );

            Product lasagna = createProduct(
                    productRepository,
                    "Meat Lasagna",
                    "Rao's",
                    "Frozen meat lasagna with pasta, ricotta, mozzarella, and tomato sauce."
            );

            Product macAndCheese = createProduct(
                    productRepository,
                    "Mac and Cheese",
                    "Stouffer's",
                    "Classic frozen macaroni and cheese with a creamy cheddar sauce."
            );

            Product dumplings = createProduct(
                    productRepository,
                    "Chicken Soup Dumplings",
                    "Trader Joe's",
                    "Frozen soup dumplings filled with chicken and savory broth."
            );

            Product iceCreamBars = createProduct(
                    productRepository,
                    "Vanilla Ice Cream Bars",
                    "Yasso",
                    "Frozen Greek yogurt bars with vanilla flavor."
            );

            // Mandarin Orange Chicken: highly reviewed, high rating
            createReview(reviewRepository, demoUser, orangeChicken, 5, "Great flavor and easy to make. One of the better frozen meals.");
            createReview(reviewRepository, alex, orangeChicken, 4, "Sauce is really good, but it can be a little sweet.");
            createReview(reviewRepository, sam, orangeChicken, 5, "Crispy if you air fry it. Definitely worth keeping around.");

            // Frozen Pizza: mixed reviews
            createReview(reviewRepository, demoUser, frozenPizza, 3, "Decent for the price. Crust could be better.");
            createReview(reviewRepository, jordan, frozenPizza, 2, "Not terrible, but I probably would not buy it again.");

            // Waffles: solid but simple
            createReview(reviewRepository, alex, waffles, 4, "Reliable breakfast option. Gets crispy in the toaster.");
            createReview(reviewRepository, sam, waffles, 4, "Good quick breakfast, especially with peanut butter.");

            // Burrito: average
            createReview(reviewRepository, demoUser, burrito, 4, "Simple but filling. Good quick lunch.");
            createReview(reviewRepository, jordan, burrito, 3, "Convenient, but the filling could use more flavor.");

            // Lasagna: high rating, fewer reviews
            createReview(reviewRepository, alex, lasagna, 5, "Tastes closer to homemade than most frozen lasagnas.");
            createReview(reviewRepository, sam, lasagna, 5, "Really good sauce and texture for frozen lasagna.");

            // Mac and cheese: many reviews, lower average
            createReview(reviewRepository, demoUser, macAndCheese, 3, "Creamy, but a little bland.");
            createReview(reviewRepository, alex, macAndCheese, 4, "Comfort food. Not amazing, but dependable.");
            createReview(reviewRepository, jordan, macAndCheese, 3, "Fine as a side, but not enough as a meal.");

            // Dumplings: one review
            createReview(reviewRepository, sam, dumplings, 5, "Small portion, but the flavor is excellent.");

            // Ice cream bars intentionally has no reviews
            // This helps test empty review states.
        };
    }

    private User createUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String username,
            String email,
            String password
    ) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    private Product createProduct(
            ProductRepository productRepository,
            String name,
            String brand,
            String description
    ) {
        Product product = new Product();
        product.setName(name);
        product.setBrand(brand);
        product.setDescription(description);

        return productRepository.save(product);
    }

    private void createReview(
            ReviewRepository reviewRepository,
            User user,
            Product product,
            int rating,
            String text
    ) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setText(text);

        reviewRepository.save(review);
    }
}