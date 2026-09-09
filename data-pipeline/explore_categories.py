import json
from pathlib import Path
from collections import Counter


DATA_DIR = Path(__file__).parent / "data"
FILE_PATH = DATA_DIR / "us-category-counts.json"


with open(FILE_PATH, "r", encoding="utf-8") as file:
    category_counts = Counter(json.load(file))


def search_categories(term):
    matches = [
        (category, count)
        for category, count in category_counts.items()
        if term.lower() in category.lower()
    ]

    matches.sort(key=lambda x: x[1], reverse=True)

    for category, count in matches:
        print(f"{count:>8}  {category}")


search_categories("supplement")