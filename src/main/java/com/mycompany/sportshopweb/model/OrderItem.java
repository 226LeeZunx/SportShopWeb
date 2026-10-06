package com.mycompany.sportshopweb.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name="OrderItem")
public class OrderItem {
    
    @EmbeddedId
    private OrderItemId orderItemId;
    
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name="orderId")
    private Order order;
    
    @ManyToOne
    @MapsId("variantId")
    @JoinColumn(name = "variantId")
    private ProductVariant productVariant;
    
    
    private int quantity;

    public OrderItem() {
        this.quantity = 1;
    }

    public OrderItem(ProductVariant productVariant, int quantity) {
        this.productVariant= productVariant;
        this.quantity = quantity;
    }

    // Getter & Setter cho item
    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant= productVariant;
    }

    // Getter & Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}