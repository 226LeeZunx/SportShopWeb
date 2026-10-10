-- =============================================================================
-- SPORTSHOPWEB - SCRIPT DỮ LIỆU MẪU (SEED DATA)
-- Hệ cơ sở dữ liệu: PostgreSQL (Neon.tech)
-- Cách dùng: Mở Neon Console -> SQL Editor -> Dán toàn bộ script này -> Run
-- =============================================================================

-- 1. XÓA DỮ LIỆU CŨ (Nếu muốn nạp lại từ đầu)
-- Chạy theo thứ tự bảng con trước, bảng cha sau để không dính Foreign Key
TRUNCATE TABLE "ProductImages", "ProductVariants", "Products", "Categories", "Brands" RESTART IDENTITY CASCADE;

-- =============================================================================
-- 2. DANH MỤC THỂ THAO (CATEGORIES)
-- =============================================================================
INSERT INTO "Categories" ("name", "description") VALUES
('Running & Training', 'High performance running shoes, breathable shirts, and workout training gear.'),
('Football', 'Authentic football boots, club jerseys, and professional match equipment.'),
('Basketball', 'Signature basketball sneakers, jerseys, and streetball apparel.'),
('Gym & Fitness', 'Compression wear, gym tanks, weightlifting accessories, and fitness clothing.'),
('Tennis & Badminton', 'Court footwear, lightweight rackets, and court sport accessories.'),
('Sports Accessories', 'Sports backpacks, gym bags, water bottles, sweatbands, and socks.');

-- =============================================================================
-- 3. THƯƠNG HIỆU THỂ THAO (BRANDS)
-- =============================================================================
INSERT INTO "Brands" ("name", "logoUrl", "description") VALUES
('Nike', 'https://upload.wikimedia.org/wikipedia/commons/a/a6/Logo_NIKE.svg', 'Just Do It. World leading innovator in athletic footwear and apparel.'),
('Adidas', 'https://upload.wikimedia.org/wikipedia/commons/2/20/Adidas_Logo.svg', 'Impossible Is Nothing. Premium German sportswear manufacturer.'),
('Puma', 'https://upload.wikimedia.org/wikipedia/commons/8/88/Puma_logo.svg', 'Forever Faster. Leading global sports brand in soccer and running.'),
('Under Armour', 'https://upload.wikimedia.org/wikipedia/commons/4/44/Under_armour_logo.svg', 'High-performance athletic gear, compression apparel and footwear.'),
('Mizuno', 'https://upload.wikimedia.org/wikipedia/commons/a/a7/Mizuno_logo.svg', 'Japanese precision craftsmanship and sports technology.');

-- =============================================================================
-- 4. SẢN PHẨM (PRODUCTS)
-- (categoryId: 1-Running, 2-Football, 3-Basketball, 4-Gym, 5-Tennis, 6-Accessories)
-- (brandId: 1-Nike, 2-Adidas, 3-Puma, 4-Under Armour, 5-Mizuno)
-- =============================================================================
INSERT INTO "Products" ("productName", "description", "categoryId", "brandId") VALUES
-- 1
('Nike Air Zoom Pegasus 41', 'Responsive cushioning in the Pegasus delivers an energized ride for daily road running. Features dual Air Zoom units.', 1, 1),
-- 2
('Adidas Ultraboost Light', 'Experience epic energy return with the lightest Ultraboost ever made. BOOST midsole provides supreme comfort.', 1, 2),
-- 3
('Nike Mercurial Superfly 10 Elite', 'Engineered for speed. Built with a 3/4-length Air Zoom unit for explosive acceleration on firm ground pitches.', 2, 1),
-- 4
('Adidas Predator Elite FG', 'Precision meets power. Strikeskin rubber fins placed strategically for pinpoint control and curve shots.', 2, 2),
-- 5
('Puma MB.03 LaMelo Ball Toxic', 'Signature basketball shoe engineered for explosive court agility, featuring NITRO foam cushioning.', 3, 3),
-- 6
('Nike Dri-FIT DNA Basketball Jersey', 'Lightweight, sweat-wicking performance basketball jersey with authentic on-court mesh fabrication.', 3, 1),
-- 7
('Under Armour HeatGear Compression Top', 'Ultra-tight, second-skin fit for a locked-in feel. Ergonomic design keeps seams off high abrasion areas.', 4, 4),
-- 8
('Gymshark Sport Seamless T-Shirt', 'Engineered for tough gym workouts with anti-odor and high-stretch breathable fabric.', 4, 3),
-- 9
('Mizuno Wave Enforce Tour Tennis', 'Engineered for aggressive baseline and all-court players seeking stability and cushioning.', 5, 5),
-- 10
('Nike Utility Elite Training Backpack', 'Spacious training backpack with cushioned straps and dedicated compartments for shoes and gym gear.', 6, 1);

