package com.api.crud.repositories;

import com.api.crud.models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class ProductDaoImp implements ProductDao{

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImp.class);

    @Override
    public List<Product> getAdminProducts() {
        logger.debug("Executing query to fetch products");
        List<Product> products;
        try{
            String query ="FROM Product p";
            products= entityManager.createQuery(query, Product.class).getResultList();
        } catch(Exception e){
            logger.error("Error while querying product {}:", e.getMessage());
            products= Collections.emptyList();
        }
        return products;
    }

    @Override
    public Optional<Product> findProductById(Long idProduct) {
        logger.debug("Executing query to find product with ID: {}", idProduct);
        Optional<Product> response;
        try{
            Product product = entityManager.find(Product.class, idProduct);
            response = Optional.ofNullable(product);
        } catch(Exception e){
            logger.error("Error while querying product with ID {}: {}", idProduct, e.getMessage());
            response = Optional.empty();
        }
        return response;
    }

    @Override
    public Optional<Product> findProductByName(String nameProduct) {
        logger.debug("Executing query to find product with name: {}", nameProduct);
        Optional<Product> response;

        try {
            Product product = entityManager.createQuery(
                            "SELECT p FROM Product p WHERE p.nameProduct = :nameProduct", Product.class)
                    .setParameter("nameProduct", nameProduct)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);

            response = Optional.ofNullable(product);
        } catch (Exception e) {
            logger.error("Error while querying product with name {}: {}", nameProduct, e.getMessage());
            response = Optional.empty();
        }
        return response;
    }

    @Override
    public void saveProduct(Product product) {
        logger.debug("Executing query Registering new product: {}", product.getNameProduct());
        try{
            entityManager.persist(product);
            logger.debug("Query Product with ID {} was successfully saved", product.getIdProduct());
        }catch(Exception e){
            logger.error("Error query saving Product with ID {}: {}", product.getIdProduct(), e.getMessage());
        }
    }

    @Override
    public boolean deleteProduct(Long idProduct) {
        logger.debug("Executing query Attempting to delete product with ID: {}", idProduct);
        boolean response =false;
        try{
            Product product = entityManager.find(Product.class, idProduct);
            if (product != null) {
                entityManager.remove(product);
                response = true;
            }}catch (Exception e) {
                logger.error("Error while querying delete product with ID {}: {}", idProduct, e.getMessage(), e);
            }
        return response;
        }


}
