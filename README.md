# 🛒 CartZone — Shopping Cart REST API

A fully functional **Spring Boot + MySQL** backend for a customer shopping cart system.  
No Lombok. No external auth libraries. Clean, plain Java — works out of the box.

---

## 🧱 Tech Stack

| Layer       | Technology                          |
|-------------|-------------------------------------|
| Language    | Java 17                             |
| Framework   | Spring Boot 3.2.0                   |
| Database    | MySQL 8+                            |
| ORM         | Spring Data JPA / Hibernate 6       |
| Build Tool  | Maven                               |
| Server      | Embedded Apache Tomcat (port 8080)  |

---

## 📁 Project Structure

```
src/
└── main/
    ├── java/com/cart/app/
    │   ├── CartAppApplication.java        ← Entry point
    │   ├── config/
    │   │   ├── CorsConfig.java            ← CORS for frontend
    │   │   ├── DataSeeder.java            ← Auto-seeds 8 sample products
    │   │   └── GlobalExceptionHandler.java← Centralised error responses
    │   ├── controller/
    │   │   ├── UserController.java        ← /api/users
    │   │   ├── ProductController.java     ← /api/products
    │   │   └── CartController.java        ← /api/cart
    │   ├── dto/
    │   │   └── Dtos.java                  ← All request/response DTOs
    │   ├── model/
    │   │   ├── User.java
    │   │   ├── Product.java
    │   │   ├── Cart.java
    │   │   └── CartItem.java
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   ├── ProductRepository.java
    │   │   ├── CartRepository.java
    │   │   └── CartItemRepository.java
    │   └── service/
    │       ├── UserService.java
    │       ├── ProductService.java
    │       └── CartService.java
    └── resources/
        └── application.properties
```

---

## ⚙️ Setup & Run

### 1. Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.6+ (or use STS/IntelliJ built-in Maven)

### 2. Configure Database

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cartdb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

> The database `cartdb` is created automatically on first run.

### 3. Run the Application

**Via Maven CLI:**
```bash
mvn spring-boot:run
```

**Via STS:**
Right-click project → Run As → Spring Boot App

**Via IntelliJ:**
Run `CartAppApplication.java` main method

### 4. Verify

Visit: [http://localhost:8080/api/products](http://localhost:8080/api/products)

You should see 8 auto-seeded products in JSON format.

---

## 📡 API Reference

### Users — `/api/users`

| Method | Endpoint        | Description         | Body                              |
|--------|-----------------|---------------------|-----------------------------------|
| GET    | `/api/users`    | Get all users       | —                                 |
| GET    | `/api/users/{id}` | Get user by ID    | —                                 |
| POST   | `/api/users`    | Create new user     | `{"username":"x","email":"x@x.x"}`|
| PUT    | `/api/users/{id}` | Update user       | `{"username":"x","email":"x@x.x"}`|
| DELETE | `/api/users/{id}` | Delete user       | —                                 |

### Products — `/api/products`

| Method | Endpoint              | Description              | Body / Params              |
|--------|-----------------------|--------------------------|----------------------------|
| GET    | `/api/products`       | Get all products         | —                          |
| GET    | `/api/products?search=keyboard` | Search by name | `?search=term`            |
| GET    | `/api/products?category=Electronics` | Filter by category | `?category=name`  |
| GET    | `/api/products/{id}`  | Get product by ID        | —                          |
| POST   | `/api/products`       | Create product           | See body below             |
| PUT    | `/api/products/{id}`  | Update product           | See body below             |
| DELETE | `/api/products/{id}`  | Delete product           | —                          |

**Product body:**
```json
{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 799.99,
  "imageUrl": "https://example.com/mouse.jpg",
  "stock": 50,
  "category": "Electronics"
}
```

### Cart — `/api/cart`

| Method | Endpoint                          | Description              | Body / Params             |
|--------|-----------------------------------|--------------------------|---------------------------|
| GET    | `/api/cart/{userId}`              | Get user's cart          | —                         |
| POST   | `/api/cart/{userId}/add`          | Add item to cart         | `{"productId":1,"quantity":2}` |
| PUT    | `/api/cart/{userId}/item/{itemId}`| Update item quantity     | `?quantity=3`             |
| DELETE | `/api/cart/{userId}/item/{itemId}`| Remove item from cart    | —                         |
| DELETE | `/api/cart/{userId}/clear`        | Clear entire cart        | —                         |

### Sample Requests (curl)

```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","email":"john@example.com"}'

# Add product to cart (userId=1, productId=1)
curl -X POST http://localhost:8080/api/cart/1/add \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"quantity":2}'

# View cart
curl http://localhost:8080/api/cart/1
```

---

## 🗄️ Database Schema

Tables auto-created by Hibernate on startup:

```sql
users        (id, username UNIQUE, email)
products     (id, name, description, price, image_url, stock, category)
carts        (id, user_id FK→users)
cart_items   (id, cart_id FK→carts, product_id FK→products, quantity)
```

---

## 🌐 CORS

Configured to allow requests from:
- `http://localhost:5500` (VS Code Live Server)
- `http://127.0.0.1:5500`
- `http://localhost:3000` (React dev server)

To add more origins, edit `CorsConfig.java`.

---

## 🚀 Importing into STS (Spring Tool Suite)

1. `File` → `Import` → `Maven` → `Existing Maven Projects`
2. Browse to this project folder → Finish
3. Update `application.properties` with your MySQL password
4. Right-click project → `Run As` → `Spring Boot App`

---

## 📝 Notes

- **No Lombok** — all models and DTOs use plain Java getters/setters for maximum IDE compatibility
- **Auto-seeding** — 8 sample products are inserted on first run if the products table is empty
- **DDL auto** — set to `update`, so schema evolves without data loss on restart

---

## 📄 License

MIT — free to use, modify, and distribute.
