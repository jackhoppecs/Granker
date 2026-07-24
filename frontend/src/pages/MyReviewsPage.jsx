import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyReviews, updateReview, deleteReview } from "../api/reviews";
import MyReviewCard from "../components/MyReviewCard";
import LoadingState from "../components/LoadingState";

function MyReviewsPage({ currentUser }) {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [editingReviewId, setEditingReviewId] = useState(null);

  useEffect(() => {
    async function loadMyReviews() {
      try {
        setLoading(true);
        setError("");
        // if (import.meta.env.DEV) {
        //   await new Promise((resolve) => setTimeout(resolve, 1500));
        // }
        const data = await getMyReviews();
        setReviews(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    if (currentUser) {
      loadMyReviews();
    } else {
      setReviews([]);
      setLoading(false);
    }
  }, [currentUser]);

  async function handleUpdateReview(reviewId, updatedReviewData) {
    try {
      const updatedReview = await updateReview(reviewId, updatedReviewData);

      setReviews((prevReviews) =>
        prevReviews.map((r) =>
          r.reviewId === reviewId
            ? {
                ...r,
                rating: updatedReview.rating,
                text: updatedReview.text,
                updatedAt: updatedReview.updatedAt,
              }
            : r,
        ),
      );

      setEditingReviewId(null);
      setError("");
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleDeleteReview(reviewId) {
    try {
      await deleteReview(reviewId);

      setReviews((prevReviews) =>
        prevReviews.filter((r) => r.reviewId !== reviewId),
      );
      setError("");
    } catch (err) {
      setError(err.message);
    }
  }

  if (loading) {
    return (
      <main className="page my-reviews-page">
        <header className="page-header">
          <div>
            <h1>My Reviews</h1>
            <p>
              Manage the reviews you have written across different products.
            </p>
          </div>
        </header>

        <LoadingState message="Loading your reviews..." />
      </main>
    );
  }

  if (error) {
    return (
      <main className="page">
        <h1>My Reviews</h1>
        <p className="error-message">{error}</p>
      </main>
    );
  }

  return (
    <main className="page my-reviews-page">
      <header className="page-header">
        <h1>My Reviews</h1>
        <p>Manage the reviews you have written across different products.</p>
      </header>

      {reviews.length === 0 ? (
        <div className="empty-state">
          <h2>No reviews yet</h2>
          <p>Reviews you write will appear here.</p>
          <Link to="/products">Browse products</Link>
        </div>
      ) : (
        <div className="my-reviews-list">
          {reviews.map((review) => (
            <MyReviewCard
              key={review.reviewId}
              review={review}
              editingReviewId={editingReviewId}
              setEditingReviewId={setEditingReviewId}
              onUpdateReview={handleUpdateReview}
              onDeleteReview={handleDeleteReview}
            />
          ))}
        </div>
      )}
    </main>
  );
}
export default MyReviewsPage;
