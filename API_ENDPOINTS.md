# API endpoints (base URL: http://localhost:8080)

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/products` | Create product |
| GET | `/api/products` | List products |
| GET | `/api/products/{id}` | Get product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |
| GET | `/api/products/category/{category}` | Filter by category |
| GET | `/api/products/low-stock` | Find products at/below reorder level |
| POST | `/api/products/{id}/stock-in` | Increase stock |
| POST | `/api/products/{id}/stock-out` | Decrease stock |

Product JSON: `{"name":"Wireless Mouse","sku":"WM-100","category":"Accessories","price":599.00,"quantity":25,"reorderLevel":5}`
Stock JSON: `{"quantity":3}`
