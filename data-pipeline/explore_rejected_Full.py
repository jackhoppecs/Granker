import json
import random
from pathlib import Path
from collections import Counter

DATA_DIR = Path(__file__).parent / "data"
REJECTED_PATH = DATA_DIR / "granker-products-rejected-full.jsonl"


def load_jsonl(path):
    with open(path, "r", encoding="utf-8") as file:
        return [
            json.loads(line)
            for line in file
            if line.strip()
        ]


rejected = load_jsonl(REJECTED_PATH)

print("Rejected products:", len(rejected))


# -------------------------
# Rejection reason counts
# -------------------------

reason_counts = Counter(
    item["reason"]
    for item in rejected
)

print("\nRejection reasons:")

for reason, count in reason_counts.most_common():
    print(f"{reason}: {count}")


# -------------------------
# Random rejected products
# -------------------------

print("\nRandom rejected products:")

for item in random.sample(
    rejected,
    min(200, len(rejected))
):
    print(
        item.get("name"),
        "|",
        item.get("brand"),
        "|",
        item.get("reason"),
        "|",
        item.get("categoriesTags"),
        "|",
        item.get("productType"),
    )