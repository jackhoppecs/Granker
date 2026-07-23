import { useState, useEffect } from "react";
// UseState = lets component remember/change data
// UseEffect = let's code run when the page loads
import { getProducts, getCategories, getBrands } from "../api/products";
import ProductCard from "../components/ProductCard";
import LoadingState from "../components/LoadingState";

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
  const [loading, setLoading] = useState(false);
  const [filtersLoading, setFiltersLoading] = useState(true);
  const [filtersError, setFiltersError] = useState("");

  // React re-renders the UI when the state changes
  // When this page first loads, call getProducts()
  useEffect(() => {
    setLoading(true);
    // Creates a promise that is already successful
    // Think of it as starting a promise chain with no meaningful value
    Promise.resolve()
      // .then(() => {
      //   if (import.meta.env.DEV) {
      //     return new Promise((resolve) => setTimeout(resolve, 1500));
      //   }
      // })
      .then(() => getProducts(sort, minRating, selectedCategory, selectedBrand))
      .then((data) => {
        setProducts(data);
        setError("");
        setLoading(false);
      })
      .catch((err) => setError(err.message))
      .finally(() => {
        setLoading(false);
      });
  }, [sort, minRating, selectedCategory, selectedBrand]);

  // Grab filter options
  useEffect(() => {
    async function fetchFilterOptions() {
      setFiltersLoading(true);
      setFiltersError("");

      try {
        // if (import.meta.env.DEV) {
        //   await new Promise((resolve) => setTimeout(resolve, 1500));
        // }
        const [categoriesData, brandsData] = await Promise.all([
          getCategories(),
          getBrands(),
        ]);

        setCategories(categoriesData);
        setBrands(brandsData);
      } catch (err) {
        setError(err.message);
      } finally {
        setFiltersLoading(false);
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

  function delay(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

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
          <label htmlFor="product-category">Category</label>
          <select
            id="product-category"
            value={selectedCategory}
            onChange={(event) => setSelectedCategory(event.target.value)}
            disabled={filtersLoading}
          >
            {filtersLoading ? (
              <option value="">Loading categories...</option>
            ) : (
              <>
                <option value="">All categories</option>
                {categories.map((category) => (
                  <option key={category} value={category}>
                    {category}
                  </option>
                ))}
              </>
            )}
          </select>
        </div>

        <div className="control-group">
          <label htmlFor="product-brand">Brand</label>
          <select
            id="product-brand"
            value={selectedBrand}
            onChange={(event) => setSelectedBrand(event.target.value)}
            disabled={filtersLoading}
          >
            {filtersLoading ? (
              <option value="">Loading brands...</option>
            ) : (
              <>
                <option value="">All brands</option>
                {brands.map((brand) => (
                  <option key={brand} value={brand}>
                    {brand}
                  </option>
                ))}
              </>
            )}
          </select>
        </div>
      </div>

      <section className="product-list">
        {loading ? (
          <LoadingState message="Loading Products..."></LoadingState>
        ) : error ? (
          <div className="error-state">
            <h2>Unable to load products</h2>
            <p>{error}</p>
          </div>
        ) : filteredProducts.length === 0 ? (
          <div className="empty-state">
            <h2>No products found</h2>
            <p>Try clearing your search or changing your filters.</p>
          </div>
        ) : (
          filteredProducts.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))
        )}
      </section>
    </main>
  );
}

export default ProductsPage;
