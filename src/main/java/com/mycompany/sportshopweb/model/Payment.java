package com.mycompany.sportshopweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

//@author Vu

@Entity
@Table(name = "Payment")
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "order_id")
    private int orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "amount")
    private long amount;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    // constructor 0 tham so
    public Payment() {
        paymentId = 0;
        orderId = 0;
        paymentMethod = PaymentMethod.COD;
        amount = 0;
        paymentTime = LocalDateTime.now();
        transactionCode = "";
    }

    // constructor co tham so
    public Payment(
            int paymentId, // CÓ THỂ XÓA , ĐỂ Id CHO DATABASE tự tạo
            int orderId,
            PaymentMethod paymentMethod,
            long amount,
            String transactionCode,
            LocalDateTime paymentTime) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transactionCode = transactionCode;
        this.paymentTime = paymentTime;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
}
