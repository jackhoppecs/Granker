import urllib.request
import json

barcode = "0602652201042"

url = f"https://world.openfoodfacts.org/api/v2/product/{barcode}.json"

request = urllib.request.Request(
    url,
    headers={
        "User-Agent": "Granker/0.1"
    }
)

with urllib.request.urlopen(request) as response:
    data = json.load(response)

product = data.get("product", {})

print("images:")
print(json.dumps(product.get("images"), indent=2))

print("\nimage_url:")
print(product.get("image_url"))

print("\nimage_front_url:")
print(product.get("image_front_url"))

print("\nselected_images:")
print(json.dumps(product.get("selected_images"), indent=2))

print("\nknowledge_panels:")
print(json.dumps(product.get("knowledge_panels"), indent=2))