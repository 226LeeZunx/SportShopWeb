/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.CartItem;
import com.mycompany.sportshopweb.model.CartItemId;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CartItemDAO {
    public void insertCartItem(int customerId, int variantId, int quantity){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
        trans.begin();
        
        Cart cart = em.find(Cart.class, customerId);
        ProductVariant variant = em.find(ProductVariant.class, variantId);
        if (cart == null) {
            throw new IllegalArgumentException(
                "Không tìm thấy giỏ hàng của khách hàng ID: " + customerId
            );
        }
        if (variant == null) {
            throw new IllegalArgumentException(
                "Không tìm thấy phiên bản sản phẩm ID: " + variantId
            );
        }
        CartItemId id = new CartItemId(customerId, variantId);
        CartItem cartItem = em.find(CartItem.class, id);
        if(cartItem != null) {
            cartItem.setQuantity(quantity + cartItem.getQuantity());
        }else {  
        
        cartItem = new CartItem(id,variant,quantity);
        cartItem.setCart(cart);
        em.persist(cartItem);
        }
        trans.commit();
        }
        catch (Exception e){
            trans.rollback();
        }
        finally{
            em.close();
        }
        }
    public void updateCartItem(int customerId, int variantId, int quantity){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
        trans.begin();
        CartItemId id = new CartItemId(customerId, variantId);
        CartItem cartItem = em.find(CartItem.class, id);
        if (cartItem == null) {
            throw new IllegalArgumentException(
                "Không tìm thấy sản phẩm trong giỏ hàng"
            );
        }
        cartItem.setQuantity(quantity);
        trans.commit();
        }
        catch (Exception e){
            if (trans.isActive()) {
            trans.rollback();
        }

        // Không nuốt lỗi nữa: báo lỗi cho Servlet.
        throw e;
        }
        finally{
            em.close();
        }
    }
    public void deleteCartItem(int customerId, int variantId){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            CartItemId id = new CartItemId(customerId, variantId);
            CartItem cartItem = em.find(CartItem.class, id);
            if (cartItem == null) {
                throw new IllegalArgumentException(
                    "Không tìm thấy sản phẩm trong giỏ hàng"
                );
            }
            em.remove(cartItem);
            trans.commit();
        }
        catch (Exception e){
            trans.rollback();
        }
        finally{
            em.close();
        }
    }
    }

