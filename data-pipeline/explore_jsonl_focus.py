import gzip
import json
from pathlib import Path
from collections import Counter

DATA_DIR = Path(__file__).parent / "data"
FILE_PATH = DATA_DIR / "openfoodfacts-products.jsonl.gz"
OUTPUT_PATH = DATA_DIR / "us-category-counts.txt"
OUTPUT_PATH_JSON = DATA_DIR / "us-category-counts.json"

category_counts = Counter()
us_product_count = 0
total_count = 0

with gzip.open(FILE_PATH, "rt", encoding="utf-8") as file:
    for line in file:
        total_count += 1

        if total_count % 100_000 == 0:
            print(f"Scanned {total_count:,} total products...")
            
        product = json.loads(line)

        countries = product.get("countries_tags", [])

        if "en:united-states" not in countries:
            continue

        us_product_count += 1

        categories = product.get("categories_tags", [])

        category_counts.update(categories)

with open(OUTPUT_PATH, "w", encoding="utf-8") as file:
    for category, count in category_counts.most_common():
        file.write(f"{count}\t{category}\n")

sorted_counts = dict(category_counts.most_common())

with open(OUTPUT_PATH_JSON, "w", encoding="utf-8") as file:
    json.dump(sorted_counts, file, indent=2, ensure_ascii=False)

print("US products scanned:", us_product_count)
print("Unique categories:", len(category_counts))

for category, count in category_counts.most_common(100):
    print(f"{count:>8}  {category}")