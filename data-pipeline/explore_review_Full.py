import json
import random
from pathlib import Path
from collections import Counter

DATA_DIR = Path(__file__).parent / "data"
REVIEW_PATH = DATA_DIR / "granker-products-review-full.jsonl"


def load_jsonl(path):
    with open(path, "r", encoding="utf-8") as file:
        return [
            json.loads(line)
            for line in file
            if line.strip()
        ]


review = load_jsonl(REVIEW_PATH)

print("Review products:", len(review))


# -------------------------
# Review reason counts
# -------------------------

reason_counts = Counter(
    item["reason"]
    for item in review
)

print("\nReview reasons:")

for reason, count in reason_counts.most_common():
    print(f"{reason}: {count}")


# -------------------------
# Top unmapped category tags
# -------------------------

unmapped_tags = Counter()

for item in review:
    if item["reason"] != "unmapped_category":
        continue

    for tag in item["categoriesTags"]:
        unmapped_tags[tag] += 1


print("\nTop unmapped category tags:")

for tag, count in unmapped_tags.most_common(50):
    print(f"{count:>6} {tag}")


# -------------------------
# Random missing-category products
# -------------------------

missing_category = [
    item
    for item in review
    if item["reason"] == "missing_category"
]

print("\nRandom missing-category products:")

for item in random.sample(
    missing_category,
    min(100, len(missing_category))
):
    product = item["product"]

    print(
        product["name"],
        "|",
        product["brand"]
    )


# -------------------------
# Random unmapped-category products
# -------------------------

unmapped_category = [
    item
    for item in review
    if item["reason"] == "unmapped_category"
]

print("\nRandom unmapped-category products:")

for item in random.sample(
    unmapped_category,
    min(100, len(unmapped_category))
):
    product = item["product"]

    print(
        product["name"],
        "|",
        product["brand"],
        "|",
        item["categoriesTags"]
    )