package backend.repository;

import backend.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// JPA repostiory basically gives you a bunch of functions for free that are basically all CRUD
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByNameAsc();

    List<Product> findAllByOrderByCreatedAtDesc();

    // Select p means return product objects, even though we are using reviews to sort we still return products
    // p is just an alias so instead of writing Product each time we use p
    // A LEFT JOIN keeps every Product from the left side, even if it has no matching Review
    // The left table is the table right after FROM (Product aka p)
    // The right table is Review aka r
    // On is the matching rule so where r.product or the product a review belongs to equals the product in the left table
    // When a product has a matching review it creates a new row
    // after the join a product with 3 reviews appears in three joined rows
    // Orange Chicken | 4
    // Orange Chicken | 5
    // Orange Chicken | 4
    // Group by p collapses those rows back into one product group
    // Orange Chicken | 4, 5, 4
    // Now the db can calculate AVG(r.rating) Count(r)
    // COALESCE either calculates the average or if it can't sets avg to 0 and sort descending
    // If there are ties it then uses count and finally name for tiebreakers
    @Query("""
        SELECT p
        FROM Product p
        LEFT JOIN Review r ON r.product = p
        GROUP BY p
        ORDER BY COALESCE(AVG(r.rating), 0) DESC, COUNT(r) DESC, p.name ASC
    """)
    List<Product> findAllOrderByAverageRatingDesc();

    @Query("""
        SELECT p
        FROM Product p
        LEFT JOIN Review r ON r.product = p
        GROUP BY p
        ORDER BY COUNT(r) DESC, COALESCE(AVG(r.rating), 0) DESC, p.name ASC
    """)
    List<Product> findAllOrderByReviewCountDesc();
}
