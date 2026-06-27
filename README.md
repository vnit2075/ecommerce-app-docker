# ShopEZ - Spring Boot E-Commerce Application

A full-featured e-commerce web application built with **Spring Boot 3.3.4** and **MySQL**, featuring a modern dark-themed UI.

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.3.4, Spring MVC |
| Security | Spring Security 6 |
| Database | MySQL 8.0 + Spring Data JPA |
| Templates | Thymeleaf 3 |
| Build | Maven |
| Container | Docker + Docker Compose |

## ✨ Features

- 🛍️ Product catalog with search, filter by category, and pagination
- 🛒 Shopping cart (add, update quantity, remove items)
- 💳 Checkout with shipping address + payment method selection
- 📦 Order history and order detail tracking
- 👤 User registration and login (Spring Security)
- 🔐 Role-based access (ADMIN / CUSTOMER)
- 🎛️ Admin panel: manage products, categories, orders, users

## 🏃 Run Locally

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0 running on port 3306

### Steps
```bash
# 1. Create the database (MySQL)
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS ecommerce_db;"

# 2. Update credentials in src/main/resources/application.properties
#    spring.datasource.username=root
#    spring.datasource.password=<your-password>

# 3. Run the application
mvn spring-boot:run
```

Visit: **http://localhost:8080**

### Demo Credentials
| Role | Email | Password |
|------|-------|----------|
| Admin | admin@shopez.com | admin123 |
| Customer | john@example.com | john1234 |

## 🐳 Run with Docker

```bash
# Build and start both app + MySQL
docker-compose up --build

# Access at http://localhost:8080
# Stop containers
docker-compose down
```

## 📁 Project Structure

```
src/main/java/com/sak/ecommerce/
├── config/          # Security & data seeding
├── controller/      # Web controllers (Home, Product, Cart, Order, Admin)
├── model/           # JPA entities (User, Product, Category, Cart, Order)
├── repository/      # Spring Data JPA repositories
└── service/         # Business logic services

src/main/resources/
├── templates/       # Thymeleaf HTML templates
│   └── admin/       # Admin-specific pages
└── static/
    ├── css/         # style.css + admin.css
    └── js/          # main.js
```

## 🗄️ Database Schema

```
users → carts → cart_items → products → categories
orders → order_items → products
```
