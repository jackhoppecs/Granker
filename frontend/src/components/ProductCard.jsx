import { Link } from "react-router-dom";


// Components are usually a smaller reusable UI piece.
// So we reuse this ProductCard in our Products page
function ProductCard({ product }) {
  return (
    <article className="card">
      <h2>{product.name}</h2>
      <p className="brand">{product.brand}</p>
      <p>{product.description}</p>

      <Link to={`/products/${product.id}`}>View Details</Link>
    </article>
  );
}

export default ProductCard;
