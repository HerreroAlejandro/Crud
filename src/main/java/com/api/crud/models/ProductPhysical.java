package com.api.crud.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "productPhysical")
public class ProductPhysical extends Product{


    @Column(name = "stockProduct")
    @Getter @Setter
    @Size(min =2, max =30)
    private int stockProduct;

    @Getter @Setter
    @Column(name = "shippingAddress")
    @Size (min =0, max =200)
    private String shippingAddress;

    public ProductPhysical(Long idProduct, String nameProduct, BigDecimal priceProduct, String description, String imageUrl, String category, int stockProduct, String shippingAddress) {
        super(idProduct, nameProduct, priceProduct, description, imageUrl, category);
        this.stockProduct = stockProduct;
        this.shippingAddress = shippingAddress;
    }

    public ProductPhysical(){}
}
