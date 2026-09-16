import json
import urllib.request


BARCODES = [
    "0011110766557",  # Kroger Old Fashioned Oats
    "0014100077602",  # Goldfish
]


def fetch_live_product(barcode):
    url = (
        f"https://world.openfoodfacts.org/api/v2/product/"
        f"{barcode}.json"
        f"?fields=code,product_name,brands,nutriments"
    )

    request = urllib.request.Request(
        url,
        headers={
            "User-Agent": "Granker/0.1 (catalog ETL testing)"
        }
    )

    with urllib.request.urlopen(request) as response:
        data = json.load(response)

    if data.get("status") != 1:
        return None

    return data.get("product")


for barcode in BARCODES:
    product = fetch_live_product(barcode)

    print("\n" + "=" * 80)
    print("Barcode:", barcode)

    if product is None:
        print("Product not found")
        continue

    print("Name:", product.get("product_name"))
    print("Brand:", product.get("brands"))

    print("\nLive nutriments:")
    print(
        json.dumps(
            product.get("nutriments", {}),
            indent=2
        )
    )