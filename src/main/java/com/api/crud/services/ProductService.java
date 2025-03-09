package com.api.crud.services;

import com.api.crud.DTO.ProductDTO;
import com.api.crud.models.Product;
import com.api.crud.repositories.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<ProductDTO> getAdminProducts() {
        logger.info("Starting to process getAdminProducts in service");
        List<Product> products = productDao.getAdminProducts();
        if (products.isEmpty()){
            logger.debug("No products found in service.");
        }
        List<ProductDTO> productsDTO = products.stream()
                .map(product -> new ProductDTO(product.getIdProduct(), product.getNameProduct(), product.getPriceProduct(), product.getDescription(), product.getImageUrl(), product.getCategory()))
                .collect(Collectors.toList());
        logger.info("Successfully retrieved {} products", products.size());
        return productsDTO;
    }

    public Optional<ProductDTO> findProductById(Long idProduct) {
        logger.info("Starting to process Search for product with ID: {} in service", idProduct);

        Optional<ProductDTO> productAdminDTO = productDao.findProductById(idProduct)
                .map(product -> {
                    logger.info("Product with ID {} found in service", idProduct);
                    return new ProductDTO(product.getIdProduct(), product.getNameProduct(), product.getPriceProduct(), product.getDescription(), product.getImageUrl(), product.getCategory());
                });
        if (productAdminDTO.isEmpty()) {
            logger.info("Product with ID {} not found in service", idProduct);
        }
        return productAdminDTO;
    }

    public Optional<ProductDTO> findProductByName(String nameProduct){
        logger.info("Starting to process findProductByName: {}", nameProduct);
        Optional<ProductDTO> response = productDao.findProductByName(nameProduct)
                .map(product -> new ProductDTO(product.getIdProduct(), product.getNameProduct(), product.getPriceProduct(), product.getDescription(), product.getImageUrl(), product.getCategory()));

        if (response.isPresent()){
            logger.info("product with name: {} was found", nameProduct);
        }else{
            logger.info("product with name: {} wasn't found", nameProduct);
        }
        return response;
    }


    public void saveProduct(ProductDTO productDTO) {
        logger.info("Starting to process saveProduct with name {} in services", productDTO.getNameProduct());

        try {
            Product product = new Product();
            product.setNameProduct(productDTO.getNameProduct());
            product.setPriceProduct(productDTO.getPriceProduct());
            product.setDescription(productDTO.getDescription());
            product.setImageUrl(productDTO.getImageUrl());
            product.setCategory(productDTO.getCategory());

            productDao.saveProduct(product);
            logger.info("Product with name {} successfully saved", productDTO.getNameProduct());

        } catch (Exception e) {
            logger.error("Error saving product with name {} in services: {}", productDTO.getNameProduct(), e.getMessage());
            throw new RuntimeException("Error saving product: " + e.getMessage());
        }
    }

    public boolean deleteProduct(Long idProduct){
        logger.info("Starting to process delete Product with id {} in services", idProduct);
        boolean result = productDao.deleteProduct(idProduct);
        if (result) {
            logger.info("Product with id {} deleted", idProduct);
        }else{
                logger.debug("Product with id {} not deleted in services");
            }
            return result;
        }


}
