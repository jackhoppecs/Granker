from pathlib import Path
# built into python and lets us read .gz compressed files
import gzip
# built into python and converts JSON text into normal python objects
import json

DATA_DIR = Path(__file__).parent / "data"
FILE_PATH = DATA_DIR / "products.random-modulo-10000.jsonl.gz"

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
        products.append(product)

print("Total products:", len(products))

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