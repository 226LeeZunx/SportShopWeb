/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java 
 */
package com.mycompany.sportshopweb.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="ProductImages")
public class ProductImage implements Serializable{
    @Id
    @Column(name = "product_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productImageId;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "is_primary")
    private boolean isPrimary;
    @Column(name = "sort_order")
    private int sortOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") 
    private Product product;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ProductImage() {
    }

    public ProductImage(int productImageId, String imageUrl, boolean isPrimary, int sortOrder, LocalDateTime createdAt) {
        this.productImageId = productImageId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
    }

    public int getProductImageId() { return productImageId; }
    public void setProductImageId(int productImageId) { this.productImageId = productImageId; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
    
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}