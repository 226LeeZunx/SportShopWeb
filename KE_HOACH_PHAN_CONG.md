# KẾ HOẠCH PHÂN CÔNG & QUY TRÌNH DỰ ÁN SPORTSHOPWEB

> **Dự án:** SportShopWeb — Website thương mại điện tử đồ thể thao & bóng đá  
> **Công nghệ:** Java 17, Servlet/JSP, Maven WAR, JPA / PostgreSQL, HTML5/CSS3/JS  
> **Kiến trúc:** MVC phân tầng (Entity → DAO → Service → Servlet / Controller → JSP / View)  
> **Ngày cập nhật:** 09/10/2026

---

## 👥 1. BẢNG PHÂN CÔNG THÀNH VIÊN THEO MODULE

| Thành viên | Git Branch | Module phụ trách | Các Model / Entity liên quan |
| :--- | :--- | :--- | :--- |
| **Tuấn** *(Leader)* | `feature/account` | **Tài khoản & Khách hàng (Account)** | `User`, `Customer`, `Admin`, `Role`, `MembershipTier`, `Address`, `Notification` |
| **Dũng** | `feature/catalog` | **Sản phẩm & Danh mục (Catalog)** | `Product`, `ProductVariant`, `ProductImage`, `Category`, `Brand`, `Review` |
| **Duy** | `feature/order-cart` | **Giỏ hàng & Đơn hàng (Cart & Order)** | `Cart`, `CartItem`, `Order`, `OrderItem`, `Voucher` |
| **Vũ** | `feature/payment-admin` | **Thanh toán & Quản trị (Payment & Admin)** | `Payment`, `PaymentMethod`, Các chức năng quản trị Admin |

---

## 🎯 2. CHI TIẾT CÔNG VIỆC TỪNG THÀNH VIÊN (GIAI ĐOẠN UI + MVC)

### 👤 1. TUẤN — Nhánh `feature/account`
* **Nhiệm vụ:** Quản lý toàn bộ luồng người dùng, đăng ký, đăng nhập và thông tin khách hàng.
* **Giao diện (JSP/CSS):**
  * [ ] `src/main/webapp/views/login.jsp`: Giao diện Đăng nhập (Form nhập email/username, mật khẩu, link quên mật khẩu/đăng ký).
  * [ ] `src/main/webapp/views/register.jsp`: Giao diện Đăng ký (Họ tên, email, SĐT, mật khẩu, xác nhận mật khẩu).
  * [ ] `src/main/webapp/views/profile.jsp`: Trang Hồ sơ cá nhân (Xem/sửa thông tin, hạng thành viên `MembershipTier`, sổ địa chỉ `Address`).
  * [ ] `src/main/webapp/assets/css/account.css`: CSS riêng cho module tài khoản.
* **Backend (Java):**
  * [ ] `UserDAO.java`, `CustomerDAO.java`, `AddressDAO.java` (Truy vấn CSDL qua JPA/EntityManager).
  * [ ] `UserService.java`, `CustomerService.java` (Logic mã hóa mật khẩu, kiểm tra trùng lặp email/username).
  * [ ] `LoginServlet.java`: Tiếp nhận đăng nhập, lưu thông tin `Customer` / `User` vào `HttpSession`.
  * [ ] `RegisterServlet.java`: Tiếp nhận form đăng ký và tạo mới khách hàng.
  * [ ] `LogoutServlet.java`: Hủy session và chuyển hướng về trang chủ.
  * [ ] `ProfileServlet.java`: Lấy thông tin user hiện tại từ session để hiển thị và cập nhật.

---

### 👤 2. DŨNG — Nhánh `feature/catalog`
* **Nhiệm vụ:** Trải nghiệm duyệt sản phẩm, trang chủ, bộ lọc và chi tiết sản phẩm.
* **Giao diện (JSP/CSS):**
  * [ ] `src/main/webapp/views/home.jsp`: Hoàn thiện trang chủ (Hero Banner bóng đá, danh mục nổi bật, sản phẩm mới nhất).
  * [ ] `src/main/webapp/views/products.jsp`: Trang danh sách sản phẩm dạng Grid, thanh bên lọc theo Category/Brand/Price, phân trang.
  * [ ] `src/main/webapp/views/product-detail.jsp`: Trang chi tiết sản phẩm (Bộ chọn ảnh thumbnail, chọn Size/Màu từ `ProductVariant`, nút Add to Cart, đánh giá `Review`).
  * [ ] `src/main/webapp/assets/css/home.css`, `product.css`.
