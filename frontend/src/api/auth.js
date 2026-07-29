import API_BASE_URL from "./config";
import { apiFetch } from "./apiUtils";

export async function login(credentials) {
  return apiFetch(`${API_BASE_URL}/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(credentials),
  });
}

export async function logout() {
  return apiFetch(`${API_BASE_URL}/auth/logout`, {
    method: "POST",
    credentials: "include",
  });
}

export async function getCurrentUser() {
  let response;

  try {
    response = await fetch(`${API_BASE_URL}/auth/me`, {
      credentials: "include",
    });
  } catch {
    throw new Error(
      "Unable to connect to the server. Check your connection and try again.",
    );
  }

  if (response.status === 401) {
    return null;
  }

  return handleApiResponse(response);
}

export async function register(username, email, password) {
  return apiFetch(`${API_BASE_URL}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify({
      username,
      email,
      password,
    }),
  });
}
