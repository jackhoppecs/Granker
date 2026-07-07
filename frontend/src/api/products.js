import API_BASE_URL from "./config";

// export means to make this function available to other files
// async means the function does asynchronous work, and will return a "Promise"
// AKA JS is saying I don't have a value yet but will eventually
export async function getProducts(
  sort = "name",
  minRating = "",
  category = "",
  brand = "",
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

  const response = await fetch(
    `${API_BASE_URL}/api/products?${params.toString()}`,
  );
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

  if (!response.ok) {
    throw new Error("Failed to fetch products");
  }

  // turns that JSON into a JavaScript array of objects.
  return response.json();
}

export async function getProductById(productId) {
  const response = await fetch(`${API_BASE_URL}/api/products/${productId}`);

  if (!response.ok) {
    throw new Error("Failed to fetch product");
  }

  return response.json();
}

export async function createProduct(product) {
  const response = await fetch(`${API_BASE_URL}/api/products`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(product),
  });

  if (!response.ok) {
    throw new Error("Failed to create product");
  }

  return response.json();
}

export async function getCategories() {
  const response = await fetch(`${API_BASE_URL}/api/products/categories`);

  if (!response.ok) {
    throw new Error("Failed to retrieve categories");
  }

  return response.json();
}

export async function getBrands() {
  const response = await fetch(`${API_BASE_URL}/api/products/brands`);

  if (!response.ok) {
    throw new Error("Failed to retrieve categories");
  }

  return response.json();
}
