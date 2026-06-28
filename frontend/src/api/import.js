const API_BASE_URL = "http://localhost:8080";

export async function previewOpenFoodFactsImport(category, pageSize) {
  const params = new URLSearchParams({
    category,
    pageSize,
  });

  const response = await fetch(
    `${API_BASE_URL}/api/import/open-food-facts/preview?category=${params}`,
    {
      credentials: "include",
    },
  );

  if (!response.ok) {
    throw new Error("Failed to preview import results.");
  }

  return response.json();
}

export async function importOpenFoodFactsProducts(category, pageSize) {
  const params = new URLSearchParams({
    category,
    pageSize,
  });
  const response = await fetch(
    `${API_BASE_URL}/api/import/open-food-facts?category=${params}`,
    {
      method: "POST",
      credentials: "include",
    },
  );

  if (!response.ok) {
    throw new Error("Failed to import products.");
  }

  return response.json();
}
