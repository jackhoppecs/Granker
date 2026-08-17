import { useState, useEffect } from "react";
// UseState = lets component remember/change data
// UseEffect = let's code run when the page loads
import { getProductById } from "../api/products";
import { getReviewsByProductId } from "../api/reviews";
import { createReview } from "../api/reviews";
import { updateReview } from "../api/reviews";
import { deleteReview } from "../api/reviews";
import { useParams, Link } from "react-router-dom";
import ReviewCard from "../components/ReviewCard";
import ReviewForm from "../components/ReviewForm";
import LoadingState from "../components/LoadingState";
import Toast from "../components/Toast";

function ProductDetailsPage({ currentUser }) {
  const { id } = useParams();

  const [product, setProduct] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [productLoading, setProductLoading] = useState(true);
  const [productError, setProductError] = useState("");

  const [reviewsLoading, setReviewsLoading] = useState(true);
  // Error for retrieving reviews
  const [reviewsError, setReviewsError] = useState("");
  // Error for actions on reviews (deleting/updating/submitting)
  const [reviewActionError, setReviewActionError] = useState("");

  const [editingReviewId, setEditingReviewId] = useState(null);
  const [sort, setSort] = useState("");
  const [imageFailed, setImageFailed] = useState(false);

  // 404 error
  const [productNotFound, setProductNotFound] = useState(false);

  const [toast, setToast] = useState(null);

  function showToast(message, type = "success") {
    setToast({ message, type });
  }

  // useEffect for loading the product
  useEffect(() => {
    async function loadProduct() {
      setProductLoading(true);
      setProductError("");
      setImageFailed(false);
      setReviewActionError("");
      setProductNotFound(false);

      try {
        // if (import.meta.env.DEV) {
        //   await new Promise((resolve) => setTimeout(resolve, 1500));
        // }

        const productData = await getProductById(id);
        setProduct(productData);
      } catch (err) {
        if (err.status === 404) {
          setProductNotFound(true);
        } else {
          setProductError(err.message);
        }
      } finally {
        setProductLoading(false);
      }
    }

    loadProduct();
  }, [id]);

  // useEffect for loading the reviews
  useEffect(() => {
    async function loadReviews() {
      setReviewsLoading(true);
      setReviewsError("");

      try {
        // if (import.meta.env.DEV) {
        //   await new Promise((resolve) => setTimeout(resolve, 1500));
        // }

        const reviewData = await getReviewsByProductId(id, sort);
        setReviews(reviewData);
      } catch (err) {
        setReviewsError(err.message);
      } finally {
        setReviewsLoading(false);
      }
    }
    loadReviews();
  }, [id, sort]);

  if (productLoading) {
    return (
      <main className="container">
        <LoadingState message="Loading product..." />
      </main>
    );
  }

  if (productNotFound) {
    return (
      <main className="container">
        <div className="error-state">
          <h1>Product not found</h1>
          <p>
            This product may have been removed or the link may be incorrect.
          </p>
          <Link to="/">Return to products</Link>
        </div>
      </main>
    );
  }

  if (productError) {
    return (
      <main className="container">
        <div className="error-state">
          <h1>Unable to load product</h1>
          <p>{productError}</p>
          <Link to="/">Return to products</Link>
        </div>
      </main>
    );
  }

  async function handleSubmitReview(review) {
    try {
      setReviewActionError("");

      const createdReview = await createReview(id, review);
      setReviews((currentReviews) => [createdReview, ...currentReviews]);

      await refreshProductSummary();
      showToast("Review added successfully.", "success");
    } catch (err) {
      if (err.status === 401) {
        setReviewActionError(
          "Your session expired, so you can no longer submit a review. Please log in again.",
        );
        return;
      }
      setReviewActionError(err.message || "Unable to submit review.");
    }
  }

  async function handleUpdateReview(reviewId, updatedReviewData) {
    try {
      setReviewActionError("");

      const updatedReview = await updateReview(reviewId, updatedReviewData);
      setReviews((prevReviews) =>
        prevReviews.map((r) => (r.id === reviewId ? updatedReview : r)),
      );
      // close edit mode
      setEditingReviewId(null);
      await refreshProductSummary();
      showToast("Review successfully updated.", "success");
    } catch (err) {
      if (err.status === 401) {
        setReviewActionError(
          "Your session expired, so you can no longer update this review. Please log in again.",
        );
        return;
      }
      setReviewActionError(err.message || "Unable to update review.");
    }
  }

  async function handleDeleteReview(reviewId) {
    try {
      setReviewActionError("");

      await deleteReview(reviewId);
      setReviews((prevReviews) => prevReviews.filter((r) => r.id !== reviewId));
      await refreshProductSummary();
      showToast("Review successfully deleted.", "success");
    } catch (err) {
      if (err.status === 401) {
        setReviewActionError(
          "Your session expired, so you can no longer delete this review. Please log in again.",
        );
        return;
      }
      setReviewActionError(err.message || "Unable to delete review.");
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

  const hasNutrition =
    product.calories != null ||
    product.proteinGrams != null ||
    product.carbGrams != null ||
    product.fatGrams != null;

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

        {toast && (
          <Toast
            message={toast.message}
            type={toast.type}
            onClose={() => setToast(null)}
          />
        )}

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

        {/* NEW METADATA */}
        {product.category && (
          <p className="product-category">Category: {product.category}</p>
        )}
        <div className="product-detail-image-container">
          {product.imageUrl && !imageFailed ? (
            <img
              className="product-detail-image"
              src={product.imageUrl}
              alt={`${product.brand} ${product.name}`}
              onError={() => setImageFailed(true)}
            />
          ) : (
            <div className="product-detail-image-placeholder">
              No image available
            </div>
          )}
        </div>

        {hasNutrition && (
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
        )}

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
      {!reviewsLoading && (
        <>
          {!currentUser ? (
            <p className="empty-state">
              <Link className="login" to="/login">
                Log in
              </Link>{" "}
              to your account or{" "}
              <Link className="register" to="/register">
                create a new account
              </Link>{" "}
              to write a review.
            </p>
          ) : currentUserReview ? (
            <p className="empty-state">
              You already reviewed this product. You can edit or delete your
              review below.
            </p>
          ) : (
            <section>
              <ReviewForm onSubmitReview={handleSubmitReview} />
            </section>
          )}
        </>
      )}
      {reviewActionError && (
        <p className="form-error" role="alert">
          {reviewActionError}
        </p>
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
        {reviewsLoading ? (
          <LoadingState message="Loading reviews..." />
        ) : reviewsError ? (
          <div className="error-state">
            <h2>Unable to load reviews</h2>
            <p>{reviewsError}</p>
          </div>
        ) : reviews.length === 0 ? (
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
