import { useState, useEffect } from "react";
// UseState = lets component remember/change data
// UseEffect = let's code run when the page loads
import { getProducts, getCategories, getBrands } from "../api/products";
import ProductCard from "../components/ProductCard";
import LoadingState from "../components/LoadingState";
import { useSearchParams } from "react-router-dom";

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

  // Represents everything after ? : /products?sort=highest-rating&minRating=4
  const [searchParams, setSearchParams] = useSearchParams();
  const sort = searchParams.get("sort") || "name";
  const minRating = searchParams.get("minRating") || "";
  const selectedCategory = searchParams.get("category") || "";
  const selectedBrand = searchParams.get("brand") || "";
  const search = searchParams.get("search") || "";

  const [searchInput, setSearchInput] = useState(
    searchParams.get("search") || "",
  );

  const [categories, setCategories] = useState([]);
  const [brands, setBrands] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filtersLoading, setFiltersLoading] = useState(true);
  const [filtersError, setFiltersError] = useState("");

  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);

  const [retrying, setRetrying] = useState(false);

  function updateFilter(name, value) {
    const params = new URLSearchParams(searchParams);

    if (value) {
      params.set(name, value);
    } else {
      params.delete(name);
    }

    setSearchParams(params);
  }

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
      .then(() =>
        getProducts(
          sort,
          minRating,
          selectedCategory,
          selectedBrand,
          search,
          0,
          10,
        ),
      )
      .then((data) => {
        setProducts(data.products);
        setHasMore(data.hasMore);
        setPage(0);
        setError("");
        setLoading(false);
      })
      .catch((err) => setError(err.message))
      .finally(() => {
        setLoading(false);
      });
  }, [sort, minRating, selectedCategory, selectedBrand, search]);

  // Grab filter options (Brands / Categories)
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

  // Detects if a filter is active
  // Don't need to include sort because it doesn't remove products
  const hasActiveFilters =
    search.trim() !== "" ||
    minRating !== "" ||
    selectedCategory !== "" ||
    selectedBrand !== "";

  // After retreiving all products filter based on search
  // When page re renders products is kept the same but the search filter is changed and this runs
  // const filteredProducts = products.filter(
  //   (product) =>
  //     product.name.toLowerCase().includes(search.toLowerCase()) ||
  //     product.brand.toLowerCase().includes(search.toLowerCase()),
  // );

  function delay(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  async function handleLoadMore() {
    const nextPage = page + 1;

    try {
      setLoadingMore(true);

      const data = await getProducts(
        sort,
        minRating,
        selectedCategory,
        selectedBrand,
        search,
        nextPage,
        5,
      );

      setProducts((currentProducts) => [...currentProducts, ...data.products]);

      setHasMore(data.hasMore);
      setPage(nextPage);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoadingMore(false);
    }
  }

  function handleSearch() {
    updateFilter("search", searchInput.trim());
  }

  async function handleRetryProducts() {
    if (retrying) return;

    try {
      setRetrying(true);

      const data = await getProducts(
        sort,
        minRating,
        selectedCategory,
        selectedBrand,
        search,
        0,
        20,
      );

      setProducts(data.products);
      setHasMore(data.hasMore);
      setPage(0);
      setError("");
    } catch (err) {
      setError(err.message);
    } finally {
      setRetrying(false);
    }
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
          <div className="search-row">
            <input
              id="product-search"
              type="text"
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              placeholder="Search by product or brand..."
            />

            <button type="button" onClick={handleSearch}>
              Search
            </button>
          </div>
        </div>

        <div className="control-group">
          <label htmlFor="product-sort">Sort by</label>
          <select
            id="product-sort"
            value={sort}
            onChange={(e) => updateFilter("sort", e.target.value)}
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
            onChange={(e) => updateFilter("minRating", e.target.value)}
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
            onChange={(event) => updateFilter("category", event.target.value)}
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
            onChange={(event) => updateFilter("brand", event.target.value)}
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

      {loading ? (
        <LoadingState message="Loading products..." />
      ) : error ? (
        <div className="error-state">
          <h2>Unable to load products</h2>
          <p>{error}</p>
          <button
            type="button"
            onClick={handleRetryProducts}
            disabled={retrying}
          >
            {retrying ? "Trying again..." : "Try Again"}
          </button>
        </div>
      ) : products.length === 0 ? (
        hasActiveFilters ? (
          <div className="empty-state">
            <h2>No matching products</h2>
            <p>Try clearing your search or changing your filters.</p>
          </div>
        ) : (
          <div className="empty-state">
            <h2>No products available yet</h2>
            <p>Products will appear here once they have been added.</p>
          </div>
        )
      ) : (
        <section>
          <div className="product-list">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>

          {hasMore && (
            <div className="load-more-container">
              <button
                type="button"
                onClick={handleLoadMore}
                disabled={loadingMore}
              >
                {loadingMore ? "Loading..." : "Load More"}
              </button>
            </div>
          )}
        </section>
      )}
    </main>
  );
}

export default ProductsPage;
