import { useState } from "react";
import {
  previewOpenFoodFactsImport,
  importOpenFoodFactsProducts,
} from "../api/import";
import LoadingState from "../components/LoadingState";

function ImportPreviewPage() {
  const [category, setCategory] = useState("frozen-pizza");
  const [pageSize, setPageSize] = useState(10);
  const [preview, setPreview] = useState(null);
  const [importResult, setImportResult] = useState(null);
  const [error, setError] = useState("");
  const [isPreviewLoading, setIsPreviewLoading] = useState(false);
  const [isImportLoading, setIsImportLoading] = useState(false);
  const [hasPreviewed, setHasPreviewed] = useState(false);

  async function handlePreview() {
    setIsPreviewLoading(true);
    setError("");
    setImportResult(null);
    setHasPreviewed(true);

    try {
      const data = await previewOpenFoodFactsImport(category, pageSize);
      setPreview(data);
    } catch (err) {
      setError(err.message);
      setPreview(null);
    } finally {
      setIsPreviewLoading(false);
    }
  }

  async function handleImport() {
    setIsImportLoading(true);
    setError("");

    try {
      const result = await importOpenFoodFactsProducts(category, pageSize);
      setImportResult(result);

      const refreshedPreview = await previewOpenFoodFactsImport(
        category,
        pageSize,
      );
      setPreview(refreshedPreview);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsImportLoading(false);
    }
  }

  return (
    <main>
      <h1>Import Products</h1>

      <label htmlFor="import-category">
        Category
        <select
          id="import-category"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
        >
          <option value="frozen-pizza">Pizza</option>
          <option value="frozen-meals">Frozen Meals</option>
          <option value="ice-cream">Ice Cream</option>
        </select>
      </label>

      <label htmlFor="import-page-size">
        Page Size
        <select
          id="import-page-size"
          value={pageSize}
          onChange={(e) => setPageSize(Number(e.target.value))}
        >
          <option value={5}>5</option>
          <option value={10}>10</option>
          <option value={25}>25</option>
          <option value={50}>50</option>
        </select>
      </label>

      <button onClick={handlePreview} disabled={isPreviewLoading}>
        {isPreviewLoading ? "Previewing..." : "Preview Import"}
      </button>

      {isPreviewLoading ? (
        <LoadingState message="Loading import preview..." />
      ) : error ? (
        <div className="error-state">
          <h2>Unable to complete request</h2>
          <p>{error}</p>
        </div>
      ) : !hasPreviewed ? (
        <div className="empty-state">
          <h2>No preview loaded</h2>
          <p>Select a category and page size, then load an import preview.</p>
        </div>
      ) : preview && preview.products.length === 0 ? (
        <div className="empty-state">
          <h2>No products found</h2>
          <p>Try another category or page size.</p>
        </div>
      ) : preview ? (
        <section>
          <h2>{preview.displayName} Preview</h2>

          <p>Fetched: {preview.fetchedCount}</p>
          <p>Importable: {preview.importableCount}</p>
          <p>Skipped: {preview.skippedCount}</p>

          {preview.products.map((previewProduct) => (
            <article key={previewProduct.product.externalId}>
              <h3>{previewProduct.product.name}</h3>
              <p>{previewProduct.product.brand}</p>

              {previewProduct.importable ? (
                <p>Importable</p>
              ) : (
                <ul>
                  {previewProduct.skipReasons.map((reason) => (
                    <li key={reason}>{reason}</li>
                  ))}
                </ul>
              )}
            </article>
          ))}

          <button
            onClick={handleImport}
            disabled={isImportLoading || preview.importableCount === 0}
          >
            {isImportLoading
              ? "Importing..."
              : `Import ${preview.importableCount} Products`}
          </button>
        </section>
      ) : null}

      {importResult && (
        <section>
          <h2>Import Result</h2>
          <p>Fetched: {importResult.fetched}</p>
          <p>Imported: {importResult.imported}</p>
          <p>Skipped duplicates: {importResult.skippedDuplicatesCount}</p>
          <p>Skipped invalid: {importResult.skippedInvalidCount}</p>
        </section>
      )}
    </main>
  );
}

export default ImportPreviewPage;