-- =============================================================================
-- 5. BIẾN THỂ SẢN PHẨM (PRODUCT VARIANTS: Size, Màu sắc, Giá, Trạng thái)
-- =============================================================================
INSERT INTO "ProductVariants" ("productId", "variantName", "size", "color", "price", "isActive") VALUES
-- Nike Pegasus 41 (ID: 1)
(1, 'Nike Pegasus 41 - Black/White 40', '40', 'Black/White', 3490000, true),
(1, 'Nike Pegasus 41 - Black/White 41', '41', 'Black/White', 3490000, true),
(1, 'Nike Pegasus 41 - Black/White 42', '42', 'Black/White', 3490000, true),
(1, 'Nike Pegasus 41 - Volt Lime 42', '42', 'Neon Lime', 3690000, true),

-- Adidas Ultraboost Light (ID: 2)
(2, 'Adidas Ultraboost Light - Core Black 41', '41', 'Core Black', 4200000, true),
(2, 'Adidas Ultraboost Light - Core Black 42', '42', 'Core Black', 4200000, true),
(2, 'Adidas Ultraboost Light - Cloud White 42', '42', 'Cloud White', 4200000, true),

-- Nike Mercurial Superfly 10 (ID: 3)
(3, 'Nike Mercurial Superfly 10 - Blue/Volt 40', '40', 'Blue/Volt', 6890000, true),
(3, 'Nike Mercurial Superfly 10 - Blue/Volt 41', '41', 'Blue/Volt', 6890000, true),
(3, 'Nike Mercurial Superfly 10 - Blue/Volt 42', '42', 'Blue/Volt', 6890000, true),

-- Adidas Predator Elite (ID: 4)
(4, 'Adidas Predator Elite - Solar Red 41', '41', 'Solar Red/Black', 6500000, true),
(4, 'Adidas Predator Elite - Solar Red 42', '42', 'Solar Red/Black', 6500000, true),

-- Puma MB.03 (ID: 5)
(5, 'Puma MB.03 - Toxic Purple 42', '42', 'Toxic Purple', 3800000, true),
(5, 'Puma MB.03 - Toxic Purple 43', '43', 'Toxic Purple', 3800000, true),

-- Nike Dri-FIT DNA Jersey (ID: 6)
(6, 'Nike Basketball Jersey - Black M', 'M', 'Black', 850000, true),
(6, 'Nike Basketball Jersey - Black L', 'L', 'Black', 850000, true),
(6, 'Nike Basketball Jersey - White L', 'L', 'White', 850000, true),

-- UA Compression Top (ID: 7)
(7, 'UA HeatGear Compression - Midnight Navy M', 'M', 'Midnight Navy', 950000, true),
(7, 'UA HeatGear Compression - Midnight Navy L', 'L', 'Midnight Navy', 950000, true),

-- Gym T-Shirt (ID: 8)
(8, 'Sport Seamless T-Shirt - Charcoal M', 'M', 'Charcoal', 650000, true),
(8, 'Sport Seamless T-Shirt - Charcoal L', 'L', 'Charcoal', 650000, true),

