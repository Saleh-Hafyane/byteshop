# ByteShop

My full-stack e-commerce project — Spring Boot on the back, Angular on the front, Postgres holding it all together. I built it to learn how a real store actually fits together: auth, catalog, cart, checkout, admin, the whole flow.

The shop comes with 25 electronics products already loaded (laptops, MacBooks, keyboards, mice) so it feels like a store from the first click, not an empty demo.

## What you can do with it

As a shopper: browse and search the catalog, filter by category, sort, open product pages, add things to the cart (it checks stock live), check out with a validated form, then follow your orders by tracking number.

As an admin (login `admin` / `admin123`): add and edit products with photo upload, manage categories, and move orders along from PENDING to SHIPPED to DELIVERED.

## Stack

- Backend: Spring Boot 3.2, Java 21, Spring Data JPA + Spring Data REST for catalog reads
- Auth: Spring Security with JWT, USER and ADMIN roles
- Database: PostgreSQL 17 (Docker). Backend tests run on H2 in Postgres mode so `mvn test` needs no database at all
- Frontend: Angular 17 standalone components, reactive forms, a small pipe that sorts out product image URLs
- Uploads go through a `FileStorageService` (type + size checked) and are served back from `/api/files`

## Run everything with Docker

```bash
docker compose up -d
```

Then open http://localhost:4200 for the store. API is at http://localhost:8080/api. Admin login is `admin` / `admin123`.

## If you'd rather run it piece by piece

You need Java 21, Node 18+, and either Docker or a local Postgres on 5432. (I developed this on WSL, in case paths look odd.)

Database first — either let compose create it:

```bash
docker compose up -d postgres-db
```

which imports `byteshop_schema.sql` (schema + the 25 products) on first start. Or if you already have a `byteshop` database and don't want to wipe it:

```bash
psql -U postgres -d byteshop -f seed_catalog.sql
```

Local Postgres instead of Docker? Create the `byteshop` database, check the credentials in `src/main/resources/application.properties`, then:

```bash
psql -U postgres -d byteshop -f byteshop_schema.sql
```

Backend:

```bash
mvn clean compile
mvn spring-boot:run
```

Backend runs on http://localhost:8080.

Frontend, from the `frontend` folder:

```bash
cd frontend
npm install
npm start
```

Store runs on http://localhost:4200.

## Sanity checks

```bash
mvn test
cd frontend && npx ng build --configuration development
```

Backend suite is 37 tests, all green, no Docker needed. Frontend builds clean.

## Where things live

```
src/main/java/com/salehhafyane/ecommerce/
  auth/          login, register, JWT bits
  config/        security, CORS, admin seeding
  controller/    AdminProduct, AdminOrder, Checkout, File, UserOrder...
  dto/           mappers, purchase + status requests
  entity/        Product, ProductCategory, Order, User...
  service/       Checkout, OrderAdmin/Query, FileStorage...
frontend/src/app/
  components/    16 standalone components, catalog to checkout to admin
  services/      product, cart, auth, checkout, order, category
  pipes/         product-image pipe
byteshop_schema.sql   schema + seed, used by docker on first start
seed_catalog.sql      same seed, safe to run on a live database
```
