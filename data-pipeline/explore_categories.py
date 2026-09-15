import json
from pathlib import Path
from collections import Counter


DATA_DIR = Path(__file__).parent / "data"
FILE_PATH = DATA_DIR / "us-category-counts.json"

SAMPLE_PATH = DATA_DIR / "us-random-sample-10000.jsonl"

products = []

with open(SAMPLE_PATH, "r", encoding="utf-8") as file:
    for line in file:
        product = json.loads(line)
        products.append(product)


with open(FILE_PATH, "r", encoding="utf-8") as file:
    category_counts = Counter(json.load(file))

# Search all categories for a matching term
def search_categories(term):
    matches = [
        (category, count)
        for category, count in category_counts.items()
        if term.lower() in category.lower()
    ]

    matches.sort(key=lambda x: x[1], reverse=True)

    for category, count in matches:
        print(f"{count:>8}  {category}")


# search_categories("fruit")
# search_categories("vegetable")
# search_categories("lettuce")
# search_categories("banana")
# search_categories("produce")


def has_required_identity(product):
    barcode = product.get("code")
    name = product.get("product_name")
    brand = product.get("brands")

    return (
        bool(barcode)
        and bool(name and name.strip())
        and bool(brand and brand.strip())
    )

def print_product(product):
    print(
        product.get("product_name"),
        "|",
        product.get("brands"),
        "|",
        product.get("categories_tags", [])
    )


PRODUCE_CANDIDATE_CATEGORIES = {
    "en:fresh-fruits",
    "en:fresh-vegetables",
    "en:produce",
    "en:lettuces",
    "en:bananas",
}

def inspect_products_matching_categories(
    products,
    categories_to_match,
    limit=100
):
    shown = 0

    for product in products:
        if not has_required_identity(product):
            continue

        categories = set(product.get("categories_tags", []))

        if categories & categories_to_match:
            print("\nName:", product.get("product_name"))
            print("Generic name:", product.get("generic_name"))
            print("Brand:", product.get("brands"))
            print("Brand owner:", product.get("brand_owner"))
            print("Quantity:", product.get("quantity"))
            print("Product quantity:", product.get("product_quantity"))
            print("Product quantity unit:", product.get("product_quantity_unit"))
            print("Packaging:", product.get("packaging"))
            print("Packaging tags:", product.get("packaging_tags", []))
            print("Packaging text:", product.get("packaging_text"))
            print("Packagings:", product.get("packagings"))
            print("Packagings complete:", product.get("packagings_complete"))
            print("Stores:", product.get("stores"))
            print("Stores tags:", product.get("stores_tags", []))
            print("Product type:", product.get("product_type"))
            print("Categories:", product.get("categories_tags", []))

            shown += 1

            if shown >= limit:
                break

inspect_products_matching_categories(
    products,
    PRODUCE_CANDIDATE_CATEGORIES,
    limit=100
)


# Explore each key path
def collect_key_paths(value, prefix=""):
    paths = set()

    if isinstance(value, dict):
        for key, nested_value in value.items():
            if prefix:
                path = f"{prefix}.{key}"
            else:
                path = key

            paths.add(path)

            paths.update(
                collect_key_paths(nested_value, path)
            )

    elif isinstance(value, list):
        for item in value:
            paths.update(
                collect_key_paths(item, prefix)
            )

    return paths

all_paths = set()

for product in products:
    all_paths.update(
        collect_key_paths(product)
    )


SEARCH_TERMS = {
    "pack",
    "quantity",
    "brand",
    "store",
    "generic",
    "product",
}

for path in sorted(all_paths):
    path_lower = path.lower()

    if any(term in path_lower for term in SEARCH_TERMS):
        print(path)