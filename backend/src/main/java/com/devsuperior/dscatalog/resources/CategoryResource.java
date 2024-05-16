package com.devsuperior.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	@Autowired
	private CategoryService service;
	
	//@GetMapping	
	//public ResponseEntity<List<Category>> findAll(){	
		//testando a API
		/*
		List<Category> list = new ArrayList<>();
		list.add(new Category(1L, "Teste Cat 1"));
		list.add(new Category(2L, "Teste Cat 2"));
		return ResponseEntity.ok().body(list);	
		*/	
		//fim teste
		//abaixo segunto teste, mas não pode acessar diretamente a entidade. Tinha que ser o DTO
	//	List<Category> list = service.findAll();		
	//	return ResponseEntity.ok(list);
	//}
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list = service.findAll();
		return ResponseEntity.ok(list);
	}
	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
		 dto = service.insert(dto);
		 //colocar o cabeario da resposta de inserção
		 URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		 //retorno com http 201(criado)
		 return ResponseEntity.created(uri).body(dto);
		 
	}


}
