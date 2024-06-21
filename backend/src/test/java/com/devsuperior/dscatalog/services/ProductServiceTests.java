package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository productrepository;
	
	@Mock
	private CategoryRepository categoryrepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;//pageImpl é classe concreta que representa uma página de dados. No caso página de produto
	private Product product;
	private Category category;
	private ProductDTO productDTO;
	
	
	@BeforeEach
	void setup() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		
		//simular o comportamento do findall ArgumentMatcher é qualquer valor.
		//tem que fazer o casting pois tem sobrecarga de métodos. findall tem mais de um. findAll((Pageable)Argu.....
		Mockito.when(productrepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		//simulando o save.  ArgumentMatchers.any() significa qualquer objeto
		Mockito.when(productrepository.save(ArgumentMatchers.any())).thenReturn(product);
		//simulando o findById
		Mockito.when(productrepository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(productrepository.findById(nonExistingId)).thenReturn(Optional.empty());
				
		//simulando o getone
		Mockito.when(productrepository.getOne(existingId)).thenReturn(product);
		Mockito.when(productrepository.getOne(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		//simulando o getone do category
		Mockito.when(categoryrepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryrepository.getOne(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(productrepository.existsById(existingId)).thenReturn(true);
		Mockito.when(productrepository.existsById(nonExistingId)).thenReturn(false);
		
		Mockito.when(productrepository.existsById(dependentId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productrepository).deleteById(dependentId);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class,  () -> {
			service.update(nonExistingId,productDTO);
		});
		
	}
	
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		//ProductDTO productDTO = Factory.createProductDTO();
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class,  () -> {
			service.findById(nonExistingId);
		});
		
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);;
		
	}
	
	
	
	@Test
	public void findAllPagedShowldReturnPage() {
		Pageable pageble = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageble);
		//saber se foi chamado o método no repository
		Assertions.assertNotNull(result);
		Mockito.verify(productrepository).findAll(pageble); //ou Mockito.verify(repository, Mockito.times(1)).findAll(pageble);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
	
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		//serve para saber se o método do repository foi chamado alguma vez na ação acima. Recomenda-se fazer isso
		Mockito.verify(productrepository, Mockito.times(1)).deleteById(existingId);		
		//o spring não lança exceção quando chama o delete.  							
	}
	
	/*
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	*/
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
		
		Assertions.assertThrows(DatabaseException.class,  () -> {
			service.delete(dependentId);
		});
		
	}
	
	
}
