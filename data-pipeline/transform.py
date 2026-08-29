import gzip
import json
from pathlib import Path


def transform_product(product):
    return {
        "barcode": product.get("code"),
        "name": product.get("product_name"),
        "brand": product.get("brands"),
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


for product in products[:10]:
    transformed = transform_product(product)
    print(transformed)
    print()
    