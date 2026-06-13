import { useState } from "react";

function ReviewCard({
  review,
  currentUser,
  editingReviewId,
  setEditingReviewId,
  onUpdateReview,
  onDeleteReview,
}) {
  const [rating, setRating] = useState(review.rating);
  const [text, setText] = useState(review.text);

  const isEditing = editingReviewId === review.id;
  const isOwner = currentUser && currentUser.username === review.username;

  function handleEditClick() {
    setRating(review.rating);
    setText(review.text);
    setEditingReviewId(review.id);
  }

  function handleCancelClick() {
    setRating(review.rating);
    setText(review.text);
    setEditingReviewId(null);
  }

  function handleSaveClick() {
    onUpdateReview(review.id, {
      rating,
      text,
    });
  }

  function handleDeleteClick() {
    // Built in browser function that opens a single confirmation popup
    // returns a boolean
    const confirmed = window.confirm(
      "Are you sure you want to delete this review?",
    );

    if (!confirmed) {
      return;
    }
    onDeleteReview(review.id);
  }

  if (isEditing) {
    return (
      <article className="review-card review-card-editing">
        <div className="review-form-group">
          <label htmlFor={`rating-${review.id}`}>Rating</label>
          <select
            id={`rating-${review.id}`}
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
          <label htmlFor={`text-${review.id}`}>Review</label>
          <textarea
            id={`text-${review.id}`}
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
        <p className="review-user">{review.username || "Anonymous"}</p>
        <p className="review-rating">{review.rating}/5</p>
      </div>

      <p className="review-text">{review.text}</p>

      {review.updatedAt && (
        <p className="review-date">
          Updated {new Date(review.updatedAt).toLocaleDateString()}
        </p>
      )}

      {isOwner && !isEditing && (
        <div className="review-actions">
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
      )}
    </article>
  );
}

export default ReviewCard;
