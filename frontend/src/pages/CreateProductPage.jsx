import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createProduct } from "../api/products";

function CreateProductPage() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [brand, setBrand] = useState("");
  const [description, setDescription] = useState("");

  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();

    if (!name.trim() || !brand.trim() || !description.trim()) {
      setFormError("Name, brand, and description are required");
      return;
    }

    const product = {
      name: name.trim(),
      brand: brand.trim(),
      description: description.trim(),
    };

    try {
      setSubmitting(true);
      setFormError("");

      const createdProduct = await createProduct(product);

      navigate(`/products/${createdProduct.id}`);
    } catch (err) {
      setFormError(err.message || "Something went wrong creating the product.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <main className="container">
      <header className="page-header">
        <h1>Add Product</h1>
        <p>Add a frozen food product so it can be reviewd.</p>
      </header>

      <form className="review-form" onSubmit={handleSubmit}>
        {formError && <p className="form-error">{formError}</p>}

        <div className="form-group">
          <label htmlFor="product-name">Name</label>
          <input
            type="text"
            id="product-name"
            value={name}
            onChange={(event) => setName(event.target.value)}
            disabled={submitting}
            placeholder="Example: HEB Frozen Pizza"
          />
        </div>

        <div className="form-group">
          <label htmlFor="product-brand">Brand</label>
          <input
            type="text"
            id="product-brand"
            value={brand}
            onChange={(event) => setBrand(event.target.value)}
            disabled={submitting}
            placeholder="Example: Trader Joe's"
          />
        </div>

        <div className="form-group">
          <label htmlFor="product-description">Description</label>
          <input
            type="text"
            id="product-description"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
            disabled={submitting}
            placeholder="Briefly describe the product"
          />
        </div>

        <button className="primary-button" type="submit" disabled={submitting}>
          {submitting ? "Creating..." : "Create Product"}
        </button>
      </form>
    </main>
  );
}

export default CreateProductPage;
