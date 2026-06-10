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
    // A bean is an objec that spring creates and manages for you
    // Spring, call this method and store the returned object in the Spring application context
    @Bean
    // Runs when backend starts
    CommandLineRunner seedData(
        ProductRepository productRepository,
        ReviewRepository reviewRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ){
        // This is a java lambda
        // It's what runs when commandlinerunner.run() is called 
        return args -> {
            if (productRepository.count() > 0 || reviewRepository.count() > 0 || userRepository.count() > 0){
                    return;
            }
            
            User demoUser = new User();
            demoUser.setUsername("demo_user");
            demoUser.setEmail("demo@example.com");
            demoUser.setPassword("password");

            User savedUser = userRepository.save(demoUser);

            Product orangeChicken = new Product();
            orangeChicken.setName("Mandarin Orange Chicken");
            orangeChicken.setBrand("Trader Joe's");
            orangeChicken.setDescription("Crispy battered chicken pieces with a sweet and tangy orange sauce.");

            Product frozenPizza = new Product();
            frozenPizza.setName("Frozen Pepperoni Pizza");
            frozenPizza.setBrand("HEB");
            frozenPizza.setDescription("Classic frozen pepperoni pizza with mozzarella cheese and a crispy crust.");

            Product waffles = new Product();
            waffles.setName("Homestyle Waffles");
            waffles.setBrand("Eggo");
            waffles.setDescription("Frozen toaster waffles with a light, crisp texture.");

            Product burrito = new Product();
            burrito.setName("Bean and Cheese Burrito");
            burrito.setBrand("Amy's");
            burrito.setDescription("Organic bean and cheese burrito wrapped in a soft flour tortilla.");

            Product lasagna = new Product();
            lasagna.setName("Meat Lasagna");
            lasagna.setBrand("Rao's");
            lasagna.setDescription("Frozen meat lasagna with pasta, ricotta, mozzarella, and tomato sauce.");

            productRepository.save(orangeChicken);
            productRepository.save(frozenPizza);
            productRepository.save(waffles);
            productRepository.save(burrito);
            productRepository.save(lasagna);

            createReview(reviewRepository, savedUser, orangeChicken, 5, "Great flavor and easy to make. One of the better frozen meals.");
            createReview(reviewRepository, savedUser, orangeChicken, 4, "Sauce is really good, but it can be a little sweet.");

            createReview(reviewRepository, savedUser, frozenPizza, 3, "Decent for the price. Crust could be better.");
            createReview(reviewRepository, savedUser, waffles, 4, "Reliable breakfast option. Gets crispy in the toaster.");
            createReview(reviewRepository, savedUser, burrito, 4, "Simple but filling. Good quick lunch.");
            createReview(reviewRepository, savedUser, lasagna, 5, "Tastes closer to homemade than most frozen lasagnas.");
        };

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
