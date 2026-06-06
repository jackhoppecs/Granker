function ReviewCard({ review }) {
  return (
    <article className="review-card">
      <div className="review-card-header">
        <p className="review-user">{review.username || "Anonymous"}</p>
        <p className="review-rating">{review.rating}/5</p>
      </div>

      <p className="review-text">{review.text}</p>
    </article>
  );
}

export default ReviewCard;
