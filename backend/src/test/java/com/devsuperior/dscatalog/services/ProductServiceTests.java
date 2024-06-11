package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private long existingId;
	
	
	@BeforeEach
	void setup() throws Exception{
		existingId = 1L;
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
	
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		//serve para saber se o método do repository foi chamado alguma vez na ação acima. Recomenda-se fazer isso
		Mockito.verify(repository).deleteById(existingId);
		
	}
}
