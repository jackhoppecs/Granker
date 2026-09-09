import gzip
import json
from pathlib import Path
from collections import Counter

DATA_DIR = Path(__file__).parent / "data"
FILE_PATH = DATA_DIR / "openfoodfacts-products.jsonl.gz"

category_counts = Counter()
us_product_count = 0

with gzip.open(FILE_PATH, "rt", encoding="utf-8") as file:
    for line in file:
        product = json.loads(line)

        countries = product.get("countries_tags", [])

        if "en:united-states" not in countries:
            continue

        us_product_count += 1

        categories = product.get("categories_tags", [])

        category_counts.update(categories)

print("US products scanned:", us_product_count)
print("Unique categories:", len(category_counts))

for category, count in category_counts.most_common():
    print(f"{count:>8}  {category}")