package com.api.crud.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table (name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long idCart;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Getter @Setter
    private UserModel userCart;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<CartItem> itemsCart;

    public Cart(Long idCart, UserModel userCart, List<CartItem> itemsCart) {
        this.idCart = idCart;
        this.userCart = userCart;
        this.itemsCart = itemsCart;
    }

    public Cart(){}

}
