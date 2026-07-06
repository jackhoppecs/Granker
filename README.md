# Granker

Granker is a full-stack frozen foods review app. Users can browse frozen food products, view product details, read reviews, submit reviews, and add new products.

## Features

- Browse frozen food products
- Search products by name or brand
- Sort products by name, newest, highest rating, or most reviewed
- Filter products by minimum rating
- Filter products by category or brand
- View product details, average rating, and review count
- View reviews for a specific product
- Sort reviews by newest, highest rating, or lowest rating
- Submit a review for a product
- Edit and delete your own reviews
- Prevent duplicate reviews from the same user on the same product
- View created and updated timestamps for reviews
- Add new products from the frontend
- Basic session-based login/logout flow
- Seeded demo data for local development
- Empty states for products and reviews
- View all reviews written by the current user on a My Reviews page
- Edit and delete reviews from the My Reviews page
- Link review history entries back to their product detail pages
- Restore logged-in user state after browser refresh
- Product metadata fields for categories, images, nutrition, and source attribution
- Register a new user account from the frontend
- Import a small real-world product dataset from Open Food Facts
- Normalize imported Open Food Facts product data into the app's internal product model
- Prevent duplicate imported products using source name and external product ID
- Preserve source attribution for imported product data
- Preview Open Food Facts imports before saving products
- Select supported import categories and page sizes from the frontend
- Display detailed importability status and skip reasons before importing
- Return user-friendly errors when Open Food Facts is unavailable

## Tech Stack

### Frontend

- React
- React Router
- JavaScript
- CSS

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Open Food Facts API integration

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

On Windows PowerShell:

```bash
cd backend
.\mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Demo Login

```txt
Email: demo@example.com
Password: password
```

## Data Imports

Granker supports a small Open Food Facts import pipeline for bringing real product data into the app. The backend calls the Open Food Facts API, maps the external JSON response into internal import DTOs, normalizes selected fields, validates imported products, and saves usable products into the local PostgreSQL database.

Imported products include source metadata such as source name, source URL, external product ID, and import timestamp. Duplicate imports are prevented using the combination of source name and external product ID.

The import workflow uses a preview-first approach. Users select a supported category and page size, preview products returned from Open Food Facts, review which products are importable or skipped, and then import only the products that passed validation.

Supported import categories currently include:

- Frozen Pizza
- Frozen Meals
- Ice Cream

The import pipeline currently focuses on a small, controlled dataset rather than broad bulk imports. Open Food Facts data can contain noisy categories, missing fields, international products, and inconsistent product metadata, so imported products are filtered, validated, and lightly normalized before being saved.

The import preview displays fetched product counts, importable product counts, skipped product counts, and product-level skip reasons. This makes it easier to inspect imported data quality before adding products to Granker.

Open Food Facts import preview and import execution are admin-only so normal users cannot repeatedly call the external API through the app backend.

## MVP Status

The app currently includes product browsing, product details, product search, product sorting/filtering, review submission, review editing/deletion for review owners, duplicate review prevention, seeded demo data, average ratings, review counts, frontend registration, session-based login/logout, persisted login state after refresh, and a My Reviews page where logged-in users can view, edit, delete, and navigate back to products they have reviewed.

The app also includes product metadata and discovery features. Products can store and display category, image URL, nutrition fields, and source attribution. Users can filter products by category or brand, and product cards/detail pages display more useful product information.

Granker includes a preview-based backend import pipeline for Open Food Facts. Imported products are fetched from the external API, converted into a normalized internal DTO, validated, checked for duplicates, mapped into the app's Product entity, stored in PostgreSQL, and displayed through the existing frontend product browsing flow. The frontend import preview page allows admin users to choose a supported category and page size, inspect importable products and skip reasons, and only import products after previewing the data.

The app now includes basic role-based protection for sensitive actions. Product management, user management, and Open Food Facts import tools are restricted to admin users, while normal users can continue using the core review and browsing features.

## Future Improvements

- Public deployment
- Production-safe admin account setup using environment variables or manual database setup
- Rate limiting for sensitive endpoints
- Automated backend and frontend tests
- Docker setup
- Import result history
- Better imported data quality checks
- More detailed nutrition and serving size information
- User profile pages
- Favorite or saved products
- Barcode support
- Mobile frontend exploration
- More consistent authorization helper usage across controllers

## Version Status

### v1.1.0

This version adds average product ratings, review counts, and logout support.

### v1.2.0

This version adds review management features. Logged-in users can edit and delete their own reviews, while review controls are only shown for reviews owned by the current user. Reviews now include created and updated timestamps, duplicate reviews are prevented so each user can only review a product once, and product rating summaries update after reviews are created, edited, or deleted.

### v1.3.0

Added product discovery features. Users can sort products by name, newest, highest rating, or most reviewed. Products can also be filtered by minimum rating. Product detail pages now support review sorting by newest, highest rating, or lowest rating. Empty states and spacing were improved for a cleaner browsing experience.

### v1.4.0

Added user account and review history features. Users can now register from the frontend, stay logged in after refreshing the browser, and access a My Reviews page showing all reviews they have written. Each review history entry displays product name, brand, rating, review text, timestamps, and a link back to the product detail page. Users can also edit and delete their reviews directly from My Reviews, and navbar links now update based on login state.

### v1.5.0

Added product metadata and improved discovery features. Products now support categories, image URLs, nutrition fields, and source attribution for seeded or imported data. Product cards and detail pages now display richer product information, including category labels, nutrition previews, image placeholders, and source attribution. Users can also filter products by category and brand, with backend support for optional query parameters and filter option endpoints. The product schema is now better prepared for future real-data imports.

### v1.6.0

Added an Open Food Facts import pipeline for real-world product data. The backend now includes an Open Food Facts API client, external response models for search/product/nutriment data, a normalized ImportedProductDTO, and mapping logic to convert external API responses into Granker products.

Imported products now store import-specific metadata, including source name, source URL, external product ID, and import timestamp. Duplicate imported products are prevented using source name and external ID checks, with database-level uniqueness protection. The import pipeline also includes basic category normalization so noisy external category strings can be mapped into cleaner Granker categories.

This version makes Granker more realistic by allowing the app to populate products from an external food database while preserving source attribution and keeping imported data separate from manually-created product data.

### v1.7.0

Added an import preview and data quality workflow for Open Food Facts imports. Import endpoints now support category and page-size parameters, allowing the app to fetch smaller, controlled sets of products by supported frozen food category.

This version adds a supported import category whitelist, including frozen pizza, frozen burritos, frozen vegetables, frozen meals, and ice cream. Imported products now go through stricter validation and improved category normalization before they can be saved.

The backend now returns detailed import preview results, including fetched product counts, importable product counts, skipped product counts, and product-level skip reasons. This makes the import process more transparent and helps prevent low-quality or duplicate data from being added silently.

The frontend now includes an import preview page where users can select an import category and page size, preview Open Food Facts products, review which products are importable, see skip reasons for rejected products, and import only after previewing the results.

Open Food Facts API failures are now handled with clearer, user-friendly error messages, making the import flow easier to understand when the external service is unavailable.

### v.1.8.0

Added basic security and admin-role protections to make the app safer and more deployable.

Users now include an admin flag, and auth responses include admin status so the frontend can adjust navigation and page access based on the current user's role. Public registration defaults new users to non-admin accounts, while seeded development data includes a separate admin demo user.

Open Food Facts import preview and import execution are now admin-only. This prevents normal users from repeatedly calling the external Open Food Facts API through the app backend. Product management actions, including product creation, updates, and deletion, are also restricted to admins.

The frontend now hides admin-only navigation links from normal users and shows friendly blocked-access messages when a non-admin or logged-out user navigates directly to an admin-only page. API error handling was improved so unauthorized and forbidden responses produce clearer user-facing messages.

This version establishes a cleaner permission model: normal users can browse products and manage their own reviews, while admins manage product data, imports, and user-management endpoints.

## Screenshots

### Products Page

![Products Page](docs/images/ProductPage.png)

### Product Details Page

![Product Details Page](docs/images/ProductDetails.png)

### Add Product Page

![Add Product Page](docs/images/AddProduct.png)

### Login Page

![Login Page](docs/images/LoginPage.png)
