import { Link } from "react-router-dom";

// Components are usually a smaller reusable UI piece.
// So we reuse this ProductCard in our Products page
function ProductCard({ product }) {
  return (
    <Link className="product-card" to={`/products/${product.id}`}>
      <h2>{product.name}</h2>
      <p className="product-brand">{product.brand}</p>
      <p className="product-description">{product.description}</p>
    </Link>
  );
}

export default ProductCard;
