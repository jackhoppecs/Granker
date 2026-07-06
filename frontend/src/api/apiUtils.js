export async function handleApiResponse(response) {
  if (response.ok) {
    if (response.status === 204) {
      return null;
    }

    return response.json();
  }

  if (response.status === 401) {
    throw new Error("Please log in to continue.");
  }

  if (response.status === 403) {
    throw new Error("You do not have permission to perform this action.");
  }

  if (response.status === 404) {
    throw new Error("The requested item could not be found.");
  }

  throw new Error("Something went wrong. Please try again.");
}
