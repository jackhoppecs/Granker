const API_BASE_URL = "http://localhost:8080";

export async function getReviewsByProductId(productId) {
  const response = await fetch(
    `${API_BASE_URL}/api/products/${productId}/reviews`,
  );

  if (!response.ok) {
    throw new Error("Failed to fetch reviews");
  }

  return response.json();
}
