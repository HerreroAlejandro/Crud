package com.api.crud.repositories;

import com.api.crud.models.Product;

import java.util.List;
import java.util.Optional;


public interface ProductDao {

    List<Product> getAdminProducts();

    Optional<Product> findProductById(Long idProduct);

    Optional<Product> findProductByName(String nameProduct);

    void saveProduct(Product product);

    boolean deleteProduct(Long idProduct);


}
