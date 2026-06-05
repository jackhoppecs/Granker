function ReviewCard({ review }) {
  return (
    <article className="card">
      <p>Name: {review.username} </p>
      <p className="rating">Rating: {review.rating}/5</p>
      <p>{review.text}</p>
    </article>
  );
}

export default ReviewCard;
