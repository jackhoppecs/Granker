import { use, useState } from "react";
import {
  previewOpenFoodFactsImport,
  importOpenFoodFactsProducts,
} from "../api/import";

function ImportPreviewPage() {
  const [category, setCategory] = useState("pizza");
  const [pageSize, setPageSize] = useState(10);
  const [preview, setPreview] = useState(null);
  const [importResult, setImportResult] = useState(null);
  const [error, setError] = useState("");
  const [isPreviewLoading, setIsPreviewLoading] = useState(false);
  const [isImportLoading, setIsImportLoading] = useState(false);

  async function handlePreview() {
    setIsPreviewLoading(true);
    setError("");
    setImportResult(null);

    try {
      const data = await previewOpenFoodFactsImport(category, pageSize);
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

      <label htmlFor="">
        Category
        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          <Option value="pizza">Pizza</Option>
          <Option value="frozen-meals">Frozen Meals</Option>
          <Option value="ice-cream">Ice Cream</Option>
        </select>
      </label>

      <label>
        Page Size
        <select value={pageSize} onChange={(e) => setPageSize(e.target.value)}>
          <option value={5}>5</option>
          <option value={10}>10</option>
          <option value={25}>25</option>
          <option value={50}>50</option>
        </select>
      </label>

      <button onClick={handlePreview} disabled={isPreviewLoading}>
        {isPreviewLoading ? "Previewing..." : "Preview Import"}
      </button>

      {error && <p>{error}</p>}

      {preview && (
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
      )}

      {importResult && (
        <section>
          <h2>Import Result</h2>
          <p>Fetched: {importResult.fetched}</p>
          <p>Imported: {importResult.imported}</p>
          <p>Skipped duplicates: {importResult.skippedDuplicates}</p>
          <p>Skipped invalid: {importResult.skippedInvalid}</p>
        </section>
      )}
    </main>
  );
}

export default ImportPreviewPage;
