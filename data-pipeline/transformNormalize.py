import json
from pathlib import Path
from collections import Counter


# -------------------------
# Configuration
# -------------------------

DATA_DIR = Path(__file__).parent / "data"
FILE_PATH = DATA_DIR / "us-random-sample-10000.jsonl"


HARD_EXCLUDED_CATEGORIES = {
    "en:dietary-supplements",
    "en:bodybuilding-supplements",
    "en:medicine",
    "en:household-cleaner",
    "en:home-garden-household-supplies-laundry-supplies",
    "en:personal-care",
    "en:cosmetic-oil",
    "en:beauty",
    "en:open-pet-food-facts",
}

FOOD_LIKE_CATEGORIES = {
    "en:protein-bars",
    "en:energy-bars",
    "en:protein-shakes",
}

# -------------------------
# Validation / filtering
# -------------------------

def has_required_identity(product):
    barcode = product.get("code")
    name = product.get("product_name")
    brand = product.get("brands")

    return (
        bool(barcode)
        and bool(name and name.strip())
        and bool(brand and brand.strip())
    )


def is_allowed_product_type(product):
    categories = set(product.get("categories_tags", []))

    # Explicitly allow food-like products even if OFF also
    # classifies them as supplements.
    if categories & FOOD_LIKE_CATEGORIES:
        return True

    # Otherwise reject known unsupported categories.
    if categories & HARD_EXCLUDED_CATEGORIES:
        return False

    return True

def get_numeric_value(data, key):
    value = data.get(key)

    if value is None:
        return None

    if isinstance(value, (int, float)):
        return value

    try:
        return float(value)
    except (TypeError, ValueError):
        return None

# def get_nutrition_per_100g(product):
#     nutriments = product.get("nutriments", {})

#     calories = nutriments.get("energy-kcal_100g")
#     protein = nutriments.get("proteins_100g")
#     carbs = nutriments.get("carbohydrates_100g")
#     fat = nutriments.get("fat_100g")

#     if all(
#         value is not None
#         for value in [calories, protein, carbs, fat]
#     ):
#         return {
#             "calories": calories,
#             "protein": protein,
#             "carbs": carbs,
#             "fat": fat,
#         }

#     nutrition = product.get("nutrition", {})
#     input_sets = nutrition.get("input_sets", [])

#     for input_set in input_sets:
#         if input_set.get("per") != "100g":
#             continue

#         if input_set.get("preparation") != "as_sold":
#             continue

#         nutrients = input_set.get("nutrients", {})

#         calories_data = nutrients.get("energy-kcal", {})
#         protein_data = nutrients.get("proteins", {})
#         carbs_data = nutrients.get("carbohydrates", {})
#         fat_data = nutrients.get("fat", {})

#         calories = calories_data.get("value")
#         protein = protein_data.get("value")
#         carbs = carbs_data.get("value")
#         fat = fat_data.get("value")

#         if all(
#             value is not None
#             for value in [calories, protein, carbs, fat]
#         ):
#             return {
#                 "calories": calories,
#                 "protein": protein,
#                 "carbs": carbs,
#                 "fat": fat,
#             }

#     return None

def get_nutrition_per_100g(product):
    nutrition = product.get("nutrition", {})
    aggregated = nutrition.get("aggregated_set", {})

    if (
        aggregated.get("per") == "100g"
        and aggregated.get("preparation") == "as_sold"
    ):
        nutrients = aggregated.get("nutrients", {})

        calories = nutrients.get("energy-kcal", {}).get("value")
        protein = nutrients.get("proteins", {}).get("value")
        carbs = nutrients.get("carbohydrates", {}).get("value")
        fat = nutrients.get("fat", {}).get("value")

        if all(
            value is not None
            for value in [calories, protein, carbs, fat]
        ):
            return {
                "calories": calories,
                "protein": protein,
                "carbs": carbs,
                "fat": fat,
            }

    # Older / alternate OFF structure
    nutriments = product.get("nutriments", {})

    calories = nutriments.get("energy-kcal_100g")
    protein = nutriments.get("proteins_100g")
    carbs = nutriments.get("carbohydrates_100g")
    fat = nutriments.get("fat_100g")

    if all(
        value is not None
        for value in [calories, protein, carbs, fat]
    ):
        return {
            "calories": calories,
            "protein": protein,
            "carbs": carbs,
            "fat": fat,
        }

    return None

