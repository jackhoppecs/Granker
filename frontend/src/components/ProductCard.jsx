import { Link } from "react-router-dom";

// Components are usually a smaller reusable UI piece.
// So we reuse this ProductCard in our Products page
function ProductCard({ product }) {
  return (
    <Link className="product-card" to={`/products/${product.id}`}>
      <h2>{product.name}</h2>
      <p className="product-brand">{product.brand}</p>
      <p className="product-description">{product.description}</p>
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
    </Link>
  );
}

export default ProductCard;
