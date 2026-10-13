# KẾ HOẠCH PHÂN CÔNG & QUY TRÌNH DỰ ÁN — SPORTSHOPWEB

> **Dự án:** SportShopWeb — Website thương mại điện tử đồ thể thao đa năng (Multi-Sports)  
> **Công nghệ:** Java 17 · Servlet / JSP · Maven WAR · JPA (EclipseLink) · PostgreSQL (Neon.tech Cloud) · HTML5 / CSS3 / JS  
> **Kiến trúc:** MVC phân tầng — Entity → DAO → Service → Controller (Servlet) → View (JSP)  
> **Ngày cập nhật:** 09/10/2026

---

## 1. BẢNG PHÂN CÔNG THÀNH VIÊN

| Thành viên | Git Branch | Module phụ trách | Entity liên quan |
|:---|:---|:---|:---|
| **Tuấn** | `feature/account` | Account & Customer | `User`, `Customer`, `Admin`, `Role`, `MembershipTier`, `Address` |
| **Dũng** | `feature/catalog` | Product & Catalog | `Product`, `ProductVariant`, `ProductImage`, `Category`, `Brand`, `Review` |
| **Duy** | `feature/order-cart` | Cart & Order | `Cart`, `CartItem`, `Order`, `OrderItem`, `Voucher` |
| **Vũ** | `feature/payment-admin` | Payment & Admin | `Payment`, `PaymentMethod` + toàn bộ màn hình Admin |

---

## 2. CHI TIẾT CÔNG VIỆC & CHỨC NĂNG TỪNG MODULE

---

### 👤 TUẤN — feature/account — Account & Customer

#### Chức năng phụ trách:

| Chức năng | Mô tả |
|:---|:---|
| **Đăng ký tài khoản** | Khách hàng nhập họ tên, email, số điện thoại, mật khẩu. Hệ thống kiểm tra email/username chưa tồn tại, mã hóa mật khẩu rồi tạo bản ghi User + Customer. |
| **Đăng nhập** | Xác thực email/username và mật khẩu. Nếu đúng, lưu đối tượng Customer/User vào HttpSession. Nếu sai hiển thị thông báo lỗi tại form. |
| **Đăng xuất** | Huỷ session (session.invalidate()), chuyển hướng về trang chủ. |
| **Hồ sơ cá nhân** | Khách hàng đã đăng nhập xem và sửa: Họ tên, số điện thoại, ngày sinh, hạng thành viên (MembershipTier). |
| **Quản lý địa chỉ** | Khách hàng thêm, sửa, xóa địa chỉ giao hàng (Address). Chọn địa chỉ mặc định. |
| **Lịch sử đơn hàng (View)** | Khách hàng xem danh sách đơn hàng của mình (phối hợp dữ liệu từ module feature/order-cart). |

#### File cần tạo:

**Giao diện (View):**
- [x] `src/main/webapp/views/login.jsp`
- [x] `src/main/webapp/views/register.jsp`
- [x] `src/main/webapp/views/profile.jsp`
- [x] `src/main/webapp/assets/css/account.css`

**Backend (Java):**
- [ ] `src/main/java/.../dao/UserDAO.java`
- [ ] `src/main/java/.../dao/CustomerDAO.java`
- [ ] `src/main/java/.../dao/AddressDAO.java`
- [ ] `src/main/java/.../service/UserService.java`
- [ ] `src/main/java/.../service/CustomerService.java`
- [ ] `src/main/java/.../controller/LoginServlet.java`
- [ ] `src/main/java/.../controller/RegisterServlet.java`
- [ ] `src/main/java/.../controller/LogoutServlet.java`
- [ ] `src/main/java/.../controller/ProfileServlet.java`

**Session sau khi đăng nhập:**
```java
// LoginServlet lưu vào session để các trang khác kiểm tra
session.setAttribute("customer", customer);
session.setAttribute("user", user);
```

---

### 👤 DŨNG — feature/catalog — Product & Catalog

#### Chức năng phụ trách:

