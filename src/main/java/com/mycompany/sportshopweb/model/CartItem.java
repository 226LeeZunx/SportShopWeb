package com.mycompany.sportshopweb.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "CartItem")
public class CartItem {

    @EmbeddedId
    private CartItemId id;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customerId")
    private Cart cart;

    @ManyToOne
    @MapsId("variantId")
    @JoinColumn(name = "variantId")
    private ProductVariant productVariant;

    private int quantity;

    public CartItem() {
        this.quantity = 1;
    }

    public CartItem(ProductVariant productVariant, int quantity) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}