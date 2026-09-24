import json
from pathlib import Path
import os
from dotenv import load_dotenv

import psycopg


BASE_DIR = Path(__file__).parent

load_dotenv(BASE_DIR / ".env")
DB_URL = os.environ["DB_URL"]

with psycopg.connect(DB_URL) as conn:
    with conn.cursor() as cur:
        cur.execute("SELECT COUNT(*) FROM product_import_staging")
        print(cur.fetchone())