import { useState, useEffect } from "react";
// UseState = lets component remember/change data
// UseEffect = let's code run when the page loads
import { getProducts } from "../api/products";
import ProductCard from "../components/ProductCard";

// A page is a component that represents a whole screen / route
// Pages usually connected to react router
function ProductsPage() {
  // These are react states
  // products starts as an empty array
  // products is the current value, setProducts is the function used to update products
  const [products, setProducts] = useState([]);
  // error starts as an empty string
  // error is the current value, setError is the function used to update error
  const [error, setError] = useState("");
  // React re-renders the UI when the state changes

  // When this page first loads, call getProducts()
  useEffect(() => {
    getProducts()
      .then((data) => setProducts(data))
      .catch((err) => setError(err.message));
  }, []);

  return (
    <main className="container">
      <header className="page-header">
        <div>
          <h1>Granker</h1>
          <p>Find and review frozen foods.</p>
        </div>
      </header>

      {error && <p className="error">{error}</p>}

      <section className="product-list">
        {products.map((product) => (
          // key helps react track each item efficiently. each item should have a unique key
          // product just passes in each product to ProductCard
          <ProductCard key={product.id} product={product}></ProductCard>
        ))}
      </section>
    </main>
  );
}

export default ProductsPage;
