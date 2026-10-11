--
-- PostgreSQL database dump
--

\restrict VH2P31Hhz8rgm1Tt7ga3p3fZUu2S7lGFxVm55j64JRkkv0MXUchznpmSc7yG71L

-- Dumped from database version 17.11 (Debian 17.11-1.pgdg13+2)
-- Dumped by pg_dump version 17.11 (Debian 17.11-1.pgdg13+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: address; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.address (
    id bigint NOT NULL,
    city character varying(255),
    full_address character varying(255)
);


--
-- Name: address_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.address_id_seq OWNED BY public.address.id;


--
-- Name: city; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.city (
    id bigint NOT NULL,
    city_name character varying(255)
);


--
-- Name: city_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.city_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: city_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.city_id_seq OWNED BY public.city.id;


--
-- Name: db_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.db_user (
    id bigint NOT NULL,
    email character varying(255),
    firstname character varying(255),
    lastname character varying(255),
    password character varying(255),
    role character varying(255),
    username character varying(255),
    CONSTRAINT db_user_role_check CHECK (((role)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying])::text[])))
);


--
-- Name: db_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.db_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: db_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.db_user_id_seq OWNED BY public.db_user.id;


--
-- Name: order_item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.order_item (
    id bigint NOT NULL,
    image_url character varying(255),
    product_id bigint,
    product_name character varying(255),
    quantity integer,
    unit_price numeric(38,2),
    order_id bigint
);


--
-- Name: order_item_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.order_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: order_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.order_item_id_seq OWNED BY public.order_item.id;


--
-- Name: orders; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    date_created timestamp(6) without time zone,
    last_updated timestamp(6) without time zone,
    order_tracking_number character varying(255) NOT NULL,
    status character varying(255),
    total_price numeric(38,2) NOT NULL,
    total_quantity integer NOT NULL,
    address_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT orders_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'CONFIRMED'::character varying, 'SHIPPED'::character varying, 'DELIVERED'::character varying, 'CANCELLED'::character varying])::text[])))
);


--
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- Name: product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product (
    id bigint NOT NULL,
    date_created timestamp(6) without time zone,
    description character varying(255),
    image_url character varying(255),
    last_updated timestamp(6) without time zone,
    name character varying(255),
    unit_price numeric(38,2),
    units_in_stock integer,
    category_id bigint NOT NULL
);


--
-- Name: product_category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product_category (
    id bigint NOT NULL,
    category_name character varying(255)
);


--
-- Name: product_category_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_category_id_seq OWNED BY public.product_category.id;


--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;


--
-- Name: address id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.address ALTER COLUMN id SET DEFAULT nextval('public.address_id_seq'::regclass);


--
-- Name: city id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.city ALTER COLUMN id SET DEFAULT nextval('public.city_id_seq'::regclass);


--
-- Name: db_user id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.db_user ALTER COLUMN id SET DEFAULT nextval('public.db_user_id_seq'::regclass);


--
-- Name: order_item id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_item ALTER COLUMN id SET DEFAULT nextval('public.order_item_id_seq'::regclass);


--
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);


--
-- Name: product_category id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_category ALTER COLUMN id SET DEFAULT nextval('public.product_category_id_seq'::regclass);


