export async function handleApiResponse(response) {
  if (response.ok) {
    if (response.status === 204) {
      return null;
    }

    return response.json();
  }

  const errorData = await response.json().catch(() => null);
  const backendMessage = errorData?.message;

  if (response.status === 400) {
    throw new Error(backendMessage || "Please check your input and try again.");
  }

  if (response.status === 401) {
    throw new Error(
      backendMessage || "Your session has expired. Please log in again.",
    );
  }

  if (response.status === 403) {
    throw new Error(
      backendMessage || "You do not have permission to perform this action.",
    );
  }

  if (response.status === 404) {
    throw new Error(backendMessage || "The requested item could not be found.");
  }

  if (response.status === 409) {
    throw new Error(
      backendMessage || "This action conflicts with existing data.",
    );
  }

  if (response.status >= 500) {
    throw new Error(
      "The server encountered a problem. Please try again shortly.",
    );
  }

  throw new Error(backendMessage || "Something went wrong. Please try again.");
}

export async function apiFetch(url, options = {}) {
  try {
    const response = await fetch(url, options);
    return await handleApiResponse(response);
  } catch (err) {
    if (err instanceof TypeError) {
      throw new Error(
        "Unable to connect to the server. Check your connection and try again.",
      );
    }

    throw err;
  }
}
