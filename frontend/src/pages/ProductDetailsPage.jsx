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
  const [sort, setSort] = useState("");

  // useEffect runs code after the component renders
  useEffect(() => {
    async function loadProductDetails() {
      try {
        const productData = await getProductById(id);
        const reviewData = await getReviewsByProductId(id, sort);

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
  }, [id, sort]);

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

      await refreshProductSummary();
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
      await refreshProductSummary();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleDeleteReview(reviewId) {
    try {
      await deleteReview(reviewId);
      setReviews((prevReviews) => prevReviews.filter((r) => r.id !== reviewId));
      await refreshProductSummary();
    } catch (err) {
      setError(err.message);
    }
  }

  async function refreshProductSummary() {
    const updatedProduct = await getProductById(id);
    // State changes meaning page re renders except useEffect because it depends on id
    setProduct(updatedProduct);
  }

  const currentUserReview = currentUser
    ? reviews.find((review) => review.username === currentUser.username)
    : null;

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
                ★ {product.averageRating.toFixed(1)} / 5
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

        {product.category && (
          <p className="product-category">Category: {product.category}</p>
        )}
        {product.imageUrl ? (
          <img
            className="product-detail-image"
            src={product.imageUrl}
            alt={`${product.brand} ${product.name}`}
          />
        ) : (
          <div className="product-detail-image-placeholder">
            No image available
          </div>
        )}
        <div className="nutrition-section">
          <h3>Nutrition</h3>

          <div className="nutrition-grid">
            {product.calories != null && (
              <div>
                <strong>{product.calories}</strong>
                <span>Calories</span>
              </div>
            )}

            {product.proteinGrams != null && (
              <div>
                <strong>{product.proteinGrams}g</strong>
                <span>Protein</span>
              </div>
            )}

            {product.carbGrams != null && (
              <div>
                <strong>{product.carbGrams}g</strong>
                <span>Carbs</span>
              </div>
            )}

            {product.fatGrams != null && (
              <div>
                <strong>{product.fatGrams}g</strong>
                <span>Fat</span>
              </div>
            )}
          </div>
        </div>

        {product.sourceName && (
          <p className="source-attribution">
            Product information source:{" "}
            {product.sourceUrl ? (
              <a href={product.sourceUrl} target="_blank" rel="noreferrer">
                {product.sourceName}
              </a>
            ) : (
              product.sourceName
            )}
          </p>
        )}
      </section>

      {!currentUser ? (
        <p className="empty-state">Log in to write a review.</p>
      ) : currentUserReview ? (
        <p className="empty-state">
          You already reviewed this product. You can edit or delete your review
          below.
        </p>
      ) : (
        <section>
          <ReviewForm onSubmitReview={handleSubmitReview} />
        </section>
      )}

      <section className="reviews-section">
        <div className="section-header reviews-header">
          <div>
            <h2>Reviews</h2>
          </div>
          <div className="control-group review-sort-control">
            <label htmlFor="review-sort">Sort by</label>
            <select
              id="review-sort"
              value={sort}
              onChange={(e) => setSort(e.target.value)}
            >
              <option value="newest">Newest</option>
              <option value="highest-rating">Highest Rating</option>
              <option value="lowest-rating">Lowest Rating</option>
            </select>
          </div>
        </div>
        {reviews.length === 0 ? (
          <div className="empty-state">
            <h2>No reviews yet</h2>
            <p>Be the first to review this product.</p>
          </div>
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
