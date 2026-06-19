import { Link } from "react-router-dom";
import { useState } from "react";
import MyReviewsPage from "../pages/MyReviewsPage";

function MyReviewCard({
  review,
  editingReviewId,
  setEditingReviewId,
  onUpdateReview,
  onDeleteReview,
}) {
  const [rating, setRating] = useState(review.rating);
  const [text, setText] = useState(review.text);

  const isEditing = editingReviewId === review.reviewId;

  function handleEditClick() {
    setRating(review.rating);
    setText(review.text);
    setEditingReviewId(review.reviewId);
  }

  function handleCancelClick() {
    setRating(review.rating);
    setText(review.text);
    setEditingReviewId(null);
  }

  function handleSaveClick() {
    onUpdateReview(review.reviewId, {
      rating,
      text,
    });
  }

  function handleDeleteClick() {
    const confirmed = window.confirm(
      "Are you sure you want to delete this review?",
    );

    if (!confirmed) {
      return;
    }

    onDeleteReview(review.reviewId);
  }

  if (isEditing) {
    return (
      <article className="review-card review-card-editing">
        <div className="review-form-group">
          <label htmlFor={`rating-${review.reviewId}`}>Rating</label>
          <select
            id={`rating-${review.reviewId}`}
            value={rating}
            onChange={(e) => setRating(Number(e.target.value))}
          >
            <option value={1}>1</option>
            <option value={2}>2</option>
            <option value={3}>3</option>
            <option value={4}>4</option>
            <option value={5}>5</option>
          </select>
        </div>

        <div className="review-form-group">
          <label htmlFor={`text-${review.reviewId}`}>Review</label>
          <textarea
            id={`text-${review.reviewId}`}
            value={text}
            onChange={(e) => setText(e.target.value)}
          />
        </div>

        <div className="review-actions">
          <button
            type="button"
            className="review-save-button"
            onClick={handleSaveClick}
          >
            Save
          </button>

          <button
            type="button"
            className="review-cancel-button"
            onClick={handleCancelClick}
          >
            Cancel
          </button>
        </div>
      </article>
    );
  }

  return (
    <article className="review-card">
      <div className="review-card-header">
        <div>
          <h2>{review.productName}</h2>
          <p>{review.productBrand}</p>
        </div>

        <p className="review-rating">{review.rating}/5</p>
      </div>

      <p className="review-text">{review.text}</p>

      <p className="review-date">
        Created {new Date(review.createdAt).toLocaleDateString()}
      </p>

      {review.updatedAt && (
        <p className="review-date">
          Updated {new Date(review.updatedAt).toLocaleDateString()}
        </p>
      )}

      <div className="review-actions">
        <Link to={`/products/${review.productId}`}>View product</Link>

        <button
          type="button"
          className="review-edit-button"
          onClick={handleEditClick}
        >
          Edit
        </button>

        <button
          type="button"
          className="review-delete-button"
          onClick={handleDeleteClick}
        >
          Delete
        </button>
      </div>
    </article>
  );
}

export default MyReviewCard;