* **Backend (Java):**
  * [ ] `ProductDAO.java`, `CategoryDAO.java`, `BrandDAO.java`, `ReviewDAO.java`.
  * [ ] `ProductService.java` (Lấy sản phẩm theo danh mục, tìm kiếm theo từ khóa, lọc theo khoảng giá, lấy chi tiết kèm biến thể).
  * [ ] `HomeServlet.java`: Đổ dữ liệu sản phẩm mới / bán chạy lên `home.jsp`.
  * [ ] `ProductListServlet.java`: Xử lý tìm kiếm, lọc và phân trang.
  * [ ] `ProductDetailServlet.java`: Xử lý hiển thị chi tiết 1 sản phẩm.

---

### 👤 3. DUY — Nhánh `feature/order-cart`
* **Nhiệm vụ:** Luồng mua sắm, giỏ hàng, áp mã giảm giá và đặt hàng.
* **Giao diện (JSP/CSS):**
  * [ ] `src/main/webapp/views/cart.jsp`: Bảng giỏ hàng, nút tăng/giảm số lượng (+/-), nút xóa sản phẩm, tính tổng tiền tạm tính.
  * [ ] `src/main/webapp/views/checkout.jsp`: Form chọn/nhập địa chỉ giao hàng, tóm tắt đơn hàng, ô nhập `Voucher` giảm giá, chọn phương thức thanh toán.
  * [ ] `src/main/webapp/views/orders.jsp`: Lịch sử các đơn hàng đã đặt của khách hàng.
  * [ ] `src/main/webapp/assets/css/cart.css`.
* **Backend (Java):**
  * [ ] Xây dựng đối tượng giỏ hàng trong Session (`Cart`, `CartItem`) theo chuẩn Chapter 7.
  * [ ] `OrderDAO.java`, `OrderItemDAO.java`, `VoucherDAO.java`.
  * [ ] `OrderService.java` (Tính tổng tiền, kiểm tra voucher, chuyển giỏ hàng thành bản ghi `Order` & `OrderItem` trong CSDL).
  * [ ] `CartServlet.java`: Xử lý thêm vào giỏ (`action=add`), sửa số lượng (`action=update`), xóa (`action=delete`).
  * [ ] `CheckoutServlet.java`: Tiếp nhận đặt hàng và lưu xuống CSDL.
  * [ ] `OrderHistoryServlet.java`: Hiển thị lịch sử đơn hàng của user đang đăng nhập.

---

### 👤 4. VŨ — Nhánh `feature/payment-admin`
* **Nhiệm vụ:** Quản lý phương thức thanh toán và toàn bộ cổng quản trị dành cho Admin.
* **Giao diện (JSP/CSS):**
  * [ ] `src/main/webapp/views/admin/dashboard.jsp`: Thống kê tổng doanh thu, số đơn hàng mới, số lượng khách hàng.
  * [ ] `src/main/webapp/views/admin/products.jsp`: Bảng CRUD quản lý sản phẩm và biến thể.
  * [ ] `src/main/webapp/views/admin/orders.jsp`: Bảng quản lý đơn hàng và cập nhật trạng thái (Pending → Shipping → Completed).
  * [ ] `src/main/webapp/views/admin/customers.jsp`: Bảng xem danh sách khách hàng.
  * [ ] `src/main/webapp/assets/css/admin.css`.
* **Backend (Java):**
  * [ ] `PaymentDAO.java`, `PaymentService.java` (Xử lý bản ghi `Payment`).
  * [ ] `AdminDashboardServlet.java`, `AdminProductServlet.java`, `AdminOrderServlet.java`.
  * [ ] `AdminFilter.java`: Bộ lọc bảo mật Servlet Filter chặn người dùng không có role Admin vào các trang `/admin/*`.

---

## 🌳 3. QUY TRÌNH PHỐI HỢP GIT (GIT FLOW)

