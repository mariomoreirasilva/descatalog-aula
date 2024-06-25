package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;
	
	private Long existingId;
	private Long nonExistingId;
	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	
	
	
	@BeforeEach
	void setup() throws Exception{
		//o valor não importa por ser mock e simular o comportamento
		existingId = 1L;
		nonExistingId = 2L;
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		//findallpaged recebe um pageble. Só que no mock não importa o que vem no conteúdo. Por isso o any() do ArgumentMatchers. Mesma coisa se for ArgumentMatchers.any
		when(service.findAllPaged(any())).thenReturn(page);
		//simular o comportamento do findById
		when(service.findById(existingId)).thenReturn(productDTO);
		//quando não tem retorna uma excessão que é capturada e tratada no resourceExceptionHandler pela classe controlerAdviser que retorna (HttpStatus.NOT_FOUND) 404
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
	}
    
	@Test
	public void findAllShowdReturnPage() throws Exception {
		//chamada dos controladores ou resources usa assim. Abaixo o assertion espera que retorne 200 do http
		//mockMvc.perform(get("/products")).andExpect(status().isOk());
		
		
		//poderia fazer o seguinte abaixo 
		ResultActions result = 
				mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));//retorno é um json
				               
		//ai fazemos as assertion conforme abaixo:
		result.andExpect(status().isOk());
		
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/products{id}", existingId).accept(MediaType.APPLICATION_JSON));//retorno é um json
		
		result.andExpect(status().isOk());		
		//agora verificar se retornou um produto
		//a função jsonPath verifica o conteudo Json tem os campos do produto. Analisa na resposta sem tem id, description, etc
		//o $ acessa o objeto da resposta. abaixo vendo se no corpo da resposta tem um campo id.
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		//todos os campos do json do postman		
		
	}
	
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		//pq o notFound?
		//o service pode lançar exceção. Lançado o @ControllerAdvice pega e trata na classe ResourceExceptionHandler.
		// no @ExceptionHandler(ResourceNotFoundException.class) lança o err.setStatus(HttpStatus.NOT_FOUND.value()); que é 404
		//esse assertion serve para ver se o retorno é 404.
		
		ResultActions result = 
				mockMvc.perform(get("/products{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));//retorno é um json
		
		result.andExpect(status().isNotFound());
	}
}
