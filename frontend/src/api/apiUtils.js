// Used to retain error code for additional frontend behavior
export class ApiError extends Error {
  constructor(message, status) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

export async function handleApiResponse(response) {
  if (response.ok) {
    if (response.status === 204) {
      return null;
    }

    return response.json();
  }

  const errorData = await response.json().catch(() => null);
  const backendMessage = errorData?.message;

  let message;

  if (response.status === 400) {
    message = backendMessage || "Please check your input and try again.";
  } else if (response.status === 401) {
    message =
      backendMessage || "Your session has expired. Please log in again.";
  } else if (response.status === 403) {
    message =
      backendMessage || "You do not have permission to perform this action.";
  } else if (response.status === 404) {
    message = backendMessage || "The requested item could not be found.";
  } else if (response.status === 409) {
    message = backendMessage || "This action conflicts with existing data.";
  } else if (response.status >= 500) {
    message = "The server encountered a problem. Please try again shortly.";
  } else {
    message = backendMessage || "Something went wrong. Please try again.";
  }

  throw new ApiError(message, response.status);
}

export async function apiFetch(url, options = {}) {
  let response;

  try {
    response = await fetch(url, options);
  } catch {
    throw new ApiError(
      "Unable to connect to the server. Check your connection and try again.",
      null,
    );
  }

  return handleApiResponse(response);
}
