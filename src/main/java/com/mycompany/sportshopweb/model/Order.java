package com.mycompany.sportshopweb.model;

import java.util.ArrayList;
import java.util.List;
import com.mycompany.sportshopweb.model.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Table(name="Order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;
    
    @ManyToOne
    @JoinColumn(name="customerId")
    private Customer customer;
    
    @OneToMany(mappedBy="order")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(Customer customer) {
        this.customer = customer;
    }

    // Getter & Setter cho orderId
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    // Getter & Setter cho customer
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Getter cho orderItems
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
    
  
