# ByteShop

A full‑stack e‑commerce application built with **Spring Boot 3 (Java 21)** for the backend and **Angular** for the frontend.

---

## 📦 Project Overview
- **Backend**: Spring Boot REST API, JWT authentication, MySQL persistence, Lombok, JPA.
- **Frontend**: Angular 17 (stand‑alone components), Bootstrap‑NG, RxJS services.
- **Features**: Product catalog, product categories, cart, checkout, admin CRUD, login/registration.

---

## 🛠 Prerequisites
| Tool | Minimum version |
|------|-----------------|
| Java | 21 (OpenJDK) |
| Maven | 3.9+ |
| Node.js | 20.x |
| npm | 10.x |
| MySQL | 8.x |

> **Note**: The backend uses a MySQL database called `byteshop` with a user `user` / password `password`. Adjust `src/main/resources/application.properties` if you need different credentials.

---

## 🚀 Getting Started
### 1. Clone the repository
```bash
git clone https://github.com/your‑username/byteshop.git
cd byteshop
```
### 2. Configure the database
```sql
CREATE DATABASE IF NOT EXISTS byteshop;
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON byteshop.* TO 'user'@'localhost';
FLUSH PRIVILEGES;
```
If you use a different user/password, edit `src/main/resources/application.properties` accordingly:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/byteshop
spring.datasource.username=your_user
spring.datasource.password=your_pass
```
### 3. Backend setup
```bash
# Ensure JAVA_HOME points to Java 21
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
# Build and run the Spring Boot API
mvn clean install
JAVA_HOME=$JAVA_HOME mvn spring-boot:run
```
The API will start on **http://localhost:8080**. You can verify it with:
```bash
curl http://localhost:8080/api/product-category
```
You should see a JSON payload with the seeded categories (`Laptops`, `Gaming Laptops`).
### 4. Frontend setup
```bash
cd frontend
npm install        # install dependencies
npm start          # starts the dev server on http://localhost:4200
```
The Angular app will automatically open in your browser. The sidebar shows the product categories pulled from the backend.

---

## 📋 Testing
- **Backend unit tests**: `mvn test`
- **Frontend unit tests**: `npm run test`
- **End‑to‑end**: You can use Cypress (installed as a dev dependency) with `npm run e2e`.

---

## 🐞 Troubleshooting
| Symptom | Likely cause | Fix |
|---------|--------------|-----|
| `Connection refused` on `localhost:8080` | Backend not running or wrong DB credentials | Ensure `mvn spring-boot:run` is active and `application.properties` matches your MySQL setup |
| Empty product/category view | Frontend cannot reach API (CORS or wrong URL) | Verify `category.service.ts` points to `http://localhost:8080/api/product-category` and that the backend is reachable |
| `npm start` hangs | Port 4200 already in use | Kill the process or change the port in `angular.json` (`"port": 4201` for example) |

---

## 📄 License
This project is licensed under the MIT License – see the `LICENSE` file for details.

---

**Happy coding!**