```text
main (Nhánh production nộp đồ án)
  └── develop (Nhánh tích hợp chung của cả nhóm)
        ├── feature/account        (Tuấn)
        ├── feature/catalog        (Dũng)
        ├── feature/order-cart     (Duy)
        └── feature/payment-admin  (Vũ)
```

### 📌 Nguyên tắc cốt lõi:
1. **Tuyệt đối KHÔNG code trực tiếp** trên nhánh `main` hoặc `develop`.
2. Mỗi thành viên chỉ làm việc trên nhánh `feature/...` của mình.
3. Mỗi khi xong một chức năng:
   * Commit và Push lên GitHub trên nhánh của mình.
   * Tạo **Pull Request (PR)** vào nhánh `develop`.
   * Cả nhóm (hoặc Leader) kiểm tra, test không lỗi thì mới bấm **Merge**.

### 💻 Lệnh Git chuẩn hàng ngày cho từng bạn:
```bash
# 1. Chuyển sang nhánh của mình và lấy code mới nhất từ develop về
git checkout feature/<ten_nhanh>
git pull origin develop

# 2. Sau khi code xong tính năng:
git add .
git commit -m "feat: <mô tả ngắn gọn tính năng vừa làm>"
git push origin feature/<ten_nhanh>

# 3. Lên GitHub tạo Pull Request gộp vào nhánh develop
```

---

## 📝 4. QUY ƯỚC ĐẶT TÊN & CODE (CODING CONVENTIONS)

* **Class / Interface:** PascalCase → `ProductVariant.java`, `CustomerDAO.java`, `LoginServlet.java`
* **Variable / Field:** camelCase → `productId`, `customerName`, `totalPrice`
* **Method:** camelCase → `getAllProducts()`, `findCustomerByEmail()`
* **Khóa chính ID:** `{object}Id` → `userId`, `productId`, `orderId`
* **Package Java:** Chữ thường toàn bộ → `model`, `dao`, `service`, `controller`, `util`
* **File JSP:** Kebab-case hoặc chữ thường → `login.jsp`, `product-detail.jsp`, `order-history.jsp`
* **Commit Message:** Chuẩn Conventional Commits:
  * `feat: ...` (Tính năng mới)
  * `fix: ...` (Sửa lỗi)
  * `style: ...` (Chỉnh sửa UI/CSS)
  * `refactor: ...` (Tối ưu code không đổi logic)

---

## 📋 5. CHECKLIST TO-DO LIST (THEO TỪNG GIAI ĐOẠN)

### 🏁 Giai đoạn 1: Khởi động & Đồng bộ Layout (Tuần này - Ưu tiên #1)
- [x] Tạo layout chung: `common.css`, `header.jsp`, `footer.jsp`.
- [x] Chuyển đổi toàn bộ UI sang tiếng Anh, chú thích tiếng Việt.
- [ ] Tuấn push layout dùng chung lên nhánh `feature/account` và tạo PR gộp vào `develop`.
- [ ] Dũng, Duy, Vũ `git pull origin develop` về nhánh của mình để nhận layout chung.

### 🚀 Giai đoạn 2: Xây dựng UI & Tích hợp Servlet độc lập
- [ ] **Tuấn:** Dựng `login.jsp`, `register.jsp` → Viết `LoginServlet`, `RegisterServlet` → Xác thực Session.
- [ ] **Dũng:** Dựng `products.jsp`, `product-detail.jsp` → Viết `ProductListServlet`, `ProductDetailServlet`.
- [ ] **Duy:** Dựng `cart.jsp`, `checkout.jsp` → Viết `CartServlet` (Session Cart).
- [ ] **Vũ:** Dựng giao diện `admin/dashboard.jsp`, `admin/products.jsp` → Viết `AdminServlet`.

### 🔄 Giai đoạn 3: Tích hợp CSDL thật (JPA/PostgreSQL) & Khép kín luồng
- [ ] Nối `CustomerDAO` vào database thật để đăng nhập tài khoản thực.
- [ ] Nối `ProductDAO` để lấy danh sách sản phẩm và ảnh thật từ PostgreSQL.
- [ ] Kiểm thử luồng khép kín: **Vào web → Xem sản phẩm → Thêm vào giỏ → Đăng nhập → Đặt hàng → Admin thấy đơn hàng**.
- [ ] Merge toàn bộ `develop` vào `main` để chuẩn bị báo cáo.
