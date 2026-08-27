from pathlib import Path
# built into python and lets us read .gz compressed files
import gzip
# built into python and converts JSON text into normal python objects
import json

DATA_DIR = Path(__file__).parent / "data"
# FILE_PATH = DATA_DIR / "products.random-modulo-10000.jsonl.gz"
FILE_PATH = DATA_DIR / "openfoodfacts-products.jsonl.gz"

# .open allows us to open a compressed file without manual extraction
# "rt" means r = read and t = text
with gzip.open(FILE_PATH, "rt", encoding="utf-8") as file:
    # A JSONL file conceptually has:
    # {"code":"111","name":"Product A", ...}
    # {"code":"222","name":"Product B", ...}
    # {"code":"333","name":"Product C", ...}
    # so readline gets exactly one product
    first_line = file.readline()
    # This converts that JSON text read before into a python dict
    product = json.loads(first_line)

# Now we can access produtc like a normal dict
print("Top-level keys:")
for key in product.keys():
    print(key)


# Pretty printing
# json.dumps(product) converts the python dict back to JSON text
# 
# :10000 is just normal string slicing
print("\nSample product:")
print(json.dumps(product, indent=2)[:10000])


print("Code:", product.get("code"))
print("Name:", product.get("product_name"))
print("Brand:", product.get("brands"))
print("Countries:", product.get("countries_tags"))
print("Categories:", product.get("categories_tags"))

print("\nNutriments:")
print(json.dumps(product.get("nutriments"), indent=2))

print("\nImages:")
print(json.dumps(product.get("images"), indent=2)[:5000])


# First line didnt have nutriments. What about others?
with gzip.open(FILE_PATH, "rt", encoding="utf-8") as file:
    for line in file:
        product = json.loads(line)

        nutriments = product.get("nutriments", {})

        if nutriments:
            print("Code:", product.get("code"))
            print("Name:", product.get("product_name"))
            print("Brand:", product.get("brands"))
            print("\nNutriments:")
            print(json.dumps(nutriments, indent=2))
            break

# Let's count completeness
def get_nested_value(product, path):
    value = product

    for key in path.split("."):
        if not isinstance(value, dict):
            return None

        value = value.get(key)

        if value is None:
            return None

    return value

products = []

with gzip.open(FILE_PATH, "rt", encoding="utf-8") as file:
    for line in file:
        product = json.loads(line)
        countries = product.get("countries_tags", [])

        if "en:united-states" not in countries:
            continue

        
        products.append(product)

        if len(products) >= 10_000:
            break


print("Total US products:", len(products))

fields = [
    "product_name",
    "brands",
    "countries_tags",
    "categories_tags",
    "nutriments",
    "images.selected.front",
]

for field in fields:
    populated = 0

    for product in products:
        value = get_nested_value(product, field)

        if value:
            populated += 1

    percent = populated / len(products) * 100

    print(
        f"{field}: "
        f"{populated}/{len(products)} "
        f"({percent:.1f}%)"
    )

image_fields = [
    "images",
    "images.uploaded",
    "images.selected",
    "images.selected.front",
]

for field in image_fields:
    populated = 0

    for product in products:
        value = get_nested_value(product, field)

        if value:
            populated += 1

    percent = populated / len(products) * 100

    print(
        f"{field}: "
        f"{populated}/{len(products)} "
        f"({percent:.1f}%)"
    )


for product in products:
    nutriments = product.get("nutriments", {})

    if nutriments:
        print("Name:", product.get("product_name"))

        for key in nutriments.keys():
            print(key)

        break

nutrition_fields = [
    "nutriments.energy-kcal",
    "nutriments.energy-kcal_serving",
    "nutriments.proteins",
    "nutriments.proteins_serving",
    "nutriments.carbohydrates",
    "nutriments.carbohydrates_serving",
    "nutriments.fat",
    "nutriments.fat_serving",
]

for field in nutrition_fields:
    populated = 0

    for product in products:
        value = get_nested_value(product, field)

        if value is not None:
            populated += 1

    percent = populated / len(products) * 100
    print(f"{field}: {populated}/{len(products)} ({percent:.1f}%)")


image_fields = [
"selected_images",
"image_url",
"image_front_url",
"image_front_small_url",
"image_front_thumb_url",
]

for field in image_fields:
    populated = 0

    for product in products:
        value = product.get(field)

        if value:
            populated += 1

    percent = populated / len(products) * 100
    print(f"{field}: {populated}/{len(products)} ({percent:.1f}%)")


possible_image_fields = [
    key
    for product in products
    for key in product.keys()
    if "image" in key.lower()
]

print(sorted(set(possible_image_fields)))


total_with_images = 0
total_with_front_image = 0

front_key_counts = {}

for product in products:
    images = product.get("images", {})

    if not isinstance(images, dict) or not images:
        continue

    total_with_images += 1

    front_keys = [
        key for key in images.keys()
        if key.startswith("front_")
    ]

    if front_keys:
        total_with_front_image += 1

        for key in front_keys:
            front_key_counts[key] = front_key_counts.get(key, 0) + 1

print(
    f"Products with images: "
    f"{total_with_images}/{len(products)} "
    f"({total_with_images / len(products) * 100:.1f}%)"
)

print(
    f"Products with front_* image: "
    f"{total_with_front_image}/{len(products)} "
    f"({total_with_front_image / len(products) * 100:.1f}%)"
)

print("\nMost common front image keys:")

for key, count in sorted(
    front_key_counts.items(),
    key=lambda item: item[1],
    reverse=True
)[:20]:
    print(key, count)


shown = 0

for product in products:
    images = product.get("images", {})

    if not images:
        continue

    print("\nName:", product.get("product_name"))
    print("Image keys:", list(images.keys()))

    shown += 1

    if shown >= 10:
        break

# Images has two distinct shapes that we need to count
def has_front_image(product):
    images = product.get("images", {})

    if not isinstance(images, dict):
        return False

    # Older/alternate style: front_en, front_fr, etc.
    for key in images:
        if key.startswith("front_"):
            return True

    # Nested style: images.selected.front
    selected = images.get("selected", {})
    if isinstance(selected, dict):
        front = selected.get("front", {})
        if isinstance(front, dict) and front:
            return True

    return False

front_count = sum(
    1 for product in products
    if has_front_image(product)
)

print(
    f"Products with usable front image metadata: "
    f"{front_count}/{len(products)} "
    f"({front_count / len(products) * 100:.1f}%)"
)