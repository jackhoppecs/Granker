function ReviewCard({ review }) {
  return (
    <article className="card">
      <p className="rating">Rating: {review.rating}/5</p>
      <p>{review.text}</p>
    </article>
  );
}

export default ReviewCard;