| Chức năng | Mô tả |
|:---|:---|
| **Trang chủ** | Hiển thị Hero Banner thể thao đa năng, các danh mục nổi bật (Running, Football, Basketball, Gym...), khối sản phẩm mới nhất và bán chạy lấy từ database. |
| **Danh sách sản phẩm** | Hiển thị toàn bộ sản phẩm dạng lưới (Grid). Hỗ trợ lọc theo Category (môn thể thao), lọc theo Brand (Nike, Adidas...), tìm kiếm theo tên. Có phân trang. |
| **Chi tiết sản phẩm** | Hiển thị ảnh lớn + thumbnail từ ProductImage. Hiển thị tên, mô tả, giá từ ProductVariant. Bộ chọn Size và Màu sắc. Nút Add to Cart. Khu vực đánh giá Review. |
| **Lọc & Tìm kiếm** | Tìm kiếm theo từ khóa. Lọc theo danh mục. Lọc theo hãng. Sắp xếp theo giá tăng/giảm, mới nhất. |

#### File cần tạo:

**Giao diện (View):**
- [ ] `src/main/webapp/views/home.jsp` (Hoàn thiện Hero + danh mục + sản phẩm)
- [ ] `src/main/webapp/views/products.jsp`
- [ ] `src/main/webapp/views/product-detail.jsp`
- [ ] `src/main/webapp/assets/css/home.css`
- [ ] `src/main/webapp/assets/css/product.css`

**Backend (Java):**
- [ ] `src/main/java/.../dao/ProductDAO.java`
- [ ] `src/main/java/.../dao/CategoryDAO.java`
- [ ] `src/main/java/.../dao/BrandDAO.java`
- [ ] `src/main/java/.../dao/ReviewDAO.java`
- [ ] `src/main/java/.../service/ProductService.java`
- [ ] `src/main/java/.../controller/HomeServlet.java` (Hiện đang có, cần mở rộng)
- [ ] `src/main/java/.../controller/ProductListServlet.java`
- [ ] `src/main/java/.../controller/ProductDetailServlet.java`

**Request attributes truyền sang JSP:**
```java
// HomeServlet
request.setAttribute("newProducts", productService.getNewProducts(8));
request.setAttribute("categories", categoryDAO.getAll());

// ProductListServlet
request.setAttribute("productList", products);
request.setAttribute("totalPages", totalPages);

// ProductDetailServlet
request.setAttribute("product", product);
request.setAttribute("variants", variants);
request.setAttribute("images", images);
```

---

### 👤 DUY — feature/order-cart — Cart & Order

#### Chức năng phụ trách:

| Chức năng | Mô tả |
|:---|:---|
| **Thêm vào giỏ hàng** | Nhận variantId và quantity từ trang chi tiết sản phẩm, thêm vào đối tượng Cart trong HttpSession. Nếu sản phẩm đã có trong giỏ thì cộng thêm số lượng. |
| **Xem giỏ hàng** | Hiển thị danh sách sản phẩm trong giỏ: ảnh, tên, size, màu, đơn giá, số lượng, tổng từng dòng. Hiển thị tổng tiền toàn giỏ. |
| **Cập nhật giỏ hàng** | Nút (+) và (-) để tăng/giảm số lượng từng sản phẩm. Nút xóa sản phẩm khỏi giỏ. |
| **Áp mã Voucher** | Khách hàng nhập mã voucher, hệ thống kiểm tra hợp lệ (còn hạn, chưa dùng, đủ điều kiện) và tính giảm giá. |
| **Thanh toán (Checkout)** | Khách hàng chọn/nhập địa chỉ giao hàng. Xem lại tóm tắt đơn hàng. Chọn phương thức (COD hoặc chuyển khoản). Xác nhận đặt hàng → Tạo bản ghi Order + OrderItem + Payment. |
| **Lịch sử đơn hàng** | Xem danh sách đơn hàng đã đặt, trạng thái từng đơn (Pending, Shipping, Completed, Cancelled). |

#### File cần tạo:

**Giao diện (View):**
- [ ] `src/main/webapp/views/cart.jsp`
- [ ] `src/main/webapp/views/checkout.jsp`
- [ ] `src/main/webapp/views/orders.jsp`
- [ ] `src/main/webapp/views/order-detail.jsp`
- [ ] `src/main/webapp/assets/css/cart.css`

**Backend (Java):**
- [ ] `src/main/java/.../dao/OrderDAO.java`
- [ ] `src/main/java/.../dao/OrderItemDAO.java`
- [ ] `src/main/java/.../dao/VoucherDAO.java`
- [ ] `src/main/java/.../service/CartService.java`
- [ ] `src/main/java/.../service/OrderService.java`
- [ ] `src/main/java/.../service/VoucherService.java`
- [ ] `src/main/java/.../controller/CartServlet.java`
- [ ] `src/main/java/.../controller/CheckoutServlet.java`
- [ ] `src/main/java/.../controller/OrderHistoryServlet.java`

