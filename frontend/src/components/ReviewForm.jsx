import { useState } from "react";

function ReviewForm({ onSubmitReview }) {
  const [rating, setRating] = useState(5);
  const [text, setText] = useState("");

  function handleSubmit(event) {
    event.preventDefault();

    const review = {
      rating: Number(rating),
      text,
    };

    onSubmitReview(review);

    setRating(5);
    setText("");
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <h2>Add a Review</h2>

      <label>
        Rating
        <select
          value={rating}
          onChange={(event) => setRating(event.target.value)}
        >
          <option value="5">5 - Excellent</option>
          <option value="4">4 - Good</option>
          <option value="3">3 - Okay</option>
          <option value="2">2 - Bad</option>
          <option value="1">1 - Terrible</option>
        </select>
      </label>

      <label>
        Review
        <textarea
          value={text}
          onChange={(event) => setText(event.target.value)}
          placeholder="What did you think?"
        ></textarea>
      </label>
      <button type="submit">Submit Review</button>
    </form>
  );
}

export default ReviewForm;
