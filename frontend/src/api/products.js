const API_BASE_URL = "http://localhost:8080";

export async function getProducts() {
  const response = await fetch(`${API_BASE_URL}/api/products`);
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
