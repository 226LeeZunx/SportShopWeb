/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anh Tuan
 */
@Entity
@Table(name = "Albums")
public class Album implements Serializable {

    @Id
    @Column(name = "album_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int albumId;

    private String name;
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany
    @JoinTable(
        name = "AlbumVariants",
        joinColumns = @JoinColumn(name = "album_id"),
        inverseJoinColumns = @JoinColumn(name = "variant_id")
    )
    private List<ProductVariant> items = new ArrayList<>();

    public Album() {
        this.items = new ArrayList<>();
    }

    public Album(int albumId, String name, String description, String imageUrl, List<ProductVariant> items) {
        this.albumId = albumId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.items = items != null ? items : new ArrayList<>();
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ProductVariant> getItems() {
        return items;
    }

    public void setItems(List<ProductVariant> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
}
