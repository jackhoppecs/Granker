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

export async function createReview(productId, review) {
  const response = await fetch(
    `${API_BASE_URL}/api/products/${productId}/reviews`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(review),
    },
  );

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(
      `Failed to create review. Status: ${response.status}. Body: ${errorText}`,
    );
  }

  return response.json();
}

export async function updateReview(reviewId, updatedReviewData) {
  const response = await fetch(`${API_BASE_URL}/api/reviews/${reviewId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(updatedReviewData),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(
      `Failed to edit review. Status: ${response.status}. Body: ${errorText}`,
    );
  }

  return response.json();
}

export async function deleteReview(reviewId) {
  const response = await fetch(`${API_BASE_URL}/api/reviews/${reviewId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(
      `Failed to delete review. Status: ${response.status}. Body: ${errorText}`,
    );
  }
}
