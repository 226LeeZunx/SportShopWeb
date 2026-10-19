package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Brand;
import com.mycompany.sportshopweb.model.Category;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO {
    public static final int PAGE_SIZE = 20;

    public static long countProducts() {
        return countProducts("", 0);
    }

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

    public static List<Product> getProductsByPage(int page, int pageSize) {
        return getProductsByPage(page, pageSize, "", 0);
    }

    public static List<Product> getProductsByPage(int page, int pageSize, String keyword, int categoryId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
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
                idQuery.setParameter("keyword", "%" + keyword.trim().toLowerCase() + "%");
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
        } finally {
            em.close();
        }
    }

    public boolean updateProduct(
            int productId,
            String productName,
            String description,
            int brandId,
            int categoryId,
            int quantity) {

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative");
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Product product = em.find(Product.class, productId);

            if (product == null) {
                tx.rollback();
                return false;
            }

            product.setProductName(productName);
            product.setDescription(description);
            product.setBrand(em.getReference(Brand.class, brandId));
            product.setCategory(em.getReference(Category.class, categoryId));
            product.setQuantity(quantity);

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

    public boolean softDeleteProduct(int productId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product product = em.find(Product.class, productId);

            if (product == null) {
                tx.rollback();
                return false;
            }

            // Đặt quantity của chính sản phẩm về 0.
            product.setQuantity(0);
            em.createQuery(
                    "UPDATE ProductVariant v SET v.quantity = 0 WHERE v.product.productId = :productId")
                    .setParameter("productId", productId)
                    .executeUpdate();
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Product> getAllProducts() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.images LEFT JOIN FETCH p.brand LEFT JOIN FETCH p.category",
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
                    "SELECT v FROM ProductVariant v JOIN FETCH v.product " +
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
        EntityManager em = DBUtil.getEmFactory().createEntityManager(); // Sửa lại thành DBUtil.getEmFactory()
        return null;
    }

    public static long countFiltered(int brandId, long catId, long minPrice, long maxPrice, String keyword) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            Long total = em.createQuery(
                    "SELECT COUNT(DISTINCT v.product.productId) FROM ProductVariant v " +
                            "WHERE (:brandId = 0 OR v.product.brand.brandId = :brandId) " +
                            "AND (:categoryId = 0 OR v.product.category.categoryId = :categoryId) " +
                            "AND v.price >= :minPrice " +
                            "AND v.price <= :maxPrice " +
                            "AND (:keyword IS NULL OR :keyword = '' OR LOWER(v.product.productName) LIKE LOWER(CONCAT('%', :keyword, '%')))",
                    Long.class)
                    .setParameter("brandId", brandId)
                    .setParameter("categoryId", catId)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .setParameter("keyword", keyword != null ? keyword.trim() : "")
                    .getSingleResult();

            return total != null ? total : 0L;
        } finally {
            em.close();
        }
    }

    public static List<Product> getFiltered(int brandId, long catId, long minPrice, long maxPrice, String sort,
            int page, int pageSize, String keyword) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            int offset = (page - 1) * pageSize;

            String jpqlId = "SELECT v.product.productId FROM ProductVariant v " +
                    "WHERE (:brandId = 0 OR v.product.brand.brandId = :brandId) " +
                    "AND (:categoryId = 0 OR v.product.category.categoryId = :categoryId) " +
                    "AND v.price >= :minPrice " +
                    "AND v.price <= :maxPrice " +
                    "AND (:keyword IS NULL OR :keyword = '' OR LOWER(v.product.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
                    +
                    "GROUP BY v.product.productId, v.product.productName ";

            if ("id_desc".equals(sort)) {
                jpqlId += "ORDER BY v.product.productId DESC";
            } else if ("price_asc".equals(sort)) {
                jpqlId += "ORDER BY MIN(v.price) ASC";
            } else if ("price_desc".equals(sort)) {
                jpqlId += "ORDER BY MAX(v.price) DESC";
            } else if ("name_asc".equals(sort)) {
                jpqlId += "ORDER BY v.product.productName ASC";
            } else {
                jpqlId += "ORDER BY v.product.productId ASC";
            }

            // 3. Truyền đầy đủ các tham số (bao gồm keyword) và thực hiện phân trang
            TypedQuery<Integer> queryId = em.createQuery(jpqlId, Integer.class)
                    .setParameter("brandId", brandId)
                    .setParameter("categoryId", catId)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .setParameter("keyword", keyword != null ? keyword.trim() : "")
                    .setFirstResult(offset)
                    .setMaxResults(pageSize);

            List<Integer> productIds = queryId.getResultList();
            if (productIds.isEmpty()) {
                return new ArrayList<>();
            }

            // 4. Lấy đầy đủ danh sách Product kèm theo các mối quan hệ (FETCH JOIN)
            String jpqlProduct = "SELECT DISTINCT p FROM Product p " +
                    "LEFT JOIN FETCH p.images " +
                    "LEFT JOIN FETCH p.brand " +
                    "LEFT JOIN FETCH p.category " +
                    "WHERE p.productId IN :productIds";

            List<Product> unorderedProducts = em.createQuery(jpqlProduct, Product.class)
                    .setParameter("productIds", productIds)
                    .getResultList();

            Map<Integer, Product> productMap = new HashMap<>();
            for (Product p : unorderedProducts) {
                productMap.put(p.getProductId(), p);
            }

            List<Product> orderedProducts = new ArrayList<>();
            for (Integer id : productIds) {
                Product p = productMap.get(id);
                if (p != null) {
                    orderedProducts.add(p);
                }
            }

            return orderedProducts;

        } finally {
            em.close();
        }
    }
}