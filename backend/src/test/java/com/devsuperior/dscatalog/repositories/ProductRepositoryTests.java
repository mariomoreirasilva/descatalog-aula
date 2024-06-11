package com.devsuperior.dscatalog.repositories;

import static org.mockito.ArgumentMatchers.isNull;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long id;
	private long countTotalProducts;
	
	
	@BeforeEach
	void setUp() throws Exception {
		
		id = 1L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersistWhitAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
		
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
			//arrage;
		//não precisa mais instanciar variaveis no método ja que tem a void setUp() throws Exception
		//long id = 1L;
		//act
		repository.deleteById(id);
		Optional<Product> result = repository.findById(id);
		//assertions
		Assertions.assertFalse(result.isPresent());
				
	}
	
	@Test
	public void productshouldReturnedWhenIdExistis() {
		
		Optional<Product> result = repository.findById(id);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void productshouldReturnedNullWhenIdNotExistis() {
		
		Optional<Product> result = repository.findById(30L);
		Assertions.assertFalse(result.isPresent());
		Assertions.assertTrue(result.isEmpty());
		
	}

}
