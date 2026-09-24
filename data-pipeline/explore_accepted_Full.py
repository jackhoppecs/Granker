import json
import random
from pathlib import Path
from collections import Counter

DATA_DIR = Path(__file__).parent / "data"
ACCEPTED_PATH = DATA_DIR / "granker-products-accepted-full.jsonl"


def load_jsonl(path):
    with open(path, "r", encoding="utf-8") as file:
        return [
            json.loads(line)
            for line in file
            if line.strip()
        ]


accepted = load_jsonl(ACCEPTED_PATH)

print("Accepted products:", len(accepted))


# -------------------------
# Duplicate IDs
# -------------------------

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


# -------------------------
# Category distribution
# -------------------------

category_counts = Counter(
    product["category"]
    for product in accepted
)

print("\nCategory distribution:")

for category, count in category_counts.most_common():
    print(f"{category}: {count}")


# -------------------------
# Image coverage
# -------------------------

with_image = sum(
    1
    for product in accepted
    if product["imageUrl"] is not None
)

print(
    "\nImage coverage:",
    f"{with_image}/{len(accepted)}",
    f"({with_image / len(accepted) * 100:.1f}%)"
)


# -------------------------
# Nutrition coverage
# -------------------------

with_nutrition = sum(
    1
    for product in accepted
    if product["calories"] is not None
)

print(
    "Nutrition coverage:",
    f"{with_nutrition}/{len(accepted)}",
    f"({with_nutrition / len(accepted) * 100:.1f}%)"
)


# -------------------------
# Suspicious names
# -------------------------

short_names = [
    product
    for product in accepted
    if len(product["name"].strip()) < 3
]

print("Very short names:", len(short_names))


PLACEHOLDER_NAMES = {
    "unknown",
    "na",
    "n/a",
    "none",
    "not identifiable",
    "unidentifiable product",
}

placeholder_names = [
    product
    for product in accepted
    if product["name"].strip().lower() in PLACEHOLDER_NAMES
]

print("Placeholder names:", len(placeholder_names))


# -------------------------
# Random sample
# -------------------------

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