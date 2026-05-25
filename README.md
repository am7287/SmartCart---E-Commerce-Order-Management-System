# SmartCart - E-Commerce Order Management System

This is a mini project based on a simple e-commerce backend system. It is built using Spring Boot microservices.

The project has these services:

- Config Server
- Discovery Server
- API Gateway
- User Service
- Product Service
- Order Service

The main flow is:

```text
Client / Postman -> API Gateway -> Microservices -> MySQL Database
```

## Main Features

User features:

- Register user
- Login user
- View products
- Place order
- View order history

Admin features:

- Add product
- Update product
- Delete product
- View all orders

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- Maven
- Lombok
- Feign Client
- Resilience4j
- Spring Cloud Config
- Eureka Discovery Server
- Postman

## Database / SQL Script

The SQL script is available in:

```text
sql/schema.sql
```

It contains the database design for:

- users table
- products table
- orders table

In this project, separate databases are used for services:

```text
smartcart_users
smartcart_products
smartcart_orders
```

## Postman Collection

The Postman collection is available in:

```text
postman/SmartCart.postman_collection.json
```

Postman is used to test the backend APIs because this project is mainly backend based. It works like a client/frontend for testing.

For example:

```text
Postman -> API Gateway -> Product Service -> MySQL
```

The collection contains requests for:

- Authentication APIs
- Product APIs
- Order APIs

Use these requests in order:

1. Auth - Register
2. Auth - Login
3. Product - Add
4. Product - View All
5. Order - Place
6. Order - All
7. Order - User History

## How To Run

Start all services with:

```powershell
powershell -ExecutionPolicy Bypass -File .\start-smartcart.ps1
```

Stop all services with:

```powershell
powershell -ExecutionPolicy Bypass -File .\stop-smartcart.ps1
```

API Gateway runs on:

```text
http://localhost:8080
```

Eureka dashboard runs on:

```text
http://localhost:8761
```

## Submission Files

For submission:

- Source code is in this GitHub repository
- SQL script is in `sql/schema.sql`
- Postman collection is in `postman/SmartCart.postman_collection.json`
