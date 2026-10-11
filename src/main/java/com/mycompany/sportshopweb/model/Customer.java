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
@Table(name = "Customers")
public class Customer implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "tier_id")
    private MembershipTier membershipTier;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String gender;
    
    public Customer(){
        this.customerId = 0;
        this.user = new User();
        this.membershipTier = new MembershipTier();
        this.firstName = "";
        this.lastName = "";
        this.gender = "";
    }
    
    public Customer(int customerId, User user, String firstName, String lastName, String gender){
        this.customerId = customerId;
        this.user = user;
        this.membershipTier = new MembershipTier();
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public Customer(int customerId, User user, MembershipTier membershipTier, String firstName, String lastName, String gender){
        this.customerId = customerId;
        this.user = user;
        this.membershipTier = membershipTier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MembershipTier getMembershipTier() {
        return membershipTier;
    }

    public void setMembershipTier(MembershipTier membershipTier) {
        this.membershipTier = membershipTier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}


