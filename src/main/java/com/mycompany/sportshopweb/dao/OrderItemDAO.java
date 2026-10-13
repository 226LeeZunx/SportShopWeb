/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderItem;
import com.mycompany.sportshopweb.model.OrderItemId;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 *
 * @author ADMIN
 */
public class OrderItemDAO {
    public void insertOrderItem(int orderId, int variantId, int quantity) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    EntityTransaction trans = em.getTransaction();

    try {
        trans.begin();

        Order order = em.find(Order.class, orderId);
        ProductVariant variant = em.find(ProductVariant.class, variantId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found!");
        }

        if (variant == null) {
            throw new IllegalArgumentException("Product variant not found!");
        }

        OrderItemId id = new OrderItemId(orderId, variantId);

        OrderItem orderItem = new OrderItem(variant, quantity);
        orderItem.setOrder(order);
        orderItem.setOrderItemId(id);

        em.persist(orderItem);

        trans.commit();

    } catch (Exception e) {

            trans.rollback();
    } finally {
        em.close();
    }
} 
}
