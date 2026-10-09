package com.mycompany.sportshopweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

//@author Vu
@Entity
@Table(name = "membership_point_transaction")
public class MembershipPointTransaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @Column(name = "points")
    private int points;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    // constructor 0 tham so
    public MembershipPointTransaction() {
        transactionId = 0;
        points = 0;
        createAt = LocalDateTime.now();
    }

    // constructor co tham so
    public MembershipPointTransaction(
            int points,
            LocalDateTime createAt,
            Payment payment) {
        this.points = points;
        this.createAt = createAt;
        this.payment = payment;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
