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


# How complete are the columns we're looking at. AKA how many rows contain those columns
interesting_columns = [
    "code",
    "product_name_en",
    "brands",
    "countries_tags",
    "nutrition.input_sets.packaging.as_sold.100g.nutrients.energy-kcal.value",
    "nutrition.input_sets.packaging.as_sold.100g.nutrients.proteins.value",
    "nutrition.input_sets.packaging.as_sold.100g.nutrients.carbohydrates.value",
    "nutrition.input_sets.packaging.as_sold.100g.nutrients.fat.value",
]

print("\nField completeness:")

for column in interesting_columns:
    if column in df.columns:
        populated = df[column].notna().sum()
        percent = populated / len(df) * 100

        print(f"{column}: {populated}/{len(df)} ({percent:.1f}%)")


# What about the other nutrition fields
nutrition_columns = [
    "nutrition.input_sets.estimate.as_sold.100g.nutrients.energy-kcal.value",
    "nutrition.input_sets.packaging.as_sold.100g.nutrients.energy-kcal.value",
    "nutrition.input_sets.packaging.as_sold.serving.nutrients.energy-kcal.value",

    "nutrition.input_sets.estimate.as_sold.100g.nutrients.proteins.value",
    "nutrition.input_sets.packaging.as_sold.100g.nutrients.proteins.value",
    "nutrition.input_sets.packaging.as_sold.serving.nutrients.proteins.value",

    "nutrition.input_sets.estimate.as_sold.100g.nutrients.carbohydrates.value",
    "nutrition.input_sets.packaging.as_sold.100g.nutrients.carbohydrates.value",
    "nutrition.input_sets.packaging.as_sold.serving.nutrients.carbohydrates.value",

    "nutrition.input_sets.estimate.as_sold.100g.nutrients.fat.value",
    "nutrition.input_sets.packaging.as_sold.100g.nutrients.fat.value",
    "nutrition.input_sets.packaging.as_sold.serving.nutrients.fat.value",
]

for column in nutrition_columns:
    if column in df.columns:
        populated = df[column].notna().sum()
        percent = populated / len(df) * 100
        print(f"{column}: ({percent:.1f})%")


# How many missing names
missing_names = df[df["product_name_en"].isna()]

print("Missing names:", len(missing_names))

# Make output more readable
sample = df[
    [
        "code",
        "product_name_en",
        "brands",
        "nutrition.input_sets.packaging.as_sold.serving.nutrients.energy-kcal.value",
        "nutrition.input_sets.packaging.as_sold.serving.nutrients.proteins.value",
        "nutrition.input_sets.packaging.as_sold.serving.nutrients.carbohydrates.value",
        "nutrition.input_sets.packaging.as_sold.serving.nutrients.fat.value",
    ]
].copy()

sample.columns = [
    "barcode",
    "name",
    "brand",
    "calories",
    "protein",
    "carbs",
    "fat",
]

print(sample.head(20).to_string(index=False))

# What would a minimal viable product look like?
required_columns = [
    "code",
    "product_name_en",
    "brands",
    "countries_tags",
]

valid_core = df.dropna(subset=required_columns)

print("Valid core products:", len(valid_core))
print(
    f"Coverage: {len(valid_core) / len(df) * 100:.1f}%"
)

# Duplicate codes?
duplicates = df[df["code"].duplicated(keep=False)]

print("Duplicate barcode rows:", len(duplicates))