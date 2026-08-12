import API_BASE_URL from "./config";
import { apiFetch } from "./apiUtils";

// export means to make this function available to other files
// async means the function does asynchronous work, and will return a "Promise"
// AKA JS is saying I don't have a value yet but will eventually
export async function getProducts(
  sort = "name",
  minRating = "",
  category = "",
  brand = "",
  search = "",
) {
  // await means call the backend and pause the function until there's a response
  // fetch is build into the browser and sentds an HTTP request
  // fetch returns:
  // response.ok -> true
  // response.status -> 200
  // response.json() -> data

  const params = new URLSearchParams();

  if (sort) {
    params.append("sort", sort);
  }

  if (minRating) {
    params.append("minRating", minRating);
  }

  if (category) {
    params.append("category", category);
  }

  if (brand) {
    params.append("brand", brand);
  }

  if (search) {
    params.append("search", search);
  }

  return apiFetch(`${API_BASE_URL}/api/products?${params.toString()}`);
  // Spring Boot controller returns JSON
  //   [
  //   {
  //     "id": 1,
  //     "name": "Chicken Alfredo Bowl",
  //     "brand": "Healthy Choice",
  //     "description": "Frozen pasta meal"
  //   },
  //   {
  //     "id": 2,
  //     "name": "Pepperoni Pizza",
  //     "brand": "DiGiorno",
  //     "description": "Frozen pizza"
  //   }
  // ]
}

export async function getProductById(productId) {
  return apiFetch(`${API_BASE_URL}/api/products/${productId}`);
}

export async function createProduct(product) {
  return apiFetch(`${API_BASE_URL}/api/products`, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(product),
  });
}

export async function getCategories() {
  return apiFetch(`${API_BASE_URL}/api/products/categories`);
}

export async function getBrands() {
  return apiFetch(`${API_BASE_URL}/api/products/brands`);
}
