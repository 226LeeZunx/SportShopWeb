
package com.mycompany.sportshopweb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private int voucherId;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    private double discount;

    private boolean active;

    public Voucher() {
    }

    public Voucher(int voucherId, String code,
                   double discount, boolean active) {
        this.voucherId = voucherId;
        this.code = code;
        this.discount = discount;
        this.active = active;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
