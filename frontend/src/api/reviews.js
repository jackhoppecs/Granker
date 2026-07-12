import API_BASE_URL from "./config";

export async function getReviewsByProductId(productId, sort = "newest") {
  const params = new URLSearchParams();

  if (sort) {
    params.append("sort", sort);
  }
  const response = await fetch(
    `${API_BASE_URL}/api/products/${productId}/reviews?${params.toString()}`,
  );

  if (!response.ok) {
    return handleApiResponse(response);
  }

  return response.json();
}

export async function getMyReviews() {
  const response = await fetch(`${API_BASE_URL}/api/reviews/me`, {
    credentials: "include",
  });

  if (!response.ok) {
    return handleApiResponse(response);
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
    return handleApiResponse(response);
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
    return handleApiResponse(response);
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
    return handleApiResponse(response);
  }
}