**Cách CartServlet hoạt động:**
```java
// Nhận action từ request
String action = request.getParameter("action");
// "add"    → Thêm sản phẩm vào giỏ trong session
// "update" → Cập nhật số lượng
// "remove" → Xóa sản phẩm khỏi giỏ
```

---

### 👤 VŨ — feature/payment-admin — Payment & Admin

#### Chức năng phụ trách:

| Chức năng | Mô tả |
|:---|:---|
| **Xử lý thanh toán** | Ghi nhận bản ghi Payment khi khách đặt hàng (COD hoặc chuyển khoản). Cập nhật trạng thái thanh toán. |
| **Dashboard Admin** | Thống kê tổng quan: Doanh thu hôm nay/tháng này, số đơn hàng mới, số khách hàng mới, sản phẩm sắp hết hàng. |
| **Quản lý sản phẩm** | Admin xem danh sách sản phẩm. Thêm mới. Sửa thông tin. Thêm/sửa biến thể (ProductVariant: size, màu, giá). Xóa (soft delete). |
| **Quản lý danh mục & hãng** | CRUD danh mục (Category) và thương hiệu (Brand). |
| **Quản lý đơn hàng** | Xem danh sách toàn bộ đơn hàng. Lọc theo trạng thái. Cập nhật trạng thái đơn hàng (Pending → Shipping → Completed). Xem chi tiết từng đơn. |
| **Quản lý khách hàng** | Xem danh sách khách hàng. Xem thông tin và lịch sử mua hàng. Khoá/mở khoá tài khoản. |
| **Bảo mật Admin** | Tất cả trang /admin/* đều yêu cầu Role = ADMIN. Tạo AdminFilter để tự động chặn và chuyển hướng về trang login nếu chưa đủ quyền. |

#### File cần tạo:

**Giao diện (View):**
- [ ] `src/main/webapp/views/admin/dashboard.jsp`
- [ ] `src/main/webapp/views/admin/products.jsp`
- [ ] `src/main/webapp/views/admin/product-form.jsp` (Form thêm/sửa sản phẩm)
- [ ] `src/main/webapp/views/admin/orders.jsp`
- [ ] `src/main/webapp/views/admin/order-detail.jsp`
- [ ] `src/main/webapp/views/admin/customers.jsp`
- [ ] `src/main/webapp/assets/css/admin.css`

**Backend (Java):**
- [ ] `src/main/java/.../dao/PaymentDAO.java`
- [ ] `src/main/java/.../service/PaymentService.java`
- [ ] `src/main/java/.../controller/AdminDashboardServlet.java`
- [ ] `src/main/java/.../controller/AdminProductServlet.java`
- [ ] `src/main/java/.../controller/AdminOrderServlet.java`
- [ ] `src/main/java/.../controller/AdminCustomerServlet.java`
- [ ] `src/main/java/.../controller/AdminFilter.java` (Bộ lọc bảo mật quyền admin)

---

## 3. CƠ SỞ DỮ LIỆU & DỮ LIỆU MẪU (DATABASE SEED DATA)

Dự án dùng chung Cloud PostgreSQL tại **Neon.tech** thông qua cấu hình `SportShopWebPU` trong `persistence.xml`.

### Danh mục & Thương hiệu thống nhất:
* **Category:**
  1. `Running & Training` (Giày chạy, quần áo tập)
  2. `Football` (Áo đấu CLB, giày đinh, bóng đá)
  3. `Basketball` (Giày bóng rổ, áo bóng rổ)
  4. `Gym & Fitness` (Đồ tập thể hình, phụ kiện gym)
  5. `Tennis & Badminton` (Vợt, giày chuyên dụng)
  6. `Sports Accessories` (Balo thể thao, tất, bình nước)
* **Brand:** `Nike`, `Adidas`, `Puma`, `Under Armour`, `Mizuno`

### Tài khoản test mẫu:
* **Admin:** `admin@sportshop.com` / Mật khẩu: `admin123` (Role = `ADMIN`)
* **Customer:** `customer@sportshop.com` / Mật khẩu: `123456` (Role = `CUSTOMER`)

---

## 4. CẤU TRÚC GIT FLOW

```
main                          <- Nhánh sản phẩm cuối — nộp đồ án
  └── develop                 <- Nhánh tích hợp — merge sau khi test
        ├── feature/account        (Tuấn)
        ├── feature/catalog        (Dũng)
        ├── feature/order-cart     (Duy)
        └── feature/payment-admin  (Vũ)
```

### Quy tắc bắt buộc:
- KHÔNG BAO GIỜ commit thẳng lên `main` hoặc `develop`.
- Mỗi người chỉ làm việc trên nhánh `feature/...` của mình.
- Khi xong một tính năng hoàn chỉnh → Tạo Pull Request (PR) vào `develop` → Nhóm review → Merge.

### Lệnh Git hàng ngày:
```bash
# Bắt đầu buổi code: Lấy code mới nhất về
git checkout feature/<ten_nhanh_cua_ban>
git pull origin develop

# Kết thúc buổi code: Lưu và đẩy lên
git add .
git commit -m "feat: mô tả ngắn gọn tính năng vừa làm"
git push origin feature/<ten_nhanh_cua_ban>
```

### Quy ước commit message:
| Tiền tố | Ý nghĩa | Ví dụ |
|:---|:---|:---|
| `feat:` | Thêm tính năng mới | `feat: add LoginServlet and session management` |
| `fix:` | Sửa lỗi | `fix: correct redirect path after logout` |
| `style:` | Chỉ sửa CSS/UI | `style: update login page dark theme` |
| `refactor:` | Tối ưu code, không đổi logic | `refactor: extract CartService from CartServlet` |
| `docs:` | Cập nhật tài liệu | `docs: update KE_HOACH_PHAN_CONG.md` |

---

## 5. CẤU TRÚC THƯ MỤC JAVA

```
src/main/java/com/mycompany/sportshopweb/
├── model/          [DONE] 22 Entity có sẵn
├── dao/            [TODO] Viết DAO cho từng thành viên
├── service/        [TODO] Viết Service logic
├── controller/     [TODO] Viết Servlet (đang có HomeServlet)
└── util/
    ├── DBUtil.java         [OK] EntityManagerFactory
    └── ProductDB.java      [OK] Đang dùng tạm
```

**Cấu trúc tầng phân lớp:**
```
JSP (View)
  ^ forward / redirect
Controller (Servlet)
  ^ gọi xuống
Service (Business Logic)
  ^ gọi xuống
DAO (Database Access via JPA)
  ^ dùng
Entity / Model
  ^ ánh xạ
PostgreSQL Database
```

---

## 6. QUY ƯỚC CODE (CODING CONVENTIONS)

| Loại | Quy tắc | Ví dụ |
|:---|:---|:---|
| Class / Interface | PascalCase | `ProductVariant`, `CustomerDAO`, `LoginServlet` |
| Variable / Field | camelCase | `productId`, `totalPrice`, `customerName` |
| Method | camelCase | `getAllProducts()`, `calculateTotal()` |
| ID chính | {object}Id | `userId`, `productId`, `orderId` |
| Package | chữ thường | `model`, `dao`, `service`, `controller` |
| File JSP | kebab-case | `login.jsp`, `product-detail.jsp` |
| CSS class | kebab-case | `.header-nav`, `.product-card`, `.cart-badge` |

---

## 7. CẤU TRÚC GIAO DIỆN (UI/UX)

**Phong cách thiết kế:** Dark Luxury Sports — Tông đen than (#0a0a0a), điểm nhấn vàng chanh neon (#ccff00).

**Layout dùng chung (ĐÃ HOÀN THIỆN):**
- [x] `assets/css/common.css` — Biến màu, reset CSS, Header, Dropdown menu, Footer
- [x] `views/common/header.jsp` — Logo, Menu (Home, Categories dropdown, Brands dropdown, Hot Products, Sale), icon Search/Account/Cart
- [x] `views/common/footer.jsp` — 4 cột thông tin thể thao đa năng, form newsletter, bản quyền

**Mọi trang JSP đều nhúng Header/Footer theo chuẩn:**
```jsp
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
</head>
<body>
    <jsp:include page="/views/common/header.jsp" />
    <main>
        <!-- Nội dung riêng của từng trang -->
    </main>
    <jsp:include page="/views/common/footer.jsp" />
</body>
```

---

## 8. CHECKLIST TO-DO THEO GIAI ĐOẠN

### Giai đoạn 1 — Đồng bộ Layout & Data Mẫu (Đang thực hiện)
- [x] Tạo `common.css`, `header.jsp`, `footer.jsp`
- [x] Menu Header đa năng có hiệu ứng rê chuột xổ xuống (Dropdown)
- [x] Giao diện 100% tiếng Anh, chú thích code tiếng Việt
- [x] Tạo file `data_seed.sql` và nạp data mẫu vào Cloud NeonDB
- [x] **Tuấn:** Push layout chung, tạo PR gộp vào `develop`
- [x] **Dũng / Duy / Vũ:** `git pull origin develop` để lấy layout về

---

### Giai đoạn 2 — Xây dựng UI & Servlet (Song song 4 người)

#### Tuấn — feature/account
- [x] Dựng `login.jsp` (Dark theme, form đăng nhập)
- [x] Dựng `register.jsp` (Form đăng ký tài khoản)
- [x] Dựng `profile.jsp` (Hồ sơ cá nhân, địa chỉ giao hàng)
- [x] Viết `UserDAO`, `CustomerDAO`, `AddressDAO`
- [x] Viết `UserService`, `CustomerService`
- [x] Viết `LoginServlet` — Xác thực và quản lý Session
- [x] Viết `RegisterServlet` — Tạo tài khoản mới
- [x] Viết `LogoutServlet` — Huỷ Session
- [x] Viết `ProfileServlet` — Xem và cập nhật hồ sơ

#### Dũng — feature/catalog
- [x] Hoàn thiện `home.jsp` (Hero banner thể thao + danh mục + sản phẩm hot)
- [x] Dựng `products.jsp` (Grid sản phẩm + bộ lọc + tìm kiếm)
- [x] Dựng `product-detail.jsp` (Chi tiết + chọn size/màu + Add to Cart)
- [ ] Viết `ProductDAO`, `CategoryDAO`, `BrandDAO`, `ReviewDAO`
- [ ] Viết `ProductService`
- [x] Mở rộng `HomeServlet` (Đổ dữ liệu thật lên trang chủ)
- [x] Viết `ProductListServlet` (Tìm kiếm, lọc, phân trang)
- [x] Viết `ProductDetailServlet`

#### Duy — feature/order-cart
- [ ] Dựng `cart.jsp` (Bảng giỏ hàng, tăng/giảm, xóa, tổng tiền)
- [ ] Dựng `checkout.jsp` (Địa chỉ, tóm tắt, Voucher, thanh toán)
- [ ] Dựng `orders.jsp` (Lịch sử đơn hàng)
- [ ] Xây dựng cấu trúc `Cart` trong Session
- [ ] Viết `OrderDAO`, `OrderItemDAO`, `VoucherDAO`
- [ ] Viết `CartService`, `OrderService`, `VoucherService`
- [ ] Viết `CartServlet` (action: add, update, remove)
- [ ] Viết `CheckoutServlet`
- [ ] Viết `OrderHistoryServlet`

#### Vũ — feature/payment-admin
- [ ] Dựng `admin/dashboard.jsp` (Thống kê tổng quan)
- [ ] Dựng `admin/products.jsp` (Bảng CRUD sản phẩm)
- [ ] Dựng `admin/orders.jsp` (Duyệt và cập nhật đơn hàng)
- [ ] Dựng `admin/customers.jsp` (Danh sách khách hàng)
- [ ] Viết `PaymentDAO`, `PaymentService`
- [ ] Viết `AdminDashboardServlet`, `AdminProductServlet`, `AdminOrderServlet`
- [ ] Viết `AdminFilter` — Bảo vệ tất cả trang `/admin/*`

---

### Giai đoạn 3 — Tích hợp CSDL thật & Test Luồng

- [ ] Nối `CustomerDAO` và `UserDAO` vào PostgreSQL thật → Test đăng nhập
- [ ] Nối `ProductDAO` → Test hiển thị sản phẩm thật từ database
- [ ] Test luồng khép kín: Vào web → Xem sản phẩm → Thêm giỏ → Đăng nhập → Đặt hàng → Admin xem đơn
- [ ] Kiểm tra bảo mật Session (trang cần đăng nhập, trang Admin)
- [ ] Merge `develop` → `main`, chuẩn bị báo cáo và demo

---

*Tài liệu được duy trì bởi nhóm SportShopWeb. Mỗi khi hoàn thành một checkbox, hãy cập nhật trực tiếp vào file này và commit lên nhánh develop.*
