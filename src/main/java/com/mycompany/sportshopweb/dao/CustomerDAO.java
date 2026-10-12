/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.MembershipTier;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author Anh Tuan
 */
public class CustomerDAO {
    // Thêm mới thông tin khách hàng
    public boolean insert(Customer customer) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(customer);
            trans.commit();
            return true;
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Cập nhật thông tin khách hàng (Profile)
    public boolean update(Customer customer) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(customer);
            trans.commit();
            return true;
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Tìm khách hàng theo ID tài khoản User
    public Customer findByUserId(int userId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        String jpql = "SELECT c FROM Customer c WHERE c.user.userId = :userId";
        try {
            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Tìm khách hàng theo ID khách hàng
    public Customer findById(int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            return em.find(Customer.class, customerId);
        } finally {
            em.close();
        }
    }

    // Lấy hạng thành viên mặc định cho tài khoản mới
    public MembershipTier getDefaultMembershipTier() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        String jpql = "SELECT m FROM MembershipTier m ORDER BY m.minSpending ASC";
        try {
            TypedQuery<MembershipTier> query = em.createQuery(jpql, MembershipTier.class);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
