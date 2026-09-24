import gzip
import json
from pathlib import Path

from rules import (
    HARD_EXCLUDED_CATEGORIES,
    FOOD_LIKE_CATEGORIES,
    CATEGORY_RULES,
)


DATA_DIR = Path(__file__).parent / "data"

INPUT_PATH = DATA_DIR / "openfoodfacts-products.jsonl.gz"

ACCEPTED_PATH = DATA_DIR / "granker-products-accepted-full.jsonl"
REVIEW_PATH = DATA_DIR / "granker-products-review-full.jsonl"
REJECTED_PATH = DATA_DIR / "granker-products-rejected-full.jsonl"


# -------------------------
# Product filtering
# -------------------------

def has_required_identity(product):
    code = product.get("code")
    name = product.get("product_name")
    brand = product.get("brands")

    return (
        bool(code)
        and bool(name and name.strip())
        and bool(brand and brand.strip())
    )


def is_allowed_product_type(product):
    categories = set(product.get("categories_tags") or [])
    product_type = product.get("product_type")

    if categories & FOOD_LIKE_CATEGORIES:
        return True

    if categories & HARD_EXCLUDED_CATEGORIES:
        return False

    if product_type is not None and product_type != "food":
        return False

    return True


# -------------------------
# Nutrition
# -------------------------

def get_nutrition_per_100g(product):
    nutrition = product.get("nutrition") or {}
    aggregated = nutrition.get("aggregated_set") or {}

    if (
        aggregated.get("per") == "100g"
        and aggregated.get("preparation") == "as_sold"
    ):
        nutrients = aggregated.get("nutrients") or {}

        values = {
            "calories": nutrients.get("energy-kcal", {}).get("value"),
            "protein": nutrients.get("proteins", {}).get("value"),
            "carbs": nutrients.get("carbohydrates", {}).get("value"),
            "fat": nutrients.get("fat", {}).get("value"),
        }

        if all(value is not None for value in values.values()):
            return values

    nutriments = product.get("nutriments") or {}

    values = {
        "calories": nutriments.get("energy-kcal_100g"),
        "protein": nutriments.get("proteins_100g"),
        "carbs": nutriments.get("carbohydrates_100g"),
        "fat": nutriments.get("fat_100g"),
    }

    if all(value is not None for value in values.values()):
        return values

    return None


def is_reasonable_nutrition(nutrition):
    if nutrition is None:
        return False

    calories = nutrition["calories"]
    protein = nutrition["protein"]
    carbs = nutrition["carbs"]
    fat = nutrition["fat"]

    values = [calories, protein, carbs, fat]

    if any(value < 0 for value in values):
        return False

    if protein > 100 or carbs > 100 or fat > 100:
        return False

    if protein + carbs + fat > 105:
        return False

    return True


# -------------------------
# Image
# -------------------------

def get_front_image_url(product):
    images = product.get("images") or {}
    selected = images.get("selected") or {}
    front = selected.get("front") or {}

    if not front:
        return None

    language = "en" if "en" in front else next(iter(front))
    image = front[language]

    revision = image.get("rev")
    code = product.get("code")

    if revision is None or not code:
        return None

    code = str(code)

    if len(code) < 13:
        code = code.zfill(13)

    if len(code) > 8:
        folder = (
            f"{code[0:3]}/"
            f"{code[3:6]}/"
            f"{code[6:9]}/"
            f"{code[9:]}"
        )
    else:
        folder = code

    return (
        "https://images.openfoodfacts.org/images/products/"
        f"{folder}/front_{language}.{revision}.400.jpg"
    )


# -------------------------
# Category
# -------------------------

def normalize_category(product):
    categories = set(product.get("categories_tags") or [])

    if not categories:
        return "Uncategorized"

    for granker_category, off_categories in CATEGORY_RULES:
        if categories & off_categories:
            return granker_category

    return "Other"


