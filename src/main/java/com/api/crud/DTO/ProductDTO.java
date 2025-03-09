package com.api.crud.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class ProductDTO {
    @Getter @Setter
    private Long idProduct;

    @Getter @Setter
    private String nameProduct;

    @Getter @Setter
    private BigDecimal priceProduct;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private String imageUrl;

    @Getter @Setter
    private String category;

    public ProductDTO(Long idProduct, String nameProduct, BigDecimal priceProduct, String description, String imageUrl, String category) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public ProductDTO(){}

}
