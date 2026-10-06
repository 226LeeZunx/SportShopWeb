package com.mycompany.sportshopweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Cart")
public class Cart {
    @Id
    private int customerId;
    
    @OneToOne
    @MapsId
    @JoinColumn(name="customerId")
    private Customer customer;
    
    @OneToMany(mappedBy="cart")
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart() {
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

}
