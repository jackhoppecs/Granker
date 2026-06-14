# Granker

Granker is a full-stack frozen foods review app. Users can browse frozen food products, view product details, read reviews, submit reviews, and add new products.

## Features

- View all frozen food products
- Search products by name or brand
- View product details
- View reviews for a specific product
- Submit a review for a product
- Add new products from the frontend
- Basic session-based login flow for submitting reviews
- Logout support
- Average product ratings
- Edit and delete your own reviews
- Prevent duplicate reviews from the same user on the same product
- Review timestamps for created and updated reviews
- Delete confirmation for reviews

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

The app currently includes product browsing, product details, product search, review submission, review editing/deletion for review owners, duplicate review prevention, product creation, seeded demo data, average ratings, review counts, and a basic session-based login/logout flow.

## Future Improvements

- Full registration and login UI
- Product images
- Product categories
- Nutrition fields
- Sorting and filtering
- Deployment
- Automated tests
- Docker setup

## Version Status

### v1.1.0

This version adds average product ratings, review counts, and logout support.

### v1.2.0

This version adds review management features. Logged-in users can edit and delete their own reviews, while review controls are only shown for reviews owned by the current user. Reviews now include created and updated timestamps, duplicate reviews are prevented so each user can only review a product once, and product rating summaries update after reviews are created, edited, or deleted.

## Screenshots

### Products Page

![Products Page](docs/images/ProductPage.png)

### Product Details Page

![Product Details Page](docs/images/ProductDetails.png)

### Add Product Page

![Add Product Page](docs/images/AddProduct.png)

### Login Page

![Login Page](docs/images/LoginPage.png)
