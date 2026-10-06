package com.mycompany.sportshopweb.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemId implements Serializable {

    private int orderId;
    private int variantId;

    public OrderItemId() {
    }

    public OrderItemId(int orderId, int variantId) {
        this.orderId = orderId;
        this.variantId = variantId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemId)) return false;

        OrderItemId that = (OrderItemId) o;

        return orderId == that.orderId
                && variantId == that.variantId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, variantId);
    }
}