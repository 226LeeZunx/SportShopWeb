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
    public static long countProducts() {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    try {
        return em.createQuery("SELECT COUNT(p) FROM Product p", Long.class)
                 .getSingleResult();
    } finally {
        em.close();
    }
}
    public static List<Product> getProductsByPage(int page, int pageSize) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    try {
        List<Integer> ids = em.createQuery(
                "SELECT p.productId FROM Product p ORDER BY p.productId DESC", Integer.class)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        if (ids.isEmpty()) return new ArrayList<>();

        return em.createQuery(
                "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.images " +
                "WHERE p.productId IN :ids ORDER BY p.productId DESC", Product.class)
                .setParameter("ids", ids)
                .getResultList();
    } finally {
        em.close();
      }
    }
    public void insert(Product product , int brandId,int categoryId){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            product.setBrand(em.getReference(Brand.class,brandId));
            product.setCategory(em.getReference(Category.class, categoryId));
            em.persist(product);
            tx.commit();
        }catch (RuntimeException e){
            if (tx.isActive()) tx.rollback();
            throw e;
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
    public static Product getProductById(int id){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        try{
            List<Product> list=em.createQuery(
                "Select DISTINCT p From Product p "+
                "Left Join Fetch p.images "+
                "Left Join Fetch p.brand "+
                "Left Join Fetch p.category "+
                "Where p.productId = :id",Product.class
            ).setParameter("id",id)
            .getResultList();
        return list.isEmpty() ? null : list.get(0);
        }finally{
            em.close();
        }
    }
    public static List<ProductVariant> getVariantsByProductId(int productId) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    try {
        return em.createQuery(
            "SELECT v FROM ProductVariant v " +
            "WHERE v.product.productId = :id " +
            "ORDER BY v.price, v.size", ProductVariant.class)
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
            "WHERE v.variantId = :id", ProductVariant.class)
            .setParameter("id", variantId)
            .getResultList();
        return list.isEmpty() ? null : list.get(0);
    } finally {
        em.close();
    }
}
}