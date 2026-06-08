import { useState } from "react";

function ReviewForm({ onSubmitReview }) {
  const [rating, setRating] = useState(5);
  const [text, setText] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();

    if (!text.trim()) {
      setFormError("Review text is required.");
      setSuccessMessage("");
      return;
    }

    const review = {
      rating: Number(rating),
      text,
    };

    try {
      setSubmitting(true);
      setFormError("");
      setSuccessMessage("");

      await onSubmitReview(review);
      setRating(5);
      setText("");
      setSuccessMessage("Review Submitted successfully.");
    } catch (err) {
      setFormError(
        err.message || "Something went wrong submitting your review.",
      );
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className="review-form" onSubmit={handleSubmit}>
      <h2>Add a Review</h2>

      {formError && <p className="form-error">{formError}</p>}
      {successMessage && <p className="form-success">{successMessage}</p>}

      <div className="form-group">
        <label htmlFor="rating">Rating</label>
        <select
          id="rating"
          value={rating}
          onChange={(event) => setRating(event.target.value)}
        >
          <option value="5">5 - Excellent</option>
          <option value="4">4 - Good</option>
          <option value="3">3 - Okay</option>
          <option value="2">2 - Bad</option>
          <option value="1">1 - Terrible</option>
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="review-text">Review</label>
        <textarea
          id="review-text"
          value={text}
          onChange={(event) => setText(event.target.value)}
          placeholder="What did you think?"
          disabled={submitting}
        ></textarea>
      </div>

      <button className="primary-button" type="submit" disabled={submitting}>
        {submitting ? "Submitting..." : "Submit Review"}
      </button>
    </form>
  );
}

export default ReviewForm;