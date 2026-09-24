import json
from pathlib import Path
import os
from dotenv import load_dotenv

import psycopg


DATA_DIR = Path(__file__).parent / "data"
ACCEPTED_PATH = DATA_DIR / "granker-products-accepted-full.jsonl"
BASE_DIR = Path(__file__).parent
load_dotenv(BASE_DIR / ".env")

DB_URL = os.environ["DB_URL"]

with psycopg.connect(DB_URL) as conn:
    with conn.cursor() as cur:
        with open(ACCEPTED_PATH, "r", encoding="utf-8") as file:
            with cur.copy(
                """
                COPY product_import_staging (
                    brand,
                    description,
                    name,
                    calories,
                    carb_grams,
                    category,
                    fat_grams,
                    image_url,
                    protein_grams,
                    source_name,
                    source_url,
                    external_id
                )
                FROM STDIN
                """
            ) as copy:
                for line in file:
                    product = json.loads(line)

                    copy.write_row((
                        product["brand"],
                        product["description"],
                        product["name"],
                        product["calories"],
                        product["carbGrams"],
                        product["category"],
                        product["fatGrams"],
                        product["imageUrl"],
                        product["proteinGrams"],
                        product["sourceName"],
                        product["sourceUrl"],
                        product["externalId"],
                    ))