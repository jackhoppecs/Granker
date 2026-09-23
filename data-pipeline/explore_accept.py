import json
import random
from pathlib import Path
from collections import Counter

DATA_DIR = Path(__file__).parent / "data"

ACCEPTED_PATH = DATA_DIR / "granker-products-sample.jsonl"
REVIEW_PATH = DATA_DIR / "granker-products-review.jsonl"
REJECTED_PATH = DATA_DIR / "granker-products-rejected.jsonl"


def load_jsonl(path):
    with open(path, "r", encoding="utf-8") as file:
        return [
            json.loads(line)
            for line in file
            if line.strip()
        ]


accepted = load_jsonl(ACCEPTED_PATH)
review = load_jsonl(REVIEW_PATH)
rejected = load_jsonl(REJECTED_PATH)

print("Accepted:", len(accepted))
print("Review:", len(review))
print("Rejected:", len(rejected))
print()


# Duplicate external IDs
external_ids = [
    product["externalId"]
    for product in accepted
]

duplicate_ids = [
    external_id
    for external_id, count
    in Counter(external_ids).items()
    if count > 1
]

print("Duplicate external IDs:", len(duplicate_ids))


# Category distribution
category_counts = Counter(
    product["category"]
    for product in accepted
)

print("\nCategory distribution:")
for category, count in category_counts.most_common():
    print(category, count)


# Image coverage
with_image = sum(
    1 for product in accepted
    if product["imageUrl"] is not None
)

print(
    "\nImage coverage:",
    f"{with_image}/{len(accepted)}",
    f"({with_image / len(accepted) * 100:.1f}%)"
)


# Nutrition coverage
with_nutrition = sum(
    1 for product in accepted
    if product["calories"] is not None
)

print(
    "Nutrition coverage:",
    f"{with_nutrition}/{len(accepted)}",
    f"({with_nutrition / len(accepted) * 100:.1f}%)"
)


# Random manual inspection
print("\nRandom accepted products:")

for product in random.sample(
    accepted,
    min(100, len(accepted))
):
    print(
        product["name"],
        "|",
        product["brand"],
        "|",
        product["category"]
    )



reason_counts = Counter(
    item["reason"]
    for item in review
)

print(reason_counts)


unmapped_tags = Counter()

for item in review:
    if item["reason"] != "unmapped_category":
        continue

    for tag in item["categoriesTags"]:
        unmapped_tags[tag] += 1

for tag, count in unmapped_tags.most_common(50):
    print(f"{count:>4} {tag}")