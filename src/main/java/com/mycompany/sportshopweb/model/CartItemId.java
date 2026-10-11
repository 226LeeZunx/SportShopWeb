package com.mycompany.sportshopweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartItemId implements Serializable {

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "variant_id")
    private int variantId;

    public CartItemId() {
    }

    public CartItemId(int customerId, int variantId) {
        this.customerId = customerId;
        this.variantId = variantId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
        if (!(o instanceof CartItemId)) return false;

        CartItemId that = (CartItemId) o;

        return customerId == that.customerId
                && variantId == that.variantId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, variantId);
    }
}
