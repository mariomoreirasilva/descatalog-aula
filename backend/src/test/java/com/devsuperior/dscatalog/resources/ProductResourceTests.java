package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.tests.Factory;

@WebMvcTest(ProductResourceTests.class)
public class ProductResourceTests {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;
		
	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	
	
	
	@BeforeEach
	void setup() throws Exception{
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		//findallpaged recebe um pageble. Só que no mock não importa o que vem no conteúdo. Por isso o any() do ArgumentMatchers. Mesma coisa se for ArgumentMatchers.any
		when(service.findAllPaged(any())).thenReturn(page);
		
		
	}
    
	@Test
	public void findAllShowdReturnPage() throws Exception {
		//chamada dos controladores ou resources usa assim. Abaixo o assertion espera que retorne 200 do http
		mockMvc.perform(get("/products")).andExpect(status().isOk());
		
	}
	
}