--
-- Data for Name: address; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.address (id, city, full_address) VALUES (1, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (2, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (3, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (4, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (5, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (6, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (7, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (8, 'Rabat', 'Smoke Street 6b');
INSERT INTO public.address (id, city, full_address) VALUES (9, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (10, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (11, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (12, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (13, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (14, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (15, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (16, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (17, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (18, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (19, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (20, 'Casa', 'Rue 10');
INSERT INTO public.address (id, city, full_address) VALUES (21, 'Rabat', 'er');
INSERT INTO public.address (id, city, full_address) VALUES (22, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (23, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (24, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (25, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (26, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (27, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (28, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (29, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (30, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (31, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (32, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (33, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (34, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (35, 'Rabat', 'Avenue Mohammed V');
INSERT INTO public.address (id, city, full_address) VALUES (36, 'Rabat', 'Avenue Mohammed V');


--
-- Data for Name: city; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.city (id, city_name) VALUES (1, 'Casablanca');
INSERT INTO public.city (id, city_name) VALUES (2, 'Rabat');
INSERT INTO public.city (id, city_name) VALUES (3, 'Marrakech');
INSERT INTO public.city (id, city_name) VALUES (4, 'Fes');
INSERT INTO public.city (id, city_name) VALUES (5, 'Tangier');
INSERT INTO public.city (id, city_name) VALUES (6, 'Agadir');


--
-- Data for Name: db_user; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (2, 'user@example.com', 'Test', 'User', '$2a$10$NSnhZcKFPWV0cNjbvmYaPOMyP75kJFj5o0Bhrx7o2K9.oMIfkRKPe', 'USER', 'testuser');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (3, 'sddda@fddf.com', 'ad', 'sd', '$2a$10$qn2xRdxiVqGNHQRyXU.WLOjORaFGsmzE5HxAwzy9LZ06JEroRCy1K', 'USER', 'dsd');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (4, 'use1r@example.com', 'dwed', 'wedwed', '$2a$10$Xoi23acfic937LSk2sIsROKb7kVWem4cNBRMPqGvp4Qbnl3jBZ/0q', 'USER', 'testuser1');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (5, 'user-bfc9426e@example.com', 'Test', 'User', '$2a$10$IVkyz225J.J5ybPm6fzmp.dFPr3G7N7ABCVE0/MSWyA22q9pwGimG', 'USER', 'user-bfc9426e');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (6, 'user-d85fdc12@example.com', 'Test', 'User', '$2a$10$EEo46ehKIM6cL4dtfkR0fO44icf9TEhrSrK1XIcGFOLEaCprZkmN2', 'USER', 'user-d85fdc12');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (7, 'user-e58fa1aa@example.com', 'Test', 'User', '$2a$10$x6J0btEFutzPg1gLHppGYeI0L.XW6.cX9leKrQXCXLZpXP3jzU9pa', 'USER', 'user-e58fa1aa');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (8, 'user-2d3047c9@example.com', 'Test', 'User', '$2a$10$HfJPIkAz6J1LrijLDpZO2ux4jllyeaWD3DH1u2AFRzU807FMKtYrS', 'USER', 'user-2d3047c9');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (9, 'fix1791559799@example.com', 'Test', 'User', '$2a$10$NOF7kF4XpptEyhJgoupK2uMP8ZLfVClE//fOpHRC4W3Fzeq3O5m0K', 'USER', 'fix1791559799');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (10, 'user-05f75a32@example.com', 'Test', 'User', '$2a$10$r5/GnGu5Na.uUMdY6W7rzu7AxK9f5AKm1gzunIGsci1KZUAUku9ly', 'USER', 'user-05f75a32');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (11, 'user-80d2dcd4@example.com', 'Test', 'User', '$2a$10$j9SHxiBk0RqoH5xaKqBzbOGQMI64v8FedKNPDgTTo2zFi/zKwrzP2', 'USER', 'user-80d2dcd4');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (12, 'user-6cffcc7f@example.com', 'Test', 'User', '$2a$10$skWPhJHwIFQTXpc2d./DEe0W74lqF.e118/BFtRfeXdFHkcIL6G/O', 'USER', 'user-6cffcc7f');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (13, 'user-eb81dadc@example.com', 'Test', 'User', '$2a$10$e2HPjItwEPB2RWZ9CmXS5ulp8P.8ERUxS0jKslrTYG7Nlfy7V4u5y', 'USER', 'user-eb81dadc');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (14, 'snap1791561609@example.com', 'Test', 'User', '$2a$10$vPZGef0EFK/0i3RWASbd/ON3HXN1Id6KcD.8yUZ4e6TLKJIJDEbze', 'USER', 'snap1791561609');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (15, 'snap1791561670@example.com', 'Test', 'User', '$2a$10$1JZ7SzqziNVIPU4QHgUYc.Gj8g5dI0J4BG9XNIee6gtj/9tPx7LFW', 'USER', 'snap1791561670');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (16, 'user-fea5d109@example.com', 'Test', 'User', '$2a$10$m0E6xoi/7e3Y2Fux.BS3MOFQpqSz7WBrpekTcNKS9wUutY/XxuFuu', 'USER', 'user-fea5d109');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (17, 'user-b49b8abc@example.com', 'Test', 'User', '$2a$10$udxqa6SvqebnrXRigPhg3ufA1SsMYQmJJbf4qnTCmxM/rW0E3Hr2O', 'USER', 'user-b49b8abc');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (18, 'smoke6b_1791562344@test.com', 'Smoke', 'Test', '$2a$10$albSnrnWuhczR/gxmlH8r.s8nZvqMiy5apukjVYxWqmmmfqYUG3mO', 'USER', 'smoke6b_1791562344');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (19, 'smoke6b_a202a20939@test.com', 'Smoke', 'Test', '$2a$10$qFdKeeql3S2oqyNAWMC82.CBdslVxd.chaGUugN0at4RuNUQ5mKUW', 'USER', 'smoke6b_a202a20939');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (20, 'user-24395250@example.com', 'Test', 'User', '$2a$10$jz47JkistxqQl/5JT707keiyIIqwGm3yDSvjJtH.bK0Lbt4/b.CrK', 'USER', 'user-24395250');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (21, 'user-c458f68d@example.com', 'Test', 'User', '$2a$10$0nXHDaAEEOmOcKkLy87kI.zjbpCP9pRyISm/oSOMhf/GYE.1go4nW', 'USER', 'user-c458f68d');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (22, 'user-107e5cfb@example.com', 'Test', 'User', '$2a$10$iBuHtiEnzC1K8VjQz9dcW.83d0.BnRgm19qsrIua0TEkdzAXaIuR.', 'USER', 'user-107e5cfb');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (23, 'user-8ebda631@example.com', 'Test', 'User', '$2a$10$/.vrLIy4THXo.qUgaF57wehNVvsFj828CvhUd/SWJjo5Hha0Y2Zhq', 'USER', 'user-8ebda631');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (24, 'user-c84c2e23@example.com', 'Test', 'User', '$2a$10$4ptTIYPTwLiePlzWU/Whfe/q6OMJvjAxw3k70N9OT0Wv4TPo3oap6', 'USER', 'user-c84c2e23');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (25, 'user-894d5b02@example.com', 'Test', 'User', '$2a$10$PonPLFZDhwv9VEIx4fbjNuznGi2rA9F8PBpB6jzU5WVI.A.j3Y.YS', 'USER', 'user-894d5b02');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (26, 'user-a7f90b70@example.com', 'Test', 'User', '$2a$10$u2J45QQOHukBy/n9O5180ubbI8YpooQwM3fyYQ29rcOspnT3rciGS', 'USER', 'user-a7f90b70');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (27, 'user-bde5d10d@example.com', 'Test', 'User', '$2a$10$DxJr2BMa6q84doMLVRICROMDsm89pVXoG7uoXww/35bn7n1PUzJaG', 'USER', 'user-bde5d10d');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (28, 'user-ca92ce06@example.com', 'Test', 'User', '$2a$10$aMxd0/Wzl4M75puFtiBGteZ7EhezqqmI40M5FNpqylqisF5b14cSG', 'USER', 'user-ca92ce06');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (29, 'user-1af8117d@example.com', 'Test', 'User', '$2a$10$pwUb3uGE7yMaxllcU.u1Auq1W1ktFsxyZSIR/8DhqD.pve24DnsX.', 'USER', 'user-1af8117d');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (30, 'user-b7050c18@example.com', 'Test', 'User', '$2a$10$JB5ISFswVFVo8JOIKPqAv.6jAlY8WXxlbrUOmfFvCngEI5fPUHep2', 'USER', 'user-b7050c18');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (31, 'admin-dce77ae1@example.com', 'Admin', 'Test', '$2a$10$COQqrjqDzdyyViYpFLwFrehPHvVhuoICnoxucZe2p7pM1sqTNgNMm', 'ADMIN', 'admin-dce77ae1');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (32, 'user-45bd6a0d@example.com', 'Test', 'User', '$2a$10$AtdK.gsDUFOKO8cA3mpwzOjPVnJ6LRst.P4.1JjcMkwBJqSLtSf0q', 'USER', 'user-45bd6a0d');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (33, 'user-6b447fcd@example.com', 'Test', 'User', '$2a$10$CasrSzvU.Y2MVwx/jkP8hu5qmMFO5tgVQe6wU9IAmN9InNAp8PxRi', 'USER', 'user-6b447fcd');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (34, 'user-8edcefa9@example.com', 'Test', 'User', '$2a$10$Jek/xNuPEcYyNKiH/P8EteckwvJYjNazhi/jDaDbfBFWzj0qWVcUC', 'USER', 'user-8edcefa9');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (35, 'admin-4dcea99f@example.com', 'Admin', 'Test', '$2a$10$ettCE2KVLZ/tyzUtgeCYMOiKudfusYESDA6PkRUyvZIxfcjhzdCPm', 'ADMIN', 'admin-4dcea99f');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (36, 'user-89d21cbe@example.com', 'Test', 'User', '$2a$10$8mbAm75aH7lI1e.FfpEeM.XroLkD2VlU0qd0YXmJrKOzrLZv9Xhpu', 'USER', 'user-89d21cbe');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (37, 'user-f982c802@example.com', 'Test', 'User', '$2a$10$VGwjwcQ09SXWsbFSPoQdoeDWKTKSGo4n7Ut.WjmQoPKSgtjoec4Im', 'USER', 'user-f982c802');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (38, 'user-bc11ec91@example.com', 'Test', 'User', '$2a$10$QcJq6BaGJiNux8VoaiSo1.HD8B.3NJKUdTWrYOCDQjf02AenARaM6', 'USER', 'user-bc11ec91');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (39, 'user-3f7bacfd@example.com', 'Test', 'User', '$2a$10$C.tHMa/0kj/6yMH4jyybxu9k1TUysGBS6r0KhIWdU0kpZTc4FyApy', 'USER', 'user-3f7bacfd');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (40, 'admin-3b38556b@example.com', 'Admin', 'Test', '$2a$10$BCwZcrJwkeZynqyUU/5ozO73d3Ad9C9hDSAMb.o4VxMMWQcTVucsa', 'ADMIN', 'admin-3b38556b');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (41, 'user-495e346c@example.com', 'Test', 'User', '$2a$10$qZyVLtuk9blGBr4gNZo9.ev8/W7bCS.iqJy39EVlt0byc0W9I2MPq', 'USER', 'user-495e346c');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (42, 'admin-53ccfb25@example.com', 'Admin', 'Test', '$2a$10$7G4Cn7gb3GpSbnSVJcsHuO.1VOWcV3n2Jkf4/AA/47k0m55uFizcS', 'ADMIN', 'admin-53ccfb25');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (43, 'user-77515411@example.com', 'Test', 'User', '$2a$10$zu.4//3Drf3RNNn4GWV.fO4fKXW8SwFybU5PRPljViCP4MNEOqsLm', 'USER', 'user-77515411');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (44, 'user-08151e4b@example.com', 'Test', 'User', '$2a$10$Xj2FtqSbHo.q8wVzMsfqruK5h8UBtm9hod1/b9CmeqC4cPxieIhUG', 'USER', 'user-08151e4b');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (45, 'user-8705e5cf@example.com', 'Test', 'User', '$2a$10$fT6d2BLGmFiaqN5NmfhTnOC5vvCa6WsBVu9YkFgE641ToQT98AwgW', 'USER', 'user-8705e5cf');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (46, 'admin-f26e273d@example.com', 'Admin', 'Test', '$2a$10$8sJK0sJRchTCM2icXalwsuf0tziK32KKsiMUsdZPcUVk6lSt7YAUe', 'ADMIN', 'admin-f26e273d');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (47, 'admin-5503f06a@example.com', 'Admin', 'Test', '$2a$10$G3rS33MWuyl5WWAeX696WuWvHjq0p.4cnsIcOQkuAgFD1KdljecPq', 'ADMIN', 'admin-5503f06a');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (48, 'user-582be2e5@example.com', 'Test', 'User', '$2a$10$eJp0bIND/AiKs3y85jjePu/.QDH81odIwgkiUTme50nmiaY5HbjIy', 'USER', 'user-582be2e5');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (49, 'user-60e95075@example.com', 'Test', 'User', '$2a$10$uMrkjJpciAXB9zjjRPm7XOetp/VFQJRhJ8Dhm0e.3PYEGs5g1oWUe', 'USER', 'user-60e95075');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (50, 'user-da9786d2@example.com', 'Test', 'User', '$2a$10$wk8HBjPJ.COrqTWPByI8MuB3apcLZHyADJyWuWABkx3N4DDH6XKhC', 'USER', 'user-da9786d2');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (51, 'admin-0e53ef54@example.com', 'Admin', 'Test', '$2a$10$xdvyVEM/mVEChlFbb7AuA.o/.uinCYh/RVIMJtl7T3u3/polGcKpq', 'ADMIN', 'admin-0e53ef54');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (52, 'user-f8e59ccf@example.com', 'Test', 'User', '$2a$10$mFOVEJJKK2a/MbE4VHcgs.lxk.0YPP8jgoObisOSTyqwtsGozh.fC', 'USER', 'user-f8e59ccf');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (53, 'user-51b571f5@example.com', 'Test', 'User', '$2a$10$JkTufrtp5MA4F.98t7QRoeXcIQfNKb9w/EAZZ8wvaGMaUkzz3AsM.', 'USER', 'user-51b571f5');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (54, 'user-c32887cc@example.com', 'Test', 'User', '$2a$10$cYwY/fCXgwY0JqRh8w5NwefywmDCzrAjAegWNwZ5ZGcM.Y5SNL8cm', 'USER', 'user-c32887cc');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (55, 'admin@example.com', NULL, NULL, '$2a$10$ymD2NLErbrSFz.xdJcUmqesAnFhjyy5VPe79XFoaxIhNrUJrWe13K', 'ADMIN', 'admin');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (56, 'ch10_mv2peuo8@test.com', 'Ch', 'Ten', '$2a$10$G0oDQOVSSkjs/yOFr45Rv.dRhLAUxYMXp4mdCkQuCRkBNh3iy00uK', 'USER', 'ch10_mv2peuo8');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (57, 'user-574bc729@example.com', 'Test', 'User', '$2a$10$thhy40Cpj1t/C66u0gkQMe5YbQmDzB.uiGam2r4DgXjDCC7OBPDX6', 'USER', 'user-574bc729');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (58, 'admin-b69a2338@example.com', 'Admin', 'Test', '$2a$10$SqgMtNqntu70hZAoGeDdSOUJRIf0uhackc2L6oCVoS8jy1lC9QU5G', 'ADMIN', 'admin-b69a2338');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (59, 'user-a1f11177@example.com', 'Test', 'User', '$2a$10$TKqb.F6Yda93Z04mx73Q6OFYf0U5FyUVqRPMEjx.FZk65M8Pt.TPS', 'USER', 'user-a1f11177');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (60, 'admin-0be5bdad@example.com', 'Admin', 'Test', '$2a$10$Em88PHwqEaNOIIpZqu2sve7Rpl6RBzvHo79tOzw.dxTf86QUS5SGS', 'ADMIN', 'admin-0be5bdad');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (61, 'user-068acc0b@example.com', 'Test', 'User', '$2a$10$yd0MnxTj2QqD.bapjyFrN.LJMFgBFFxrJc7VN3zXa7DQiKkq7WV6m', 'USER', 'user-068acc0b');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (62, 'user-b6373734@example.com', 'Test', 'User', '$2a$10$xOZsuJi9l.Bd9nAAV.ejl.hFtwo9H9xRXCqhUxL2sMqI3LvH8.TNi', 'USER', 'user-b6373734');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (63, 'user-fdaf0886@example.com', 'Test', 'User', '$2a$10$tkZayQnbn/heOMg1LpFpT.URJvWDNgHUGp.3GPsnIW0rY.GVXQfoa', 'USER', 'user-fdaf0886');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (64, 'admin-b47f4212@example.com', 'Admin', 'Test', '$2a$10$OjbMfSIAce/aDVvZ9f1m/.rb1F5bFR5NdwtV.2gOs06GV0bTrMtwi', 'ADMIN', 'admin-b47f4212');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (65, 'admin-14caaeec@example.com', 'Admin', 'Test', '$2a$10$Eq8Xgd0gc0wQPbHg8Oh3j.MpBZhU..anmfyaUPJ1Zb7.2b4k92J0q', 'ADMIN', 'admin-14caaeec');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (66, 'user-8d9a2ceb@example.com', 'Test', 'User', '$2a$10$VObcqednmuVlkKwK.2i4T.H3uucRdMcIHEOi04uB7wotun0FQo0sW', 'USER', 'user-8d9a2ceb');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (67, 'user-8b33cff9@example.com', 'Test', 'User', '$2a$10$VINka5TuKfxgbWvr7OBhwelunbI29y20UofG9SH2TQpV48wpO5rl6', 'USER', 'user-8b33cff9');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (68, 'user-7bf77d7c@example.com', 'Test', 'User', '$2a$10$psnw2pE/tnlyDxemukgdSucvJiCEU0B/vP/ZSXfeFEkWNouZtP.Oy', 'USER', 'user-7bf77d7c');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (69, 'admin-3a4cee2f@example.com', 'Admin', 'Test', '$2a$10$potsRgX6k4tyIoOjlTU5su8MRhaioz03YaoEQgWrTYnXK1aRZEaRC', 'ADMIN', 'admin-3a4cee2f');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (70, 'user-341d5b81@example.com', 'Test', 'User', '$2a$10$HpvwyR89vLeOtMLmk1LQLOZek6Dv8yhSv0zBAYLQesDqSbjx5RQYa', 'USER', 'user-341d5b81');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (71, 'user-332a712d@example.com', 'Test', 'User', '$2a$10$PvALqS4f3M.7HrNleusI4u7xnLprS/yh6rLOuWG76bae0pI6R6HZ2', 'USER', 'user-332a712d');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (72, 'user-857ef595@example.com', 'Test', 'User', '$2a$10$2bl1SER5SBd6iq5Srq.UA.tn4LzDhkgnG.zc5flL4Gk6VM2CACwhi', 'USER', 'user-857ef595');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (73, 'user-97fe9c8e@example.com', 'Test', 'User', '$2a$10$rdTJZpn.eNBV5dQsDOLSQexetanhgiM0vq7pEMT4ifs1LNbxvmaWm', 'USER', 'user-97fe9c8e');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (74, 'admin-ac4c74de@example.com', 'Admin', 'Test', '$2a$10$eTfLPWC9WAZAoesq8GfNf.ghXqDgzOm2FYi6fHvIgnUgWGn.f3056', 'ADMIN', 'admin-ac4c74de');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (75, 'user-2a10a144@example.com', 'Test', 'User', '$2a$10$WStsFFlDNDRZ1zPjSVT8v.KxBq8mABvFwTFgayhTIWcSVqPxmZEv2', 'USER', 'user-2a10a144');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (76, 'admin-a3c93725@example.com', 'Admin', 'Test', '$2a$10$d1orKKvvqtlTCbtgU2uUAe3Av8jQnd2fkJCMZ9gh5OoS7/F5C3Cee', 'ADMIN', 'admin-a3c93725');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (77, 'user-c0443de9@example.com', 'Test', 'User', '$2a$10$okyh3pKeyaU7Cq3Jr1cAM.UaSZBDr0UOv6BA7TYu/YoHWo9CeUWkW', 'USER', 'user-c0443de9');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (78, 'user-cfb02ec1@example.com', 'Test', 'User', '$2a$10$fPQb.OCglEmW20J2jJIwoux7EKDtuEJPFv1hqolzJqOz7wmxE2y3.', 'USER', 'user-cfb02ec1');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (79, 'user-fc83c2e7@example.com', 'Test', 'User', '$2a$10$pkNR4SogYbe14pUFQqUgQ.E9u8PVx8fsrc0Az1UN/H1cZdUyaWML2', 'USER', 'user-fc83c2e7');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (80, 'admin-d93f96f7@example.com', 'Admin', 'Test', '$2a$10$6aQF37DCHV8cTSpeVFuez.UpCNZlkQLuNuK9t3I0.edLG/XWPJMba', 'ADMIN', 'admin-d93f96f7');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (81, 'admin-9c0f295c@example.com', 'Admin', 'Test', '$2a$10$.uOr2cIbAbbbvc/xN.laneU8jdh5YJsciCTYufaNaRdSpvUFrg1OO', 'ADMIN', 'admin-9c0f295c');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (82, 'user-429d38dd@example.com', 'Test', 'User', '$2a$10$O0g5FkNPXM9uN4xmhlF5veZRDYdc5ptEwiWAweTw2PpxdGvo8Gjom', 'USER', 'user-429d38dd');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (83, 'user-64f263a9@example.com', 'Test', 'User', '$2a$10$Xp2xAT5lisJ5tEIpL8niZOL3TapjOuounKixoqzs.WBiNI1U0BP9y', 'USER', 'user-64f263a9');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (84, 'user-9135fc3b@example.com', 'Test', 'User', '$2a$10$iUysr9hhUyUo3zRjC3qcvO.ZcHR2loBvJ0Xc4qU5hITPnaI3W8A4i', 'USER', 'user-9135fc3b');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (85, 'admin-23d6faff@example.com', 'Admin', 'Test', '$2a$10$fL3eKyklv.cWdYwO8k4b5e/HEzw3glEgCg9wioHAptdtMx2g369zm', 'ADMIN', 'admin-23d6faff');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (86, 'user-4c7ea67e@example.com', 'Test', 'User', '$2a$10$y.D4loR16Yyp86ntJVact.p5JW//nfhx89EBfBbcaUj.SXYNOQyoa', 'USER', 'user-4c7ea67e');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (87, 'user-b8d77a07@example.com', 'Test', 'User', '$2a$10$rREvhi9xlgS7IeWE3I3KvOEuiZsUVYpFq6uLKWYXxfEkHmIvhJbwq', 'USER', 'user-b8d77a07');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (88, 'user-7f09d35e@example.com', 'Test', 'User', '$2a$10$VwpQ/VeXTccBYGe/koYkWuJgKHH0pxe54occftsxmm.YwQGOfmYAa', 'USER', 'user-7f09d35e');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (89, 'user-0bbda04d@example.com', 'Test', 'User', '$2a$10$DoKj.ZdXVoal1kRifVNKE.ueckYrhhevnLZTdb.ynryW9EMyXlD8i', 'USER', 'user-0bbda04d');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (90, 'admin-83d28ce3@example.com', 'Admin', 'Test', '$2a$10$cEGa1d.MIheKKqf.drejWO7meXeJE91UHyzFbRZNxYMf21hK0jkDK', 'ADMIN', 'admin-83d28ce3');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (91, 'user-62e31004@example.com', 'Test', 'User', '$2a$10$4eIozyDZPFo9MRJGAryGauGIHQA0M9Cge7TSb.P3sSB.4sn7HqWJq', 'USER', 'user-62e31004');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (92, 'admin-3011563e@example.com', 'Admin', 'Test', '$2a$10$S/4/aIJlELc81U7p.fyQCuk4Amls/pcpP8dYH046oE/RfSIEcGc/O', 'ADMIN', 'admin-3011563e');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (93, 'user-08dfd0d5@example.com', 'Test', 'User', '$2a$10$TQr6JyM.ulJ.xezQXLTCw.cCc5dbUi/OMghCLlo7iiBHblTcAg68e', 'USER', 'user-08dfd0d5');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (94, 'user-4e1c6517@example.com', 'Test', 'User', '$2a$10$yZ6zzdiVLflHxnzVI1ev/uRLyUheLVjl5D2AEqDBsqSgXyHseSyeW', 'USER', 'user-4e1c6517');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (95, 'user-13c96c21@example.com', 'Test', 'User', '$2a$10$Ag.9GNVgEH7M6JYJ00cCS.RLaPY5bsqku9EURRhhHUV9qnXIHhRwu', 'USER', 'user-13c96c21');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (96, 'admin-f705b461@example.com', 'Admin', 'Test', '$2a$10$kzPJA0awHvfvNWF3tyoDWulz4Q5UW0AkFvNyWDRBUNZZ3s5b/QoRa', 'ADMIN', 'admin-f705b461');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (97, 'admin-0383ed5b@example.com', 'Admin', 'Test', '$2a$10$9OpEch7Hx7tcI/C6x/8PDe2.zSKlEnojsmS5eMe6EWyc6f8ySfw8i', 'ADMIN', 'admin-0383ed5b');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (98, 'user-bc5b25a7@example.com', 'Test', 'User', '$2a$10$OkJx/LeSMycBoTzkhwsUtO5JdIRSlmDtFGG5DlYlXN8dTX0cRYJjS', 'USER', 'user-bc5b25a7');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (99, 'user-a9e99e71@example.com', 'Test', 'User', '$2a$10$0wrNS1UoFEyzI0J4Lvyou.2srnQz6NEWour1f88f/Y6FV95j0t3v2', 'USER', 'user-a9e99e71');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (100, 'user-83f8d4b4@example.com', 'Test', 'User', '$2a$10$dKoAgHpLCuQ8fpgWgx9RTudNiJU1j7uw8tDDvVH.Iqhkp6BXbBVjC', 'USER', 'user-83f8d4b4');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (101, 'admin-cadfdb68@example.com', 'Admin', 'Test', '$2a$10$CqHYImC6JX/rboBO7xw2p.vUI9DpYxkHeFaSzaxYDCsfD8elGtddm', 'ADMIN', 'admin-cadfdb68');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (102, 'user-54d9846c@example.com', 'Test', 'User', '$2a$10$FmwQKZT3EGXTYjzcOpK/9u.nZbBxXdH3LISQA7tRnNr0z.YJuzwR2', 'USER', 'user-54d9846c');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (103, 'user-8bbebe39@example.com', 'Test', 'User', '$2a$10$Vsj8s4khXvGMlgixaI32RetJ5.q5XnmeO0o4AvSnjU/vIQkNDhsJu', 'USER', 'user-8bbebe39');
INSERT INTO public.db_user (id, email, firstname, lastname, password, role, username) VALUES (104, 'user-f559b2fe@example.com', 'Test', 'User', '$2a$10$DN57csLLJ.lp4hZAt7BUcuSgpP9n6Eidb4Qh9ZWD9d8O/BMkO3CTi', 'USER', 'user-f559b2fe');


--
-- Data for Name: order_item; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (1, 'https://example.com/p.png', 1, NULL, 1, 99.99, 1);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (2, 'https://example.com/p.png', 1, NULL, 1, 99.99, 2);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (3, 'https://example.com/p.png', 1, NULL, 2, 99.99, 3);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (4, 'https://example.com/p.png', 1, NULL, 1, 99.99, 4);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (5, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 5);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (6, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 6);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (7, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 7);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (8, 'x', 1, 'Smartphone X1', 1, 10.00, 8);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (9, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 9);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (10, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 10);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (11, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 11);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (12, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 12);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (13, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 13);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (14, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 14);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (15, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 15);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (16, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 16);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (17, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 17);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (18, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 18);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (19, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 19);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (20, 'x', 1, 'Smartphone X1', 1, 10.00, 20);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (21, 'https://via.placeholder.com/150', 2, 'Wireless Headphones', 1, 149.99, 21);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (22, 'https://via.placeholder.com/150', 3, 'Spring Boot in Action', 1, 49.99, 21);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (23, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 22);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (24, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 23);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (25, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 24);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (26, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 25);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (27, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 26);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (28, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 27);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (29, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 28);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (30, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 29);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (31, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 30);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (32, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 31);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (33, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 32);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (34, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 33);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (35, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 34);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (36, 'https://example.com/img.png', 1, 'Smartphone X1', 1, 99.99, 35);
INSERT INTO public.order_item (id, image_url, product_id, product_name, quantity, unit_price, order_id) VALUES (37, 'https://example.com/p.png', 1, 'Smartphone X1', 1, 99.99, 36);


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (1, '2026-10-08 20:25:56.349', '2026-10-08 20:25:56.349', '23f041ee-098f-4136-8b4d-3887b8bbaaf2', 'PENDING', 99.99, 1, 1, 5);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (2, '2026-10-08 20:52:02.09', '2026-10-08 20:52:02.09', '2fed266d-5d18-4a22-a8f9-eb5ba16c1356', 'PENDING', 99.99, 1, 2, 7);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (3, '2026-10-09 15:30:00.58', '2026-10-09 15:30:00.58', 'c7b30a8d-260d-4e96-b62a-84241b9bc1bf', 'PENDING', 199.98, 2, 3, 9);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (4, '2026-10-09 16:45:01.159', '2026-10-09 16:45:01.16', '4000f061-5fd8-48f8-9c01-0b1b851d5a9d', 'PENDING', 99.99, 1, 4, 10);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (5, '2026-10-09 16:54:14.495', '2026-10-09 16:54:14.495', 'bb07bda6-fd4d-45d6-801a-2ba7991b502e', 'PENDING', 99.99, 1, 5, 12);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (6, '2026-10-09 16:01:10.814', '2026-10-09 16:01:10.814', '45afbc66-613d-4d99-8685-97e475af9a2a', 'PENDING', 99.99, 1, 6, 15);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (7, '2026-10-09 17:08:23.771', '2026-10-09 17:08:23.771', '3c273e0d-0251-454c-9e84-af4c34fc11a6', 'PENDING', 99.99, 1, 7, 16);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (8, '2026-10-09 16:13:40.444', '2026-10-09 16:13:40.444', '20a20249-c0d8-46f0-af23-9272b966b1cf', 'PENDING', 10.00, 1, 8, 19);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (9, '2026-10-09 19:20:56.916', '2026-10-09 19:20:56.916', '5da3a7ab-fb52-446f-b933-195144ecaa28', 'PENDING', 99.99, 1, 9, 21);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (10, '2026-10-09 19:20:58.141', '2026-10-09 19:20:58.141', '8eb56802-aee8-48b8-830e-bb684b145eb6', 'PENDING', 99.99, 1, 10, 23);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (11, '2026-10-09 19:20:59.099', '2026-10-09 19:20:59.099', '99cbed66-e049-4b10-b3c1-65f82542cadb', 'PENDING', 99.99, 1, 11, 26);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (12, '2026-10-09 19:26:31.214', '2026-10-09 19:26:31.214', '2831b3ea-b48a-4eea-abf6-3f5dcfd9cd9c', 'PENDING', 99.99, 1, 12, 29);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (13, '2026-10-09 19:26:32.37', '2026-10-09 19:26:32.37', '2968afea-0c8e-462d-9ee0-a31fb35f3692', 'PENDING', 99.99, 1, 13, 32);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (14, '2026-10-09 19:26:33.397', '2026-10-09 19:26:33.397', '1c77e928-15eb-4fc5-9fcc-ed30e63b31a7', 'PENDING', 99.99, 1, 14, 36);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (15, '2026-10-09 20:08:08.086', '2026-10-09 20:08:08.369', '81c10b98-5e7d-4ea1-9826-44118b950ba1', 'SHIPPED', 99.99, 1, 15, 41);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (16, '2026-10-09 20:08:08.745', '2026-10-09 20:08:08.745', '7acc4ad7-3a6a-4844-897e-daabf20583be', 'PENDING', 99.99, 1, 16, 43);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (17, '2026-10-09 20:08:09.303', '2026-10-09 20:08:09.303', 'e2b98d21-e119-475d-aba2-032d7817c76a', 'PENDING', 99.99, 1, 17, 44);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (18, '2026-10-09 20:08:10.687', '2026-10-09 20:08:10.687', 'c3893c8b-dc51-47b5-b486-0691f99132a9', 'PENDING', 99.99, 1, 18, 48);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (19, '2026-10-09 20:08:11.779', '2026-10-10 21:51:45.502', 'e2a28f2d-4a36-40db-8af2-a5c19384f5de', 'SHIPPED', 99.99, 1, 19, 52);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (20, '2026-10-10 18:04:41.751', '2026-10-10 21:45:05.584', '7606193c-a4c0-4349-b3a9-29347e58e62b', 'SHIPPED', 10.00, 1, 20, 56);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (21, '2026-10-10 21:20:56.93', '2026-10-10 21:44:58.706', 'af094028-0ca0-480a-8155-a47fa519eb12', 'CONFIRMED', 199.98, 2, 21, 2);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (22, '2026-10-11 13:05:34.852', '2026-10-11 13:05:34.991', '137912f7-7a14-4e91-bd1f-6e54f3f16d46', 'SHIPPED', 99.99, 1, 22, 59);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (23, '2026-10-11 13:05:35.297', '2026-10-11 13:05:35.297', '7dbe89cf-e373-4fe4-b28c-0670e6e66824', 'PENDING', 99.99, 1, 23, 61);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (24, '2026-10-11 13:05:35.742', '2026-10-11 13:05:35.742', '45e8a0be-df7a-487d-b3a4-c39912232ec1', 'PENDING', 99.99, 1, 24, 62);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (25, '2026-10-11 13:05:36.917', '2026-10-11 13:05:36.917', '483d88bc-6b56-4641-bb60-ca56eade2257', 'PENDING', 99.99, 1, 25, 66);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (26, '2026-10-11 13:05:37.931', '2026-10-11 13:05:37.931', 'b98a0619-0bde-4fee-8b32-1c52b6a19742', 'PENDING', 99.99, 1, 26, 70);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (27, '2026-10-11 13:24:30.712', '2026-10-11 13:24:30.87', '28d0e2ba-46a2-4aab-930f-a0d8a5b1abe9', 'SHIPPED', 99.99, 1, 27, 75);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (28, '2026-10-11 13:24:31.17', '2026-10-11 13:24:31.17', '46bfeec1-33e0-4b54-a281-e41e93bb34d4', 'PENDING', 99.99, 1, 28, 77);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (29, '2026-10-11 13:24:31.605', '2026-10-11 13:24:31.605', '1fc56455-35b2-45aa-8fb4-f5560b814dd3', 'PENDING', 99.99, 1, 29, 78);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (30, '2026-10-11 13:24:32.767', '2026-10-11 13:24:32.767', '71d4d875-db5e-4161-a6e3-a4cdfa053450', 'PENDING', 99.99, 1, 30, 82);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (31, '2026-10-11 13:24:33.765', '2026-10-11 13:24:33.765', 'c8857723-8d73-4446-a2ba-62715067d89d', 'PENDING', 99.99, 1, 31, 86);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (32, '2026-10-11 13:25:27.86', '2026-10-11 13:25:27.989', '2b283292-e659-40db-9deb-f47371384ae4', 'SHIPPED', 99.99, 1, 32, 91);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (33, '2026-10-11 13:25:28.268', '2026-10-11 13:25:28.268', '45b02dd7-c62b-4b5b-a7fc-07ef43c5f85f', 'PENDING', 99.99, 1, 33, 93);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (34, '2026-10-11 13:25:28.711', '2026-10-11 13:25:28.711', '5f45a5b3-1d1c-4433-82ba-31aaa01182cd', 'PENDING', 99.99, 1, 34, 94);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (35, '2026-10-11 13:25:29.866', '2026-10-11 13:25:29.866', 'a76fb8e7-dc22-46ae-ab0c-ed447e808881', 'PENDING', 99.99, 1, 35, 98);
INSERT INTO public.orders (id, date_created, last_updated, order_tracking_number, status, total_price, total_quantity, address_id, user_id) VALUES (36, '2026-10-11 13:25:30.927', '2026-10-11 13:25:30.927', '5077b5a9-2fcb-46ee-9361-df5abfc60201', 'PENDING', 99.99, 1, 36, 102);


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.product (id, date_created, description, image_url, last_updated, name, unit_price, units_in_stock, category_id) VALUES (1, '2026-01-01 00:00:00', 'Latest smartphone with advanced features', 'https://via.placeholder.com/150', '2026-01-01 00:00:00', 'Smartphone X1', 699.99, 50, 1);
INSERT INTO public.product (id, date_created, description, image_url, last_updated, name, unit_price, units_in_stock, category_id) VALUES (2, '2026-01-01 00:00:00', 'High-quality wireless headphones', 'https://via.placeholder.com/150', '2026-01-01 00:00:00', 'Wireless Headphones', 149.99, 100, 1);
INSERT INTO public.product (id, date_created, description, image_url, last_updated, name, unit_price, units_in_stock, category_id) VALUES (3, '2026-01-01 00:00:00', 'Comprehensive guide to Spring Boot', 'https://via.placeholder.com/150', '2026-01-01 00:00:00', 'Spring Boot in Action', 49.99, 75, 2);
INSERT INTO public.product (id, date_created, description, image_url, last_updated, name, unit_price, units_in_stock, category_id) VALUES (4, '2026-01-01 00:00:00', 'Angular development handbook', 'https://via.placeholder.com/150', '2026-01-01 00:00:00', 'Angular Masterclass', 59.99, 60, 2);
INSERT INTO public.product (id, date_created, description, image_url, last_updated, name, unit_price, units_in_stock, category_id) VALUES (5, '2026-01-01 00:00:00', 'Comfortable cotton t-shirt', 'https://via.placeholder.com/150', '2026-01-01 00:00:00', 'Classic T-Shirt', 29.99, 200, 3);
INSERT INTO public.product (id, date_created, description, image_url, last_updated, name, unit_price, units_in_stock, category_id) VALUES (6, '2026-01-01 00:00:00', 'Durable denim jeans', 'https://via.placeholder.com/150', '2026-01-01 00:00:00', 'Denim Jeans', 79.99, 150, 3);


--
-- Data for Name: product_category; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.product_category (id, category_name) VALUES (1, 'Electronics');
INSERT INTO public.product_category (id, category_name) VALUES (2, 'Books');
INSERT INTO public.product_category (id, category_name) VALUES (3, 'Clothing');


--
-- Name: address_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.address_id_seq', 36, true);


--
-- Name: city_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.city_id_seq', 6, true);


--
-- Name: db_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.db_user_id_seq', 104, true);


--
-- Name: order_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.order_item_id_seq', 37, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.orders_id_seq', 36, true);


--
-- Name: product_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.product_category_id_seq', 3, true);


--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.product_id_seq', 6, true);


--
-- Name: address address_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- Name: city city_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id);


--
-- Name: db_user db_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.db_user
    ADD CONSTRAINT db_user_pkey PRIMARY KEY (id);


--
-- Name: order_item order_item_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT order_item_pkey PRIMARY KEY (id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- Name: product_category product_category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_category
    ADD CONSTRAINT product_category_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: db_user uk_2yo5sed605l75swa973ub4kra; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.db_user
    ADD CONSTRAINT uk_2yo5sed605l75swa973ub4kra UNIQUE (username);


--
-- Name: db_user uk_6mh6gc6k1iu7htpabhhn6rcvr; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.db_user
    ADD CONSTRAINT uk_6mh6gc6k1iu7htpabhhn6rcvr UNIQUE (email);


--
-- Name: product fk5cypb0k23bovo3rn1a5jqs6j4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT fk5cypb0k23bovo3rn1a5jqs6j4 FOREIGN KEY (category_id) REFERENCES public.product_category(id);


--
-- Name: orders fkf5464gxwc32ongdvka2rtvw96; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkf5464gxwc32ongdvka2rtvw96 FOREIGN KEY (address_id) REFERENCES public.address(id);


--
-- Name: orders fksqvhoi7sogv0bsdps6j4xmret; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fksqvhoi7sogv0bsdps6j4xmret FOREIGN KEY (user_id) REFERENCES public.db_user(id);


--
-- Name: order_item fkt4dc2r9nbvbujrljv3e23iibt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fkt4dc2r9nbvbujrljv3e23iibt FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- PostgreSQL database dump complete
--

\unrestrict VH2P31Hhz8rgm1Tt7ga3p3fZUu2S7lGFxVm55j64JRkkv0MXUchznpmSc7yG71L

