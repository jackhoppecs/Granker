function ProductCard({ product }) {
  return (
    <article className="card">
      <h2>{product.name}</h2>
      <p className="brand">{product.brand}</p>
      <p>{product.description}</p>
    </article>
  );
}

export default ProductCard;
