# ByteShop - E-Commerce Application

ByteShop is a full-stack e-commerce web application featuring a Java / Spring Boot backend and an Angular frontend.

## 🚀 Key Features
- **Product Management (CRUD):** Fully managed by the Admin.
- **Category Management (CRUD):** Fully managed by the Admin.
- **User Authentication:** Secured using Spring Security with JWT token-based authentication.
- **Shopping Cart & Checkout:** Customers can browse items, manage their cart, and place orders.

---

## 🏗️ Technical Architecture
The project is built on a clean, layered architecture:
- **Presentation Layer (Frontend):** Angular framework, structured components, and reactive services.
- **Web Controller Layer:** Spring MVC REST Controllers (`AdminProductController`, `AuthController`, etc.) managing request-response mapping and REST endpoints.
- **Service/Business Layer:** Clear separation of concerns with Services organized into Interfaces and Implementations (`IProductService` & `ProductServiceImpl`, `IProductCategoryService` & `ProductCategoryServiceImpl`).
- **Data Access Layer (DAO):** Spring Data JPA repositories communicating with PostgreSQL.
- **Database:** PostgreSQL relational database.

---

## 🛠️ Prerequisites
Before running the application, make sure you have the following installed:
- **Java JDK 21** or higher
- **Node.js** (v18+) and **npm**
- **PostgreSQL Server** (listening on `localhost:5432`) — or **Docker**, to run it via `docker compose`
- **Maven** (optional, wrapper script `mvn` is included)
- **note** : i used WSL while developing this project

---

## 🔧 Database Setup
### Option A — Docker Compose (recommended)
```bash
docker compose up -d postgres-db
```
This starts PostgreSQL 17 on `localhost:5432` (user `postgres`, password `postgres`, database `byteshop`) and automatically imports `byteshop_schema.sql` (schema + seed/catalog data) on first start of the empty volume.

### Option B — Local PostgreSQL server
1. Create a database named `byteshop`:
   ```sql
   CREATE DATABASE byteshop;
   ```
2. Make sure the credentials in `src/main/resources/application.properties` match your server (`jdbc:postgresql://localhost:5432/byteshop`, user `postgres`).
3. To import the schema and seed data, you can run:
   ```bash
   psql -U postgres -d byteshop -f byteshop_schema.sql
   ```

> **Note:** the backend test suite (`mvn test`) is hermetic — it uses an in-memory H2 database in PostgreSQL mode and needs neither Docker nor a local server.

---

## 🏁 How to Run

### 1. Run the Spring Boot Backend
From the project root directory:
```bash
# Clean and compile the application
mvn clean compile

# Run the Spring Boot server
mvn spring-boot:run
```
The backend server will start at **http://localhost:8080**.

### 2. Run the Angular Frontend
Navigate to the `frontend` directory:
```bash
cd frontend

# Install package dependencies
npm install

# Run the local development server
npm start
```
Open **http://localhost:4200** in your browser to view the application.