def is_reasonable_nutrition(nutrition):
    if nutrition is None:
        return False

    calories = nutrition.get("calories")
    protein = nutrition.get("protein")
    carbs = nutrition.get("carbs")
    fat = nutrition.get("fat")

    values = [calories, protein, carbs, fat]


    # Checking for negative values or crazy high numbers
    if any(value is None for value in values):
        return False

    if any(value < 0 for value in values):
        return False

    if protein > 100 or carbs > 100 or fat > 100:
        return False

    if protein + carbs + fat > 105:
        return False

    return True

# -------------------------
# Image Exploration
# -------------------------
def get_front_image_url(product):
    images = product.get("images", {})
    selected = images.get("selected", {})
    front = selected.get("front", {})

    if not isinstance(front, dict) or not front:
        return None

    # Prefer English
    if "en" in front:
        language = "en"
    else:
        # Otherwise use the first available front image
        language = next(iter(front))

    image = front[language]

    rev = image.get("rev")
    code = product.get("code")

    if not rev or not code:
        return None

    code = str(code)

    # OFF expects short barcodes padded to 13 digits
    if len(code) < 13:
        code = code.zfill(13)

    # Build OFF image directory
    if len(code) > 8:
        folder = f"{code[0:3]}/{code[3:6]}/{code[6:9]}/{code[9:]}"
    else:
        folder = code

    return (
        "https://images.openfoodfacts.org/images/products/"
        f"{folder}/front_{language}.{rev}.400.jpg"
    )


# -------------------------
# Transformation
# -------------------------

def transform_product(product):
    if not has_required_identity(product):
        return None

    if not is_allowed_product_type(product):
        return None

    nutrition = get_nutrition_per_100g(product)

    if not is_reasonable_nutrition(nutrition):
        nutrition = None

    image_url = get_front_image_url(product)

    return {
        "name": product.get("product_name").strip(),
        "brand": product.get("brands").strip(),
        "description": None,
        "category": None,
        "imageUrl": image_url,

        "calories": (
            round(nutrition["calories"])
            if nutrition
            else None
        ),
        "proteinGrams": (
            round(nutrition["protein"], 1)
            if nutrition
            else None
        ),
        "carbGrams": (
            round(nutrition["carbs"], 1)
            if nutrition
            else None
        ),
        "fatGrams": (
            round(nutrition["fat"], 1)
            if nutrition
            else None
        ),

        "sourceName": "OpenFoodFacts",
        "sourceUrl": None,
        "externalId": product.get("code"),
    }


# -------------------------
# Exploration helpers
# -------------------------

def print_product(product):
    print(
        product.get("product_name"),
        "|",
        product.get("brands"),
        "|",
        product.get("categories_tags", [])
    )

# Show me anything carrying these category tags.
def inspect_excluded_products(products, excluded_categories, limit=50):
    shown = 0

    for product in products:
        if not has_required_identity(product):
            continue

        categories = set(product.get("categories_tags", []))

        if categories & excluded_categories:
            print_product(product)

            shown += 1

            if shown >= limit:
                break

# Show me what Granker actually rejects.
def inspect_rejected_products(products, limit=50):
    shown = 0

    for product in products:
        if not has_required_identity(product):
            continue

        if not is_allowed_product_type(product):
            print_product(product)

            shown += 1

            if shown >= limit:
                break

def inspect_missing_nutrition(products, limit=50):
    shown = 0

    for product in products:
        if not has_required_identity(product):
            continue

        if not is_allowed_product_type(product):
            continue

        nutrition = get_nutrition_per_100g(product)

        if nutrition is not None:
            continue

        print_product(product)
        shown += 1

        if shown >= limit:
            break

def inspect_invalid_nutrition(products, limit=50):
    shown = 0

    for product in products:
        if not has_required_identity(product):
            continue

        if not is_allowed_product_type(product):
            continue

        nutrition = get_nutrition_per_100g(product)

        if nutrition is None:
            continue

        if is_reasonable_nutrition(nutrition):
            continue

        print(
            product.get("product_name"),
            "|",
            product.get("brands"),
            "|",
            nutrition,
        )

        shown += 1

        if shown >= limit:
            break

def inspect_images(products, limit=5):
    shown = 0

    for product in products:
        if not has_required_identity(product):
            continue

        images = product.get("images", {})

        if not images:
            continue

        print("\nPRODUCT:", product.get("product_name"))
        print("BARCODE:", product.get("code"))
        print("IMAGES:")
        print(json.dumps(images, indent=2))

        shown += 1

        if shown >= limit:
            break

