# Tracking Orders

Hệ thống backend quản lý đơn hàng và theo dõi vận chuyển, xây dựng bằng Spring Boot 3. Cung cấp đầy đủ các API phục vụ luồng mua hàng từ khi khách đặt đơn đến khi giao hàng thành công hoặc hoàn trả — bao gồm quản lý sản phẩm, giỏ hàng, thanh toán, tracking log và báo cáo Excel.

---

## Tech Stack

| Thành phần | Công nghệ |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4.3 |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security (HTTP Basic Auth) |
| Mapping | MapStruct 1.5.5 |
| Boilerplate | Lombok |
| Export | EasyExcel 3.3.4 |
| Build tool | Maven |

---

## Cấu trúc dự án

```
src/main/java/com/example/trackingorders/
├── config/             # Security, Auditing config
├── common/             # Enum, BaseResponse wrapper
├── controller/         # REST Controllers
├── dto/
│   ├── request/        # Request DTOs
│   └── response/       # Response DTOs
├── entity/             # JPA Entities
├── exception/          # GlobalExceptionHandler, BusinessException
├── mapper/             # MapStruct mappers
├── repository/         # Spring Data repositories
├── service/
│   ├── implement/      # Service implementations
│   └── specfication/   # JPA Specification (dynamic filter)
└── util/               # Utility classes
```

---

## Cài đặt & Chạy

### Yêu cầu
- Java 21+
- MySQL 8+
- Maven 3.8+

### Các bước

**1. Clone repository**
```bash
git clone https://github.com/DinhMinhChien/Tracking-orders.git
cd Tracking-orders
```

**2. Tạo database**
```sql
CREATE DATABASE product_manager;
```

**3. Cấu hình kết nối**

Chỉnh sửa `src/main/resources/application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/product_manager
    username: your_username
    password: your_password
server:
  port: 8001
```

**4. Chạy ứng dụng**
```bash
./mvnw spring-boot:run
```

API sẽ chạy tại: `http://localhost:8001`

---

## Phân quyền

Hệ thống sử dụng HTTP Basic Authentication với 3 roles:

| Role | Mô tả |
|---|---|
| `CUSTOMER` | Khách hàng — đặt hàng, xem giỏ hàng, yêu cầu hoàn trả |
| `ADMIN` | Quản trị viên — xác nhận/từ chối đơn hàng, quản lý sản phẩm, duyệt hoàn trả |
| `SHIPPER` | Nhân viên giao hàng — cập nhật trạng thái lấy hàng, vận chuyển, giao thành công |

---

## API Endpoints

Base URL: `http://localhost:8001/api/v1`

### Authentication
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/auth/register` | Đăng ký tài khoản (public) |

### Products
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/products` | Tạo sản phẩm mới |
| GET | `/products` | Danh sách sản phẩm (filter: status, keyword, isFeature, phân trang) |
| GET | `/products/{id}` | Chi tiết sản phẩm |
| PUT | `/products/{id}` | Cập nhật sản phẩm |
| GET | `/products/statistics` | Thống kê sản phẩm cho dashboard |
| GET | `/products/exports` | Xuất báo cáo Excel |

### Carts
| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/carts` | Xem giỏ hàng của user hiện tại |
| PATCH | `/carts/items/{cartItemId}` | Cập nhật số lượng sản phẩm trong giỏ |
| DELETE | `/carts/items/{id}` | Xóa sản phẩm khỏi giỏ |

### Checkout
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/checkout/promotions-available` | Lấy danh sách mã giảm giá khả dụng theo tổng đơn |
| POST | `/checkout/summary` | Tính tổng đơn hàng (subtotal, discount, phí ship, tổng cộng) |

