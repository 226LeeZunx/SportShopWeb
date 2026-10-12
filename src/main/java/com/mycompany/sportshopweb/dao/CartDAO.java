
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class CartDAO {

    public Cart findByCustomerId(int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            String jpql =
                "SELECT DISTINCT c " +
                "FROM Cart c " +
                "LEFT JOIN FETCH c.cartItems ci " +
                "LEFT JOIN FETCH ci.productVariant pv " +
                "LEFT JOIN FETCH pv.product p " +
                "WHERE c.customerId = :customerId";

            TypedQuery<Cart> query = em.createQuery(jpql, Cart.class);

            query.setParameter("customerId", customerId);

            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;

        } finally {
            em.close();
        }
    }
}
