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

  const [search, setSearch] = useState("");

  const [sort, setSort] = useState("");

  // React re-renders the UI when the state changes
  // When this page first loads, call getProducts()
  useEffect(() => {
    getProducts(sort)
      .then((data) => setProducts(data))
      .catch((err) => setError(err.message));
    // Only runs when the component first mounts because of the empty dependency array: []
    // Without that it would re run each time search changed.
  }, [sort]);

  // After retreiving all products filter based on search
  // When page re renders products is kept the same but the search filter is changed and this runs
  const filteredProducts = products.filter(
    (product) =>
      product.name.toLowerCase().includes(search.toLowerCase()) ||
      product.brand.toLowerCase().includes(search.toLowerCase()),
  );

  return (
    <main className="container">
      <header className="page-header">
        <div>
          <h1>Granker</h1>
          <p>Find and review frozen foods.</p>
        </div>
      </header>

      {/* Input is connect to search state. On a change setSearch changes the search state value.
        This change in state causes the page to re render */}
      <div className="search-bar">
        <input
          type="text"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search products..."
        />
        <label>
          Sort by:
          <select value={sort} onChange={(e) => setSort(e.target.value)}>
            <option value="name">Name</option>
            <option value="newest">Newest</option>
            <option value="highest-rating">Highest Rating</option>
            <option value="most-reviewed">Most Reviewed</option>
          </select>
        </label>
      </div>

      {error && <p className="error">{error}</p>}

      <section className="product-list">
        {filteredProducts.length === 0 ? (
          <p>No products found.</p>
        ) : (
          filteredProducts.map((product) => (
            // key helps react track each item efficiently. each item should have a unique key
            // product just passes in each product to ProductCard
            <ProductCard key={product.id} product={product}></ProductCard>
          ))
        )}
      </section>
    </main>
  );
}

export default ProductsPage;
