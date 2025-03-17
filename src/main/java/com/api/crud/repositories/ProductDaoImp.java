package com.api.crud.repositories;

import com.api.crud.models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
    public List<Product> showProducts() {
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

        String jpql = "SELECT p FROM Product p " +
                "LEFT JOIN FETCH ProductDigital pd ON p.idProduct = pd.idProduct " +
                "LEFT JOIN FETCH ProductPhysical pp ON p.idProduct = pp.idProduct " +
                "WHERE p.idProduct = :idProduct";

        try {
            Product product = entityManager.createQuery(jpql, Product.class)
                    .setParameter("idProduct", idProduct)
                    .getSingleResult();
            response = Optional.ofNullable(product);
        } catch (NoResultException e) {
            logger.error("Product with ID {} not found", idProduct);
            response = Optional.empty();
        }
        return response;
    }

    @Override
    public Optional<Product> findProductByName(String nameProduct) {
        logger.debug("Executing query to find product with name: {}", nameProduct);
        Optional<Product> response;

        String jpql = "SELECT p FROM Product p " +
                "LEFT JOIN FETCH ProductDigital pd ON p.idProduct = pd.idProduct " +
                "LEFT JOIN FETCH ProductPhysical pp ON p.idProduct = pp.idProduct " +
                "WHERE p.nameProduct = :nameProduct";

        try {
            Product product = entityManager.createQuery(jpql, Product.class)
                    .setParameter("nameProduct", nameProduct)
                    .getSingleResult();
            response = Optional.ofNullable(product);
        } catch (NoResultException e) {
            logger.error("Product with name {} not found", nameProduct);
            response = Optional.empty();
        }
        return response;
    }

    @Override
    public void saveProduct(Product product) {
        logger.debug("Executing query Registering new product: {}", product.getNameProduct());
        try{
            entityManager.persist(product);
            entityManager.flush();
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
