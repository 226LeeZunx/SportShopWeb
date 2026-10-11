/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Anh Tuan
 */
@Entity
@Table(name = "MembershipTiers")
public class MembershipTier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private int tierId;

    @Column(name = "tier_name")
    private String tierName;

    @Column(name = "min_spending")
    private long minSpending;

    @Column(name = "discount_rate")
    private float discountRate;

    @Column(name = "point_multiplier")
    private float pointMultiplier;

    public MembershipTier() {
        this.tierId = 0;
        this.tierName = "";
        this.minSpending = 0;
        this.discountRate = 0.0f;
        this.pointMultiplier = 0.0f;
    }

    public MembershipTier(int tierId, String tierName, long minSpending, float discountRate, float pointMultiplier) {
        this.tierId = tierId;
        this.tierName = tierName;
        this.minSpending = minSpending;
        this.discountRate = discountRate;
        this.pointMultiplier = pointMultiplier;
    }

    public int getTierId() {
        return tierId;
    }

    public void setTierId(int tierId) {
        this.tierId = tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public long getMinSpending() {
        return minSpending;
    }

    public void setMinSpending(long minSpending) {
        this.minSpending = minSpending;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public float getPointMultiplier() {
        return pointMultiplier;
    }

    public void setPointMultiplier(float pointMultiplier) {
        this.pointMultiplier = pointMultiplier;
    }
}