### Orders
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/orders` | Tạo đơn hàng |
| GET | `/orders` | Danh sách đơn hàng (filter theo status, phân trang) |
| GET | `/orders/{id}` | Chi tiết đơn hàng |
| GET | `/orders/statistics` | Thống kê đơn hàng cho dashboard |
| POST | `/orders/bulk-confirm` | Xác nhận hàng loạt nhiều đơn |
| PUT | `/orders/{id}/confirm` | Xác nhận đơn hàng |
| PUT | `/orders/{id}/reject` | Từ chối đơn hàng (kèm lý do) |
| PUT | `/orders/{id}/pick-up` | Xác nhận lấy hàng |
| PUT | `/orders/{id}/shipping` | Xác nhận đang vận chuyển |
| PUT | `/orders/{id}/delivery-success` | Xác nhận giao hàng thành công |

### Returns (Hoàn trả)
| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/returns` | Tạo yêu cầu hoàn trả (chỉ đơn đã DELIVERED) |
| GET | `/returns/filter` | Danh sách yêu cầu hoàn trả (filter theo status, phân trang) |
| GET | `/returns/{returnId}` | Chi tiết yêu cầu hoàn trả |
| GET | `/returns/summary` | Thống kê hoàn trả cho dashboard |
| GET | `/returns/export` | Xuất báo cáo Excel |
| POST | `/returns/admin/{returnId}/confirm` | Admin duyệt yêu cầu hoàn trả |
| POST | `/returns/admin/{returnId}/reject` | Admin từ chối yêu cầu hoàn trả |
| POST | `/returns/admin/{returnId}/warehouse-received` | Kho xác nhận đã nhận hàng hoàn |
| POST | `/returns/admin/{returnId}/restock` | Nhập lại hàng vào kho |
| POST | `/returns/admin/{returnId}/refund` | Hoàn tiền cho khách |
| POST | `/returns/admin/{returnId}/fail` | Đánh dấu hoàn trả thất bại |

### Tracking Logs
| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/tracking-logs/{orderId}` | Lịch sử trạng thái của đơn hàng |

### Users
| Method | Endpoint | Mô tả |
|---|---|---|
| GET | `/users/addresses` | Danh sách địa chỉ của user hiện tại |
| POST | `/users/addresses` | Thêm địa chỉ mới |

---

## Luồng trạng thái

### Đơn hàng (Order)

```
                     ┌──────────┐
                     │ PENDING  │ ← Khách đặt hàng
                     └────┬─────┘
              confirm ↓       ↓ reject
                 ┌─────────┐  ┌────────┐
                 │CONFIRMED│  │ FAILED │
                 └────┬────┘  └────────┘
              pick-up ↓       ↓ fail
                 ┌─────────┐
                 │ PICKING │
                 └────┬────┘
             shipping ↓       ↓ fail
                 ┌──────────┐
                 │ SHIPPING │
                 └────┬─────┘
     delivery-success ↓       ↓ fail
                 ┌───────────┐
                 │ DELIVERED │
                 └─────┬─────┘
              return ↓
                 ┌──────────┐
                 │RETURNING │
                 └──────────┘
```

### Hoàn trả (Return)

```
PENDING → IN_TRANSIT → WAREHOUSE_RECEIVED → RESTOCKED → REFUNDED
   ↓           ↓                ↓
REJECTED     FAILED           FAILED
```

---

## Tính năng nổi bật

- **Tracking log tự động** — mỗi lần thay đổi trạng thái đơn hàng hoặc hoàn trả đều tạo một `TrackingLog` ghi lại `from_status`, `to_status`, ghi chú và vị trí.

- **Optimistic locking** — `Inventory` và `Promotions` dùng `@Version` để tránh race condition khi nhiều người đặt hàng cùng lúc.

- **State machine có kiểm tra** — mọi chuyển đổi trạng thái đều được validate, ném `BusinessException` nếu không hợp lệ.

- **Rollback tự động** — khi đơn bị từ chối (`reject`), hệ thống tự động hoàn lại tồn kho và lượt sử dụng voucher.

- **Checkout linh hoạt** — tính phí ship cố định (30,000đ), áp dụng mã giảm giá theo `discountType` (percent/fixed), kiểm tra điều kiện `minOrderValue`.

- **Export Excel** — sản phẩm và đơn hoàn trả đều có endpoint export `.xlsx` dùng EasyExcel.

- **Auditing tự động** — `BaseEntity` ghi lại `createdAt`, `createdBy`, `updatedAt`, `updatedBy` qua Spring Data Auditing.

- **Phân trang & filter động** — dùng Spring Specification pattern cho Orders, Products, Returns.

- **i18n message** — response messages lấy từ `messages.properties` qua `MessageSource`.

---

## Database Schema (chính)

```
users ──── carts ──── cart_items ──── products ──── inventory
  │                                      │
  └── addresses                          │
  │                                      │
  └──────────── orders ─────────── order_items
                  │
                  ├── payment_methods
                  ├── promotions ──── user_promotions
                  ├── carriers ──── (users có role SHIPPER)
                  ├── tracking_logs
                  └── returns
```
