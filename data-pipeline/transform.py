import json
from pathlib import Path


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


# -------------------------
# Transformation
# -------------------------

def transform_product(product):
    if not has_required_identity(product):
        return None

    if not is_allowed_product_type(product):
        return None

    return {
        "barcode": product.get("code"),
        "name": product.get("product_name").strip(),
        "brand": product.get("brands").strip(),
        "categories": product.get("categories_tags", []),
        "countries": product.get("countries_tags", []),
        "nutriments": product.get("nutriments", {}),
        "images": product.get("images", {}),
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


# -------------------------
# Preview transformed output
# -------------------------

# print("\nSample transformed products:")

# shown = 0

# for product in products:
#     transformed = transform_product(product)

#     if transformed is None:
#         continue

#     print(json.dumps(transformed, indent=2))
#     print()

#     shown += 1

#     if shown >= 10:
#         break