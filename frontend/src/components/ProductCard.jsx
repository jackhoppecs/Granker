import { Link } from "react-router-dom";

// Components are usually a smaller reusable UI piece.
// So we reuse this ProductCard in our Products page
function ProductCard({ product }) {
  return (
    <Link className="product-card" to={`/products/${product.id}`}>
      {product.imageUrl ? (
        <img
          className="product-card-image"
          src={product.imageUrl}
          alt={`${product.brand} ${product.name}`}
        />
      ) : (
        <div className="product-card-image-placeholder">No image</div>
      )}

      <div className="product-card-content">
        <div className="product-card-header">
          <div>
            <p className="product-brand">{product.brand}</p>
            <h2>{product.name}</h2>
          </div>

          {product.category && (
            <span className="product-card-category">{product.category}</span>
          )}
        </div>

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

        {(product.calories != null || product.proteinGrams != null) && (
          <div className="product-card-nutrition">
            {product.calories != null && <span>{product.calories} cal</span>}

            {product.proteinGrams != null && (
              <span>{product.proteinGrams}g protein</span>
            )}
          </div>
        )}
      </div>
    </Link>
  );
}

export default ProductCard;
