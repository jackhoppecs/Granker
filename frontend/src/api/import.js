import API_BASE_URL from "./config";
import { handleApiResponse } from "./apiUtils";

export async function previewOpenFoodFactsImport(category, pageSize) {
  const params = new URLSearchParams({
    category,
    pageSize,
  });

  const response = await fetch(
    `${API_BASE_URL}/api/import/open-food-facts/preview?${params}`,
    {
      credentials: "include",
    },
  );

  return handleApiResponse(response);
}

export async function importOpenFoodFactsProducts(category, pageSize) {
  const params = new URLSearchParams({
    category,
    pageSize,
  });
  const response = await fetch(
    `${API_BASE_URL}/api/import/open-food-facts?${params}`,
    {
      method: "POST",
      credentials: "include",
    },
  );

  return handleApiResponse(response);
}

// export async function previewOpenFoodFactsImport(category, pageSize) {
//   return {
//     category,
//     displayName: "Pizza",
//     fetchedCount: 4,
//     importableCount: 2,
//     skippedCount: 2,
//     products: [
//       {
//         product: {
//           externalId: "test-001",
//           name: "Test Frozen Pizza",
//           brand: "Granker Test Brand",
//           description: "Temporary mock product.",
//           category: "Pizza",
//           imageUrl: "",
//           calories: 320,
//           proteinGrams: 14,
//           carbGrams: 42,
//           fatGrams: 11,
//           sourceName: "Open Food Facts",
//           sourceUrl: "https://world.openfoodfacts.org/product/test-001",
//         },
//         importable: true,
//         skipReasons: [],
//       },
//       {
//         product: {
//           externalId: "test-002",
//           name: "Missing Brand Meal",
//           brand: "",
//           description: "",
//           category: "Frozen Meals",
//           imageUrl: "",
//           calories: null,
//           proteinGrams: null,
//           carbGrams: null,
//           fatGrams: null,
//           sourceName: "Open Food Facts",
//           sourceUrl: "",
//         },
//         importable: false,
//         skipReasons: ["Missing brand"],
//       },
//       {
//         product: {
//           externalId: "test-003",
//           name: "",
//           brand: "Some Brand",
//           description: "",
//           category: "Ice Cream",
//           imageUrl: "",
//           calories: 210,
//           proteinGrams: 4,
//           carbGrams: 30,
//           fatGrams: 9,
//           sourceName: "Open Food Facts",
//           sourceUrl: "",
//         },
//         importable: false,
//         skipReasons: ["Missing product name"],
//       },
//       {
//         product: {
//           externalId: "test-004",
//           name: "Already Imported Pizza",
//           brand: "Old Brand",
//           description: "",
//           category: "Pizza",
//           imageUrl: "",
//           calories: 280,
//           proteinGrams: 12,
//           carbGrams: 35,
//           fatGrams: 10,
//           sourceName: "Open Food Facts",
//           sourceUrl: "",
//         },
//         importable: false,
//         skipReasons: ["Already imported"],
//       },
//     ],
//   };
// }

// export async function importOpenFoodFactsProducts(category, pageSize) {
//   return {
//     fetched: 4,
//     imported: 2,
//     skippedDuplicates: 1,
//     skippedInvalid: 1,
//   };
// }