# -------------------------
# Source
# -------------------------

def get_source_url(product):
    code = product.get("code")

    if not code:
        return None

    return f"https://world.openfoodfacts.org/product/{code}"


# -------------------------
# Transform
# -------------------------

def transform_product(product):
    nutrition = get_nutrition_per_100g(product)

    if not is_reasonable_nutrition(nutrition):
        nutrition = None

    return {
        "name": product["product_name"].strip(),
        "brand": product["brands"].strip(),
        "description": None,
        "category": normalize_category(product),
        "imageUrl": get_front_image_url(product),

        "calories": (
            round(nutrition["calories"])
            if nutrition else None
        ),
        "proteinGrams": (
            round(nutrition["protein"], 1)
            if nutrition else None
        ),
        "carbGrams": (
            round(nutrition["carbs"], 1)
            if nutrition else None
        ),
        "fatGrams": (
            round(nutrition["fat"], 1)
            if nutrition else None
        ),

        "sourceName": "OpenFoodFacts",
        "sourceUrl": get_source_url(product),
        "externalId": product["code"],
    }


# -------------------------
# Pipeline
# -------------------------

#REVIEW_PATH = DATA_DIR / "granker-products-review.jsonl"


def run_pipeline():
    total = 0
    us_products = 0
    accepted = 0
    review = 0
    rejected = 0

    with (
        gzip.open(INPUT_PATH, "rt", encoding="utf-8") as source,
        open(ACCEPTED_PATH, "w", encoding="utf-8") as accepted_file,
        open(REVIEW_PATH, "w", encoding="utf-8") as review_file,
        open(REJECTED_PATH, "w", encoding="utf-8") as rejected_file,
    ):
        for line in source:
            total += 1

            if total % 100000 == 0:
                print(f"Processed {total:,} raw OFF records...")

            product = json.loads(line)

            # Only keep U.S. products
            countries = set(product.get("countries_tags") or [])

            if "en:united-states" not in countries:
                continue

            us_products += 1
                
            status, reason = classify_product(product)

            if status == "rejected":
                rejected_record = {
                    "reason": reason,
                    "name": product.get("product_name"),
                    "brand": product.get("brands"),
                    "externalId": product.get("code"),
                    "categoriesTags": product.get("categories_tags") or [],
                    "productType": product.get("product_type"),
                }

                rejected_file.write(
                    json.dumps(
                        rejected_record,
                        ensure_ascii=False
                    ) + "\n"
                )

                rejected += 1
                continue

            transformed = transform_product(product)

            if status == "review":
                review_record = {
                    "reason": reason,
                    "product": transformed,
                    "categoriesTags": product.get("categories_tags") or [],
                    "productType": product.get("product_type"),
                }

                review_file.write(
                    json.dumps(
                        review_record,
                        ensure_ascii=False
                    ) + "\n"
                )

                review += 1
                continue

            accepted_file.write(
                json.dumps(
                    transformed,
                    ensure_ascii=False
                ) + "\n"
            )

            accepted += 1

    print()
    print(f"Raw OFF records scanned: {total:,}")
    print(f"U.S. products found: {us_products:,}")
    print(f"Accepted: {accepted:,}")
    print(f"Review: {review:,}")
    print(f"Rejected: {rejected:,}")

    print(f"\nAccepted output: {ACCEPTED_PATH}")
    print(f"Review output: {REVIEW_PATH}")
    print(f"Rejected output: {REJECTED_PATH}")



# -------------------------
# Classification
# -------------------------

def classify_product(product):
    if not has_required_identity(product):
        return "rejected", "missing_identity"

    if not is_allowed_product_type(product):
        return "rejected", "unsupported_product_type"

    category = normalize_category(product)

    if category == "Uncategorized":
        return "review", "missing_category"

    if category == "Other":
        return "review", "unmapped_category"

    return "accepted", None



if __name__ == "__main__":
    run_pipeline()