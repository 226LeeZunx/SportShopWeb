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
@Table(name = "OrderItem")
public class OrderItem implements Serializable {
    
    @EmbeddedId
    private OrderItemId orderItemId;
    
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne
    @MapsId("variantId")
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;
    
    private int quantity;

    public OrderItem() {
        this.quantity = 1;
    }

    public OrderItem( ProductVariant productVariant, int quantity) {
        
        this.productVariant= productVariant;
        this.quantity = quantity;
    }
    
    public OrderItemId getOrderItemId(){
        return orderItemId;
    }
    
    public void setOrderItemId(OrderItemId orderItemId){
        this.orderItemId = orderItemId;
    }
    
    public Order getOrder(){
        return order;
    }
    public void setOrder(Order order){
        this.order = order;
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