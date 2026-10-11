/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.util;

import com.mycompany.sportshopweb.model.Brand;
import com.mycompany.sportshopweb.model.Category;
import com.mycompany.sportshopweb.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProductDB {
    
    public static List<Product> getAllProducts() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
            return query.getResultList();
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
}