/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.util;

import com.mycompany.sportshopweb.model.Brand;
import com.mycompany.sportshopweb.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author dung2
 */
public class BrandDB {
    public static List<Brand> getAllBrand() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            TypedQuery<Brand> query = em.createQuery("SELECT p FROM Brand p", Brand.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