def inspect_image_fields(products, limit=10):
    shown = 0

    for product in products:
        if not has_required_identity(product):
            continue

        print("\nPRODUCT:", product.get("product_name"))
        print("image_url:", product.get("image_url"))
        print("image_front_url:", product.get("image_front_url"))
        print("image_front_small_url:", product.get("image_front_small_url"))
        print("image_front_thumb_url:", product.get("image_front_thumb_url"))

        shown += 1

        if shown >= limit:
            break

# -------------------------
# Load development sample
# -------------------------

products = []

with open(FILE_PATH, "r", encoding="utf-8") as file:
    for line in file:
        product = json.loads(line)
        products.append(product)


# -------------------------
# Pipeline report
# -------------------------

missing_identity = 0
excluded_type = 0
final_accepted = 0

for product in products:
    if not has_required_identity(product):
        missing_identity += 1
        continue

    if not is_allowed_product_type(product):
        excluded_type += 1
        continue

    final_accepted += 1


print("Total products:", len(products))
print("Missing required identity:", missing_identity)
print("Excluded product type:", excluded_type)
print("Final accepted:", final_accepted)


# -------------------------
# Inspect exclusions
# -------------------------

print("\nSample products excluded by category:")
inspect_excluded_products(
    products,
    HARD_EXCLUDED_CATEGORIES,
    limit=50
)

print("\nProducts actually rejected by product type:")
inspect_rejected_products(products)

print("\nSample products excluded by MISSING nutrition:")
inspect_missing_nutrition(
    products,
)

print("\nSample products excluded by nutrition:")
inspect_invalid_nutrition(
    products,
)

print("\nInspect images:")
inspect_images(
    products,
)
print("\nInspect image fields:")
inspect_image_fields(
    products,
)



# -------------------------
# Preview transformed output
# -------------------------

print("\nSample transformed products:")

shown = 0

for product in products:
    transformed = transform_product(product)

    if transformed is None:
        continue

    print(json.dumps(transformed, indent=2))
    print()

    shown += 1

    if shown >= 10:
        break


# nutrition_counts = {
#     "calories": 0,
#     "protein": 0,
#     "carbs": 0,
#     "fat": 0,
#     "complete": 0,
# }

# accepted = 0

# for product in products:
#     transformed = transform_product(product)

#     if transformed is None:
#         continue

#     accepted += 1

#     if transformed["calories"] is not None:
#         nutrition_counts["calories"] += 1

#     if transformed["proteinGrams"] is not None:
#         nutrition_counts["protein"] += 1

#     if transformed["carbGrams"] is not None:
#         nutrition_counts["carbs"] += 1

#     if transformed["fatGrams"] is not None:
#         nutrition_counts["fat"] += 1

#     if all(
#         transformed[field] is not None
#         for field in [
#             "calories",
#             "proteinGrams",
#             "carbGrams",
#             "fatGrams",
#         ]
#     ):
#         nutrition_counts["complete"] += 1

# print("\nNutrition coverage:")

# for field, count in nutrition_counts.items():
#     percent = count / accepted * 100

#     print(
#         f"{field}: "
#         f"{count}/{accepted} "
#         f"({percent:.1f}%)"
#     )

# ###
# nutrition_fields = [
#     # Calories
#     "energy-kcal",
#     "energy-kcal_100g",
#     "energy-kcal_serving",

#     # Protein
#     "proteins",
#     "proteins_100g",
#     "proteins_serving",

#     # Carbs
#     "carbohydrates",
#     "carbohydrates_100g",
#     "carbohydrates_serving",

#     # Fat
#     "fat",
#     "fat_100g",
#     "fat_serving",
# ]

# for field in nutrition_fields:
#     populated = 0

#     for product in products:
#         if not has_required_identity(product):
#             continue

#         if not is_allowed_product_type(product):
#             continue

#         nutriments = product.get("nutriments", {})
#         value = nutriments.get(field)

#         if value is not None:
#             populated += 1

#     percent = populated / 6287 * 100

#     print(
#         f"{field}: "
#         f"{populated}/6287 "
#         f"({percent:.1f}%)"
#     )



# accepted_products = [
#     product
#     for product in products
#     if has_required_identity(product)
#     and is_allowed_product_type(product)
# ]

