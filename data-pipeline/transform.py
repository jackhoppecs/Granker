import gzip
import json
from pathlib import Path



# def is_supported_product(product):
#     barcode = product.get("code")
#     name = product.get("product_name")
#     brand = product.get("brands")
#     categories = product.get("categories_tags", [])

#     if not barcode:
#         return False

#     if not name or not name.strip():
#         return False

#     if not brand or not brand.strip():
#         return False

#     # if not categories:
#     #     return False

#     return True


# Record completeness helper function (AKA Need barcode, name, and brand)
def has_required_identity(product):
    barcode = product.get("code")
    name = product.get("product_name")
    brand = product.get("brands")

    return (
        bool(barcode)
        and bool(name and name.strip())
        and bool(brand and brand.strip())
    )

# Categories we might not want in granker
EXCLUDED_CATEGORIES = {
    "en:dietary-supplements",
    "en:bodybuilding-supplements",
}

# If product is usable is it a kind of product Granker should use
def is_allowed_product_type(product):
    # Makes a product's category list into a set
    # Set's are built around membership and comparison operations
    categories = set(product.get("categories_tags", []))

    # & Means intersection AKA: What appears in both sets?
    # If there is a value that exists in both then the product is excluded
    if categories & EXCLUDED_CATEGORIES:
        return False

    return True

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

DATA_DIR = Path(__file__).parent / "data"
FILE_PATH = DATA_DIR / "us-random-sample-10000.jsonl"
products = []

with open(FILE_PATH, "r", encoding="utf-8") as file:
    # Each line is a JSON object
    for line in file:
        # Turn each JSON object into a python dict
        product = json.loads(line)
        # Push those dicts into products list to explore later
        products.append(product)

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



# Let's explore some accepted and some rejected
accepted = []
rejected = []

for product in products:
    if has_required_identity(product):
        accepted.append(product)
    else:
        rejected.append(product)

print("Accepted:", len(accepted))
print("Rejected:", len(rejected))

print("\nAccepted samples:")
for product in accepted[:50]:
    print(
        product.get("code"), "|",
        product.get("product_name"),
        "|",
        product.get("brands"),
        "|",
        product.get("categories_tags", [])
    )

print("\nRejected samples:")
for product in rejected[:50]:
    print(
        product.get("code"), "|",
        product.get("product_name"),
        "|",
        product.get("brands"),
        "|",
        product.get("categories_tags", [])
    )


# Why are they being rejected
missing_barcode = 0
missing_name = 0
missing_brand = 0
missing_categories = 0

for product in products:
    barcode = product.get("code")
    name = product.get("product_name")
    brand = product.get("brands")
    categories = product.get("categories_tags", [])

    if not barcode:
        missing_barcode += 1

    if not name or not name.strip():
        missing_name += 1

    if not brand or not brand.strip():
        missing_brand += 1

    if not categories:
        missing_categories += 1

print("Missing barcode:", missing_barcode)
print("Missing name:", missing_name)
print("Missing brand:", missing_brand)
print("Missing categories:", missing_categories)

# for product in products[:10]:
#     transformed = transform_product(product)
#     print(transformed)
#     print()
    

# Let's explore products not meeting category filter
print("What about for category types?")
accepted = []
rejected = []

for product in products:
    if is_allowed_product_type(product):
        accepted.append(product)
    else:
        rejected.append(product)

print("Accepted:", len(accepted))
print("Rejected:", len(rejected))