-- Mizuno Wave Enforce (ID: 9)
(9, 'Mizuno Wave Enforce - White/Blue 41', '41', 'White/Blue', 3100000, true),
(9, 'Mizuno Wave Enforce - White/Blue 42', '42', 'White/Blue', 3100000, true),

-- Nike Training Backpack (ID: 10)
(10, 'Nike Utility Elite Backpack - One Size Black', 'One Size', 'Black', 1950000, true);

-- =============================================================================
-- 6. HÌNH ẢNH SẢN PHẨM (PRODUCT IMAGES - LINK CDN CHẤT LƯỢNG CAO)
-- =============================================================================
INSERT INTO "ProductImages" ("productId", "imageUrl", "isPrimary", "sortOrder") VALUES
-- 1. Nike Pegasus 41
(1, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&auto=format&fit=crop&q=80', true, 1),
(1, 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=800&auto=format&fit=crop&q=80', false, 2),

-- 2. Adidas Ultraboost
(2, 'https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?w=800&auto=format&fit=crop&q=80', true, 1),
(2, 'https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?w=800&auto=format&fit=crop&q=80', false, 2),

-- 3. Nike Mercurial Football Boots
(3, 'https://images.unsplash.com/photo-1511556532299-8f662fc26c06?w=800&auto=format&fit=crop&q=80', true, 1),

-- 4. Adidas Predator FG
(4, 'https://images.unsplash.com/photo-1575361204480-aadea25e6e68?w=800&auto=format&fit=crop&q=80', true, 1),

-- 5. Puma MB.03 Basketball Shoes
(5, 'https://images.unsplash.com/photo-1552346154-21d32810aba3?w=800&auto=format&fit=crop&q=80', true, 1),

-- 6. Nike Basketball Jersey
(6, 'https://images.unsplash.com/photo-1519766304817-4f37bda74a29?w=800&auto=format&fit=crop&q=80', true, 1),

-- 7. Under Armour Compression Top
(7, 'https://images.unsplash.com/photo-1581655353564-df123a1eb820?w=800&auto=format&fit=crop&q=80', true, 1),

-- 8. Gym T-Shirt
(8, 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=800&auto=format&fit=crop&q=80', true, 1),

-- 9. Tennis Shoes
(9, 'https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=800&auto=format&fit=crop&q=80', true, 1),

-- 10. Training Backpack
(10, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=800&auto=format&fit=crop&q=80', true, 1);

-- =============================================================================
-- 7. TÀI KHOẢN MẪU TEST ĐĂNG NHẬP
-- =============================================================================
-- Roles
INSERT INTO "Roles" ("name", "description", "role_permission") VALUES
('ADMIN', 'System Administrator with full access', 'ALL_PERMISSIONS'),
('CUSTOMER', 'Standard Registered Customer', 'READ_WRITE_OWN');

-- Membership Tiers
INSERT INTO "MembershipTier" ("tier_name", "discount_percentage", "min_spend", "description") VALUES
('Bronze', 0, 0, 'New customer entry level tier'),
('Silver', 5, 2000000, '5% discount on all purchases'),
('Gold', 10, 5000000, '10% discount on all purchases'),
('Platinum', 15, 10000000, '15% discount and VIP perks');

-- Users (Mật khẩu mẫu: 123456)
INSERT INTO "Users" ("phone", "email", "password_hash", "status", "created_at") VALUES
('0901234567', 'admin@sportshop.com', 'admin123', 'ACTIVE', CURRENT_TIMESTAMP),
('0987654321', 'tuan@gmail.com', '123456', 'ACTIVE', CURRENT_TIMESTAMP),
('0912345678', 'customer@sportshop.com', '123456', 'ACTIVE', CURRENT_TIMESTAMP);

-- Customers
INSERT INTO "Customers" ("user_id", "tier_id", "first_name", "last_name", "gender") VALUES
(2, 1, 'Tuan', 'Huynh', 'Male'),
(3, 2, 'David', 'Beckham', 'Male');
