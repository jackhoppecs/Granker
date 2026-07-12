export async function handleApiResponse(response) {
  if (response.ok) {
    if (response.status === 204) {
      return null;
    }

    return response.json();
  }

  // Trying to read the backend's error response body safely
  // try to parse the response with .json, await waits
  // if this fails akak .catch it sets the errorData to null
  // second line uses: optional chaining
  // if there is errorData it sets backendMessage to message
  // otherwise backend message is null and can be used further down to trigger default responses.
  // This protects us from just crashing if errorData was just null
  const errorData = await response.json().catch(() => null);
  const backendMessage = errorData?.message;

  if (response.status === 401) {
    throw new Error(backendMessage || "Please log in to continue.");
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

  if (response.status === 400) {
    throw new Error(backendMessage || "Please check your input and try again.");
  }

  throw new Error(backendMessage || "Something went wrong. Please try again.");
}
