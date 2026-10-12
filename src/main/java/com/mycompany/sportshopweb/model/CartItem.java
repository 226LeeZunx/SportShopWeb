package com.mycompany.sportshopweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "CartItem")
public class CartItem implements Serializable {

    @EmbeddedId
    private CartItemId id;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private Cart cart;

    @ManyToOne
    @MapsId("variantId")
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;

    private int quantity;

    public CartItem() {
        this.quantity = 1;
    }

    public CartItem(CartItemId id, ProductVariant productVariant, int quantity) {
        this.id = id;
        this.productVariant = productVariant;
        this.quantity = quantity;
    }

    public CartItemId getId() {
        return id;
    }

    public void setId(CartItemId id) {
        this.id = id;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}