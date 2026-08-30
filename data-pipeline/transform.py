import gzip
import json
from pathlib import Path



def is_supported_product(product):
    name = product.get("product_name")
    brand = product.get("brands")
    categories = product.get("categories_tags", [])

    if not name or not brand:
        return False

    if not categories:
        return False

    return True


def transform_product(product):
    if not is_supported_product(product):
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
    if is_supported_product(product):
        accepted.append(product)
    else:
        rejected.append(product)

print("Accepted:", len(accepted))
print("Rejected:", len(rejected))

print("\nAccepted samples:")
for product in accepted[:50]:
    print(
        product.get("product_name"),
        "|",
        product.get("brands"),
        "|",
        product.get("categories_tags", [])
    )

print("\nRejected samples:")
for product in rejected[:50]:
    print(
        product.get("product_name"),
        "|",
        product.get("brands"),
        "|",
        product.get("categories_tags", [])
    )

# for product in products[:10]:
#     transformed = transform_product(product)
#     print(transformed)
#     print()
    