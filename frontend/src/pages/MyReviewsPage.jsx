import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyReviews } from "../api/reviews";

function MyReviewsPage({ currentUser }) {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadMyReviews() {
      try {
        const data = await getMyReviews();
        setReviews(data);
      } catch (err) {
        setError("Could not load your reviews.");
      } finally {
        setLoading(false);
      }
    }

    if (currentUser) {
      loadMyReviews();
    } else {
      setLoading(false);
    }
  }, [currentUser]);

  if (!currentUser) {
    return (
      <main className="page">
        <h1>My Reviews</h1>
        <p>Please log in to view your reviews.</p>
        <Link to="/login">Go to Login</Link>
      </main>
    );
  }

  if (loading) {
    return (
      <main className="page">
        <h1>My Reviews</h1>
        <p>Loading your reviews...</p>
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
    <main className="page">
      <h1>My Reviews</h1>

      {reviews.length === 0 ? (
        <p>You have not written any reviews yet.</p>
      ) : (
        <div className="review-list">
          {reviews.map((review) => (
            <article key={review.reviewId} className="review-card">
              <h2>{review.productName}</h2>
              <p>{review.productBrand}</p>

              <p>
                <strong>Rating:</strong> {review.rating}/5
              </p>

              <p>{review.text}</p>

              <p className="timestamp">
                Created: {new Date(review.createdAt).toLocaleString()}
              </p>

              <p className="timestamp">
                Updated: {new Date(review.updatedAt).toLocaleString()}
              </p>

              <Link to={`/products/${review.productId}`}>View product</Link>
            </article>
          ))}
        </div>
      )}
    </main>
  );
}
export default MyReviewsPage;
