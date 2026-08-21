from pathlib import Path

import pandas as pd

DATA_DIR = Path(__file__).parent / "data"

FILE_PATH = DATA_DIR / "openfoodfacts_export.csv"

df = pd.read_csv(
    FILE_PATH,
    sep="\t",
    low_memory=False,
)


print("Rows:", len(df))
print("Columns:", len(df.columns))

print("\nFirst 30 column names:")
for column in df.columns[:30]:
    print(column)

interesting_columns = [
    "code",
    "product_name_en",
    "quantity",
    "serving_size",
    "brands",
    "categories",
    "categories_tags",
    "countries",
    "countries_tags",
    "ingredients_text_en",
    "energy-kcal_100g",
    "proteins_100g",
    "carbohydrates_100g",
    "fat_100g",
    "image_front_url",
]

print("\nInteresting columns:")

for column in interesting_columns:
    print(f"{column}: {'YES' if column in df.columns else 'NO'}")


    existing_columns = [
    column
    for column in interesting_columns
    if column in df.columns
]

print("\nSample products:")
print(df[existing_columns].head(10).to_string())


print("\nColumns containing 'brand':")
for column in df.columns:
    if "brand" in column.lower():
        print(column)


search_terms = [
    "energy",
    "kcal",
    "protein",
    "carbo",
    "fat",
    "image",
    "category",
    "country",
]

for term in search_terms:
    print(f"\nColumns containing '{term}':")
    for column in df.columns:
        if term.lower() in column.lower():
            print(column)