# for field in nutrition_fields:
#     populated = sum(
#         1
#         for product in accepted_products
#         if product.get("nutriments", {}).get(field) is not None
#     )

#     percent = populated / len(accepted_products) * 100

#     print(
#         f"{field}: "
#         f"{populated}/{len(accepted_products)} "
#         f"({percent:.1f}%)"
#     )


# nutriment_key_counts = Counter()

# for product in accepted_products:
#     nutriments = product.get("nutriments", {})

#     if isinstance(nutriments, dict):
#         for key, value in nutriments.items():
#             if value is not None:
#                 nutriment_key_counts[key] += 1

# for key, count in nutriment_key_counts.most_common(50):
#     print(f"{count:>5}  {key}")







def has_required_nutrition(product):
    nutriments = product.get("nutriments", {})

    required = [
        "energy-kcal_100g",
        "proteins_100g",
        "carbohydrates_100g",
        "fat_100g",
    ]

    return all(
        nutriments.get(field) is not None
        for field in required
    )


def inspect_missing_nutrition(products, limit=50):
    shown = 0

    for product in products:
        if not has_required_identity(product):
            continue

        if not is_allowed_product_type(product):
            continue

        if has_required_nutrition(product):
            continue

        print_product(product)

        shown += 1

        if shown >= limit:
            break

missing_nutrition = 0
invalid_nutrition = 0
usable_nutrition = 0

for product in products:
    if not has_required_identity(product):
        continue

    if not is_allowed_product_type(product):
        continue

    nutrition = get_nutrition_per_100g(product)

    if nutrition is None:
        missing_nutrition += 1
        continue

    if not is_reasonable_nutrition(nutrition):
        invalid_nutrition += 1
        continue

    usable_nutrition += 1


total = missing_nutrition + invalid_nutrition + usable_nutrition

print("\nNutrition quality:")
print("Accepted products:", total)
print("Missing nutrition:", missing_nutrition)
print("Invalid nutrition:", invalid_nutrition)
print("Usable nutrition:", usable_nutrition)

print(
    "Usable coverage:",
    f"{usable_nutrition / total * 100:.1f}%"
)

# print("\n Missing Nutrition:")
# inspect_missing_nutrition(products)


# BARCODES_TO_INSPECT = {
#     "0011110766557",  # Kroger Old Fashioned Oats
#     "0014100077602",  # Pepperidge Farm Goldfish
# }

# for product in products:
#     if product.get("code") in BARCODES_TO_INSPECT:
#         print("\n" + "=" * 100)
#         print("BARCODE:", product.get("code"))
#         print("NAME:", product.get("product_name"))

#         print("\nNUTRIMENTS:")
#         print(
#             json.dumps(
#                 product.get("nutriments", {}),
#                 indent=2,
#                 ensure_ascii=False
#             )
#         )

#         print("\nFULL PRODUCT:")
#         print(json.dumps(product.get("nutrition", {}), indent=2))



# def get_nutrition_per_100g(product):
#     nutriments = product.get("nutriments", {})

#     calories = nutriments.get("energy-kcal_100g")
#     protein = nutriments.get("proteins_100g")
#     carbs = nutriments.get("carbohydrates_100g")
#     fat = nutriments.get("fat_100g")

#     if all(
#         value is not None
#         for value in [calories, protein, carbs, fat]
#     ):
#         return {
#             "calories": calories,
#             "protein": protein,
#             "carbs": carbs,
#             "fat": fat,
#         }

#     nutrition = product.get("nutrition", {})
#     input_sets = nutrition.get("input_sets", [])

#     for input_set in input_sets:
#         if input_set.get("per") != "100g":
#             continue

#         if input_set.get("preparation") != "as_sold":
#             continue

#         nutrients = input_set.get("nutrients", {})

#         calories_data = nutrients.get("energy-kcal", {})
#         protein_data = nutrients.get("proteins", {})
#         carbs_data = nutrients.get("carbohydrates", {})
#         fat_data = nutrients.get("fat", {})

#         calories = calories_data.get("value")
#         protein = protein_data.get("value")
#         carbs = carbs_data.get("value")
#         fat = fat_data.get("value")

#         if all(
#             value is not None
#             for value in [calories, protein, carbs, fat]
#         ):
#             return {
#                 "calories": calories,
#                 "protein": protein,
#                 "carbs": carbs,
#                 "fat": fat,
#             }

#     return None