package com.api.crud.services;

import com.api.crud.DTO.ProductDTO;
import com.api.crud.models.Product;
import com.api.crud.models.ProductDigital;
import com.api.crud.models.ProductPhysical;
import com.api.crud.repositories.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<ProductDTO> showProducts() {
        logger.info("Starting to process showProducts in service");
        List<Product> products = productDao.showProducts();
        List<ProductDTO> productDTOs = new ArrayList<>();

        for (Product product : products) {
            if (product instanceof ProductDigital) {
                ProductDigital digitalProduct = (ProductDigital) product;

                ProductDTO digitalDTO = new ProductDTO(
                        digitalProduct.getIdProduct(),
                        digitalProduct.getNameProduct(),
                        digitalProduct.getPriceProduct(),
                        digitalProduct.getDescription(),
                        digitalProduct.getImageUrl(),
                        digitalProduct.getCategory(),
                        digitalProduct.getDownloadLink(),
                        digitalProduct.getLicense(),
                        null,
                        null
                );
                productDTOs.add(digitalDTO);
            } else if (product instanceof ProductPhysical) {
                ProductPhysical physicalProduct = (ProductPhysical) product;

                ProductDTO physicalDTO = new ProductDTO(
                        physicalProduct.getIdProduct(),
                        physicalProduct.getNameProduct(),
                        physicalProduct.getPriceProduct(),
                        physicalProduct.getDescription(),
                        physicalProduct.getImageUrl(),
                        physicalProduct.getCategory(),
                        null,
                        null,
                        physicalProduct.getStockProduct(),
                        physicalProduct.getShippingAddress()
                );
                productDTOs.add(physicalDTO);
            }
        }
        return productDTOs;
    }


    public Optional<ProductDTO> findProductById(Long idProduct) {
        Optional<Product> productOpt = productDao.findProductById(idProduct);

        if (productOpt.isEmpty()) {
            return Optional.empty();
        }

        Product product = productOpt.get();
        ProductDTO productDTO;

        if (product instanceof ProductDigital) {
            ProductDigital pd = (ProductDigital) product;
            productDTO = new ProductDTO(pd.getIdProduct(), pd.getNameProduct(), pd.getPriceProduct(),
                    pd.getDescription(), pd.getImageUrl(), pd.getCategory(), pd.getDownloadLink(), pd.getLicense());
        } else if (product instanceof ProductPhysical) {
            ProductPhysical pp = (ProductPhysical) product;
            productDTO = new ProductDTO(pp.getIdProduct(), pp.getNameProduct(), pp.getPriceProduct(),
                    pp.getDescription(),pp.getImageUrl(), pp.getCategory(), pp.getStockProduct(), pp.getShippingAddress());
        } else {
            productDTO = new ProductDTO(product.getIdProduct(), product.getNameProduct(), product.getPriceProduct(),
                    product.getDescription(),product.getImageUrl(), product.getCategory());
        }
        return Optional.of(productDTO);
    }

    public Optional<ProductDTO> findProductByName(String nameProduct) {
        Optional<Product> productOpt = productDao.findProductByName(nameProduct);

        if (productOpt.isEmpty()) {
            return Optional.empty();
        }

        Product product = productOpt.get();
        ProductDTO productDTO;

        if (product instanceof ProductDigital) {
            ProductDigital pd = (ProductDigital) product;
            productDTO = new ProductDTO(pd.getIdProduct(), pd.getNameProduct(), pd.getPriceProduct(),
                    pd.getDescription(), pd.getImageUrl(), pd.getCategory(), pd.getDownloadLink(), pd.getLicense());
        } else if (product instanceof ProductPhysical) {
            ProductPhysical pp = (ProductPhysical) product;
            productDTO = new ProductDTO(pp.getIdProduct(), pp.getNameProduct(), pp.getPriceProduct(),
                    pp.getDescription(), pp.getImageUrl(), pp.getCategory(), pp.getStockProduct(), pp.getShippingAddress());
        } else {
            productDTO = new ProductDTO(product.getIdProduct(), product.getNameProduct(), product.getPriceProduct(),
                    product.getDescription(), product.getImageUrl(), product.getCategory());
        }
        return Optional.of(productDTO);
    }

    public void saveProduct(ProductDTO productDTO) {
        Product product;
        if ("DIGITAL".equalsIgnoreCase(productDTO.getCategory())) {
            product= new ProductDigital(
                    productDTO.getNameProduct(),
                    productDTO.getPriceProduct(),
                    productDTO.getDescription(),
                    productDTO.getImageUrl(),
                    productDTO.getCategory(),
                    productDTO.getDownloadLink(),
                    productDTO.getLicense()
            );
        } else if ("PHYSICAL".equalsIgnoreCase(productDTO.getCategory())) {
            product= new ProductPhysical(
                    productDTO.getNameProduct(),
                    productDTO.getPriceProduct(),
                    productDTO.getDescription(),
                    productDTO.getImageUrl(),
                    productDTO.getCategory(),
                    productDTO.getStockProduct(),
                    productDTO.getShippingAddress()
            );
        } else {
            throw new IllegalArgumentException("Invalid product type: " + productDTO.getCategory());
        }
        productDao.saveProduct(product);
    }


    public boolean deleteProduct(Long idProduct){
        logger.info("Starting to process delete Product with id {} in services", idProduct);
        boolean result = productDao.deleteProduct(idProduct);
        if (result) {
            logger.info("Product with id {} deleted", idProduct);
        }else{
                logger.debug("Product with id {} not deleted in services", idProduct);
            }
            return result;
        }


}
