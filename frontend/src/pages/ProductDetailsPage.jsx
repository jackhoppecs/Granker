import { useState, useEffect } from "react";
// UseState = lets component remember/change data
// UseEffect = let's code run when the page loads
import { getProductById } from "../api/products";
import { getReviewsByProductId } from "../api/reviews";
import { createReview } from "../api/reviews";
import { updateReview } from "../api/reviews";
import { deleteReview } from "../api/reviews";
import { useParams } from "react-router-dom";
import ReviewCard from "../components/ReviewCard";
import ReviewForm from "../components/ReviewForm";

function ProductDetailsPage({ currentUser }) {
  const { id } = useParams();

  const [product, setProduct] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [editingReviewId, setEditingReviewId] = useState(null);

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

  async function handleSubmitReview(review) {
    try {
      const createdReview = await createReview(id, review);
      setReviews((currentReviews) => [createdReview, ...currentReviews]);
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleUpdateReview(reviewId, updatedReviewData) {
    try {
      const updatedReview = await updateReview(reviewId, updatedReviewData);
      setReviews((prevReviews) =>
        prevReviews.map((r) => (r.id === reviewId ? updatedReview : r)),
      );
      // close edit mode
      setEditingReviewId(null);
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleDeleteReview(reviewId) {
    try {
      await deleteReview(reviewId);
      setReviews((prevReviews) => prevReviews.filter((r) => r.id !== reviewId));
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <main className="container">
      <section className="product-detail-card">
        <div className="product-detail-header">
          <div>
            <p className="product-detail-brand">{product.brand}</p>
            <h1>{product.name}</h1>
          </div>

          <span className="product-detail-badge">Frozen Food</span>
        </div>

        <div className="product-rating-summary">
          {product.reviewCount > 0 ? (
            <>
              <p className="product-average-rating">
                Average Rating: {product.averageRating.toFixed(1)} / 5
              </p>
              <p className="product-review-count">
                {product.reviewCount}{" "}
                {product.reviewCount === 1 ? "review" : "reviews"}
              </p>
            </>
          ) : (
            <p className="product-review-count">No ratings yet</p>
          )}
        </div>

        <p className="product-detail-description">{product.description}</p>
      </section>

      {currentUser ? (
        <section>
          <ReviewForm onSubmitReview={handleSubmitReview} />
        </section>
      ) : (
        <p className="empty-state">Log in to write a review.</p>
      )}

      <section className="reviews-section">
        <h2>Reviews</h2>

        {reviews.length === 0 ? (
          <p className="empty-state">No reviews yet.</p>
        ) : (
          reviews.map((review) => (
            // When you pass props down, the left-hand side is the name the child component will see, the right-hand side is the value from the parent.
            <ReviewCard
              key={review.id}
              review={review}
              currentUser={currentUser}
              editingReviewId={editingReviewId}
              setEditingReviewId={setEditingReviewId}
              onUpdateReview={handleUpdateReview}
              onDeleteReview={handleDeleteReview}
            />
          ))
        )}
      </section>
    </main>
  );
}

export default ProductDetailsPage;
