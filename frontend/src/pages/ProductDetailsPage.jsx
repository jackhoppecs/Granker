import { useState, useEffect } from "react";
// UseState = lets component remember/change data
// UseEffect = let's code run when the page loads
import { getProductById } from "../api/products";
import { getReviewsByProductId } from "../api/reviews";
import { useParams } from "react-router-dom";

function ProductDetailsPage() {
  const { id } = useParams();

  const [product, setProduct] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // useEffect runs code after the component renders
  useEffect(() => {
    async function loadProductDetails() {
      try {
        const productData = await getProductById(id);
        const reviewData = await getReviewsByProductId(id);

        setProduct(productData);
        setReviews(reviewData);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    loadProductDetails();
    // The [id] means run again if the id changes
  }, [id]);

  if (loading) {
    return <p>Loading product...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  return (
    <main className="container">
      <section className="card">
        <h1>{product.name}</h1>
        <p className="brand">{product.brand}</p>
        <p>{product.description}</p>
      </section>
      <section>
        <h2>Reviews</h2>

        {reviews.length === 0 && <p>No reviews yet.</p>}

        {reviews.map((review) => (
          <article className="card" key={review.id}>
            <p>Rating: {review.rating}/5</p>
            <p>{review.text}</p>
          </article>
        ))}
      </section>
    </main>
  );
}

export default ProductDetailsPage;
