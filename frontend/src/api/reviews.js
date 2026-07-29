import API_BASE_URL from "./config";
import { apiFetch } from "./apiUtils";

export async function getReviewsByProductId(productId, sort = "newest") {
  const params = new URLSearchParams();

  if (sort) {
    params.append("sort", sort);
  }

  return apiFetch(
    `${API_BASE_URL}/api/products/${productId}/reviews?${params.toString()}`,
  );
}

export async function getMyReviews() {
  return apiFetch(`${API_BASE_URL}/api/reviews/me`, {
    credentials: "include",
  });
}

export async function createReview(productId, review) {
  return apiFetch(`${API_BASE_URL}/api/products/${productId}/reviews`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(review),
  });
}

export async function updateReview(reviewId, updatedReviewData) {
  return apiFetch(`${API_BASE_URL}/api/reviews/${reviewId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(updatedReviewData),
  });
}

export async function deleteReview(reviewId) {
  return apiFetch(`${API_BASE_URL}/api/reviews/${reviewId}`, {
    method: "DELETE",
    credentials: "include",
  });
}
