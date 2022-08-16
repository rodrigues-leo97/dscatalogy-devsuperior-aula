package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory { //classe responsável por instanciar objetos (factory -> fábrica)
    public static Product createProduct() { //função estática para retornar um produto
        Product product = new Product(1L, "Phone", "good fone",  800.00, "https://img.com/img.png", Instant.parse("2020-07-13T20:50:07.12345Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
