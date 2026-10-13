package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class OrderDAO {

    public List<Order> findByCustomerId(int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql =
                "SELECT DISTINCT o " +
                "FROM Order o " +
                "LEFT JOIN FETCH o.orderItems oi " +
                "JOIN FETCH o.customer c " +
                "JOIN FETCH c.user " +
                "WHERE c.customerId = :customerId " +
                "ORDER BY o.orderId DESC";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);

            query.setParameter("customerId", customerId);

            return query.getResultList();

        } finally {
            em.close();
        }
    }
    public void insertOrder(Order order){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try{
            trans.begin();
            em.persist(order);
            trans.commit();
        }
        catch (Exception e) {
            trans.rollback();
        } finally {
            em.close();
        }
    }
    public void updateOrder(Order order){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(order);
            trans.commit();
        }
        catch (Exception e) {
            trans.rollback();
        } finally {
            em.close();
        }
        
    }
    public void cancelOrder(int orderId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Order order = em.find(Order.class, orderId);
            if (order == null) {
            throw new IllegalArgumentException("Order not found!");
            }
            order.setOrderStatus(OrderStatus.CANCELLED);
            trans.commit();
        }
        catch (Exception e) {
            trans.rollback();
        } finally {
            em.close();
        }
}
}