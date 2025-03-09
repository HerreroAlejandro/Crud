package com.api.crud.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long idProduct;

    @Column(name = "nameProduct", unique = true)
    @Getter @Setter
    @NotNull(message = "nameProduct cannot be null")
    @Size(min =2, max =30)
    private String nameProduct;

    @Column(name = "priceProduct")
    @Getter @Setter
    @Digits(integer = 5, fraction = 2)
    private BigDecimal priceProduct;

    @Column(name = "description")
    @Size (min =0, max =200)
    @Getter @Setter
    private String description;

    @Column(name = "imageUrl")
    @Getter @Setter
    @Size (min =0, max =200)
    private String imageUrl;

    @Column(name = "category")
    @Getter @Setter
    @Size (min =0, max =50)
    private String category;

    @OneToMany(mappedBy = "productOrderItem")
    @Getter @Setter
    private List<OrderItem> orderItems;

    public Product(Long idProduct, String nameProduct, BigDecimal priceProduct, String description, String imageUrl, String category) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(){}

}
