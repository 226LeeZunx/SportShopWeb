/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.util;

import com.mycompany.sportshopweb.model.Brand;
import com.mycompany.sportshopweb.model.Category;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductVariant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class ProductDB {
    public static final int PAGE_SIZE = 20;

    // đếm số sản phẩm
    public static long countProducts() {
        return countProducts("", 0);
    }

    // đếm sp sau khi dùng bộ lọc
    public static long countProducts(String keyword, int categoryId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE 1=1";

            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql += " AND LOWER(p.productName) LIKE :keyword";
            }
            if (categoryId > 0) {
                jpql += " AND p.category.categoryId = :categoryId";
            }

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("keyword", "%" + keyword.trim().toLowerCase() + "%");
            }

            if (categoryId > 0) {
                query.setParameter("categoryId", categoryId);
            }

            return query.getSingleResult();

        } finally {
            em.close();
        }
    }

    // Giữ tương thích với code đang sử dụng phương thức phân trang cũ.
    public static List<Product> getProductsByPage(int page, int pageSize) {
        return getProductsByPage(page, pageSize, "", 0);
    }

    // Lấy danh sách sản phẩm theo trang, từ khóa và danh mục.
    // Tải images, brand, category để JSP có thể hiển thị dữ liệu.
    public static List<Product> getProductsByPage(
            int page, int pageSize, String keyword, int categoryId) {

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            // Bảo vệ truy vấn khỏi page/pageSize không hợp lệ.
            page = Math.max(page, 1);
            pageSize = Math.max(pageSize, 1);

            String jpql = "SELECT p.productId FROM Product p WHERE 1 = 1";

            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql += " AND LOWER(p.productName) LIKE :keyword";
            }

            if (categoryId > 0) {
                jpql += " AND p.category.categoryId = :categoryId";
            }

            jpql += " ORDER BY p.productId DESC";

            TypedQuery<Integer> idQuery = em.createQuery(jpql, Integer.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                idQuery.setParameter(
                        "keyword", "%" + keyword.trim().toLowerCase() + "%");
            }

            if (categoryId > 0) {
                idQuery.setParameter("categoryId", categoryId);
            }

            List<Integer> ids = idQuery
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

            if (ids.isEmpty()) {
                return new ArrayList<>();
            }

            // Lấy đủ dữ liệu cần hiển thị cho từng sản phẩm.
            return em.createQuery(
                    "SELECT DISTINCT p FROM Product p "
                            + "LEFT JOIN FETCH p.images "
                            + "LEFT JOIN FETCH p.brand "
                            + "LEFT JOIN FETCH p.category "
                            + "WHERE p.productId IN :ids "
                            + "ORDER BY p.productId DESC",
                    Product.class)
                    .setParameter("ids", ids)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public void insert(Product product, int brandId, int categoryId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            product.setBrand(em.getReference(Brand.class, brandId));
            product.setCategory(em.getReference(Category.class, categoryId));
            em.persist(product);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        }
    }

    /**
     * Cập nhật thông tin cơ bản của sản phẩm.
     * 
     * @param productId   ID sản phẩm cần cập nhật.
     * @param productName Tên sản phẩm mới.
     * @param description Mô tả mới.
     * @param brandId     ID thương hiệu mới.
     * @param categoryId  ID danh mục mới.
     * @return true nếu cập nhật thành công, false nếu không tìm thấy sản phẩm.
     */
    public boolean updateProduct(
            int productId,
            String productName,
            String description,
            int brandId,
            int categoryId) {

        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Tìm sản phẩm cần cập nhật.
            Product product = em.find(Product.class, productId);

            if (product == null) {
                tx.rollback();
                return false;
            }

            // Cập nhật các thuộc tính cơ bản.
            product.setProductName(productName);
            product.setDescription(description);

            // Gán Brand và Category theo ID.
            product.setBrand(em.getReference(Brand.class, brandId));
            product.setCategory(em.getReference(Category.class, categoryId));

            // Product đang được quản lý bởi EntityManager,
            // nên JPA sẽ tự phát hiện thay đổi khi commit.
            tx.commit();

            return true;

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    /**
     * Ngừng bán sản phẩm bằng cách đặt số lượng của tất cả
     * ProductVariant thuộc sản phẩm về 0.
     *
     * Không xóa Product, ProductVariant hoặc lịch sử đơn hàng.
     *
     * @param productId ID sản phẩm cần ngừng bán.
     * @return true nếu sản phẩm tồn tại và thao tác thành công;
     *         false nếu không tìm thấy sản phẩm.
     */
    public boolean softDeleteProduct(int productId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Kiểm tra sản phẩm có tồn tại hay không.
            Product product = em.find(Product.class, productId);

            if (product == null) {
                tx.rollback();
                return false;
            }

            // Đặt số lượng của tất cả variant thuộc sản phẩm về 0.
            em.createQuery(
                    "UPDATE ProductVariant v "
                            + "SET v.quantity = 0 "
                            + "WHERE v.product.productId = :productId")
                    .setParameter("productId", productId)
                    .executeUpdate();

            tx.commit();
            return true;

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    public List<Product> getAllProducts() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.images",
                    Product.class).getResultList();
        } finally {
            em.close();
        }
    }

    public static Product getProductById(int id) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            List<Product> list = em.createQuery(
                    "Select DISTINCT p From Product p " +
                            "Left Join Fetch p.images " +
                            "Left Join Fetch p.brand " +
                            "Left Join Fetch p.category " +
                            "Where p.productId = :id",
                    Product.class).setParameter("id", id)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    public static List<ProductVariant> getVariantsByProductId(int productId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM ProductVariant v " +
                            "WHERE v.product.productId = :id " +
                            "ORDER BY v.price, v.size",
                    ProductVariant.class)
                    .setParameter("id", productId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public static ProductVariant getVariantById(int variantId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            List<ProductVariant> list = em.createQuery(
                    "SELECT v FROM ProductVariant v JOIN FETCH v.product " +
                            "WHERE v.variantId = :id",
                    ProductVariant.class)
                    .setParameter("id", variantId)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }
}