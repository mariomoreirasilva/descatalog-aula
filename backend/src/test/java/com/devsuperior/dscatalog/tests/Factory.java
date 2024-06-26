package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		
		Product product = new Product(1L,"Phone","Good Phone", 800.0, "https://teste.com.br",Instant.parse("2024-05-28T17:10:00Z"));
		product.getCategories().add(createCategory());
		return product;
		
	}
	
	
	public static ProductDTO createProductDTO() {
		
		Product product = createProduct();
		
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(2L, "Eletrônicos");
		
	}

}
