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

## MVP Status

The app currently includes product browsing, product details, product search, product sorting/filtering, review submission, review editing/deletion for review owners, duplicate review prevention, product creation, seeded demo data, average ratings, review counts, frontend registration, session-based login/logout, persisted login state after refresh, and a My Reviews page where logged-in users can view, edit, delete, and navigate back to products they have reviewed.

The app also includes product metadata and discovery features. Products can now store and display category, image URL, nutrition fields, and source attribution. Users can filter products by category or brand, and product cards/detail pages display more useful product information.

## Future Improvements

- Real data imports
- Barcode support
- More detailed nutrition and serving size information
- User profile pages
- Favorite or saved products
- Deployment
- Automated tests
- Docker setup

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

## Screenshots

### Products Page

![Products Page](docs/images/ProductPage.png)

### Product Details Page

![Product Details Page](docs/images/ProductDetails.png)

### Add Product Page

![Add Product Page](docs/images/AddProduct.png)

### Login Page

![Login Page](docs/images/LoginPage.png)
