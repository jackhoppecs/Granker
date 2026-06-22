import { useState, useEffect } from "react";
// UseState = lets component remember/change data
// UseEffect = let's code run when the page loads
import { getProducts, getCategories, getBrands } from "../api/products";
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

  const [sort, setSort] = useState("name");
  const [minRating, setMinRating] = useState("");

  const [categories, setCategories] = useState([]);
  const [brands, setBrands] = useState([]);

  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedBrand, setSelectedBrand] = useState("");

  // React re-renders the UI when the state changes
  // When this page first loads, call getProducts()
  useEffect(() => {
    getProducts(sort, minRating, selectedCategory, selectedBrand)
      .then((data) => {
        setProducts(data);
        setError("");
      })
      .catch((err) => setError(err.message));
    // Only runs when the component first mounts because of the empty dependency array: []
    // Without that it would re run each time search changed.
  }, [sort, minRating, selectedCategory, selectedBrand]);

  // Grab filter options
  useEffect(() => {
    async function fetchFilterOptions() {
      try {
        const [categoriesData, brandsData] = await Promise.all([
          getCategories(),
          getBrands(),
        ]);

        setCategories(categoriesData);
        setBrands(brandsData);
      } catch (err) {
        setError(err.message);
      }
    }

    fetchFilterOptions();
  }, []);

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
      <div className="product-controls">
        <div className="control-group search-control">
          <label htmlFor="product-search">Search</label>
          <input
            id="product-search"
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search by product or brand..."
          />
        </div>

        <div className="control-group">
          <label htmlFor="product-sort">Sort by</label>
          <select
            id="product-sort"
            value={sort}
            onChange={(e) => setSort(e.target.value)}
          >
            <option value="name">Name</option>
            <option value="newest">Newest</option>
            <option value="highest-rating">Highest Rating</option>
            <option value="most-reviewed">Most Reviewed</option>
          </select>
        </div>

        <div className="control-group">
          <label htmlFor="min-rating">Minimum rating</label>
          <select
            id="min-rating"
            value={minRating}
            onChange={(e) => setMinRating(e.target.value)}
          >
            <option value="">Any Rating</option>
            <option value="5">5+</option>
            <option value="4">4+</option>
            <option value="3">3+</option>
            <option value="2">2+</option>
            <option value="1">1+</option>
          </select>
        </div>

        <div className="control-group">
          <label htmlFor="min-rating">Category</label>
          <select
            value={selectedCategory}
            onChange={(event) => setSelectedCategory(event.target.value)}
          >
            <option value="">All categories</option>
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
        </div>

        <div className="control-group">
          <label htmlFor="min-rating">Brand</label>
          <select
            value={selectedBrand}
            onChange={(event) => setSelectedBrand(event.target.value)}
          >
            <option value="">All brands</option>
            {brands.map((brand) => (
              <option key={brand} value={brand}>
                {brand}
              </option>
            ))}
          </select>
        </div>
      </div>

      {error && <p className="error">{error}</p>}

      <section className="product-list">
        {filteredProducts.length === 0 ? (
          <div className="empty-state">
            <h2>No products found</h2>
            <p>Try clearing your search or lowering the minimum rating.</p>
          </div>
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
