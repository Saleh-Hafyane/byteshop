-- Seed rows for the hermetic H2 test database.
-- Mirrors the seed section of byteshop_schema.sql (cities, users, categories, products)
-- so integration tests exercise the same catalog as production (e.g. purchase of product id 1).
-- Table DDL itself is generated from the JPA entities (ddl-auto=create-drop);
-- spring.jpa.defer-datasource-initialization=true guarantees this script runs after that DDL.

INSERT INTO `city` (`id`, `city_name`) VALUES
(1,'Casablanca'),
(2,'Rabat'),
(3,'Marrakech'),
(4,'Fes'),
(5,'Tangier'),
(6,'Agadir');

INSERT INTO `db_user` (`id`, `email`, `firstname`, `lastname`, `password`, `role`, `username`) VALUES
(1,'admin@example.com','Admin','System','$2a$10$dCwH3P8/MyAICNvL95dVUOQZhKOYbtyNh7X90P.L/WwDFkTMQA.Iy','ADMIN','admin'),
(2,'user@example.com','Test','User','$2a$10$NSnhZcKFPWV0cNjbvmYaPOMyP75kJFj5o0Bhrx7o2K9.oMIfkRKPe','USER','testuser');

INSERT INTO `product_category` (`id`, `category_name`) VALUES
(1,'Electronics'),
(2,'Books'),
(3,'Clothing');

INSERT INTO `product` (`id`, `date_created`, `description`, `image_url`, `last_updated`, `name`, `unit_price`, `units_in_stock`, `category_id`) VALUES
(1,'2026-01-01 00:00:00.000000','Latest smartphone with advanced features','https://via.placeholder.com/150','2026-01-01 00:00:00.000000','Smartphone X1',699.99,50,1),
(2,'2026-01-01 00:00:00.000000','High-quality wireless headphones','https://via.placeholder.com/150','2026-01-01 00:00:00.000000','Wireless Headphones',149.99,100,1),
(3,'2026-01-01 00:00:00.000000','Comprehensive guide to Spring Boot','https://via.placeholder.com/150','2026-01-01 00:00:00.000000','Spring Boot in Action',49.99,75,2),
(4,'2026-01-01 00:00:00.000000','Angular development handbook','https://via.placeholder.com/150','2026-01-01 00:00:00.000000','Angular Masterclass',59.99,60,2),
(5,'2026-01-01 00:00:00.000000','Comfortable cotton t-shirt','https://via.placeholder.com/150','2026-01-01 00:00:00.000000','Classic T-Shirt',29.99,200,3),
(6,'2026-01-01 00:00:00.000000','Durable denim jeans','https://via.placeholder.com/150','2026-01-01 00:00:00.000000','Denim Jeans',79.99,150,3);
