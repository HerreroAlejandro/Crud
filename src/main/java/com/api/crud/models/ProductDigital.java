package com.api.crud.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;



@Entity
@Table(name = "productDigital")
public class ProductDigital extends Product{

    @Column(name = "downloadLink")
    @Getter @Setter
    @Size(min =0, max =200)
    private String downloadLink;

    @Column(name = "license")
    @Getter @Setter
    @Size (min =0, max =50)
    private String license;

    public ProductDigital(Long idProduct, String nameProduct, BigDecimal priceProduct, String description, String imageUrl, String category, String downloadLink, String license) {
        super(idProduct, nameProduct, priceProduct, description, imageUrl, category);
        this.downloadLink = downloadLink;
        this.license = license;
    }

    public ProductDigital(){}
}
