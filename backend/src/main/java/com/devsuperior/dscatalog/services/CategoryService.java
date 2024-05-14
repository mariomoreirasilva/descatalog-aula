package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;



@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	//teste para depois transformar para DTO
	/*
	public List<Category> findAll(){
		return repository.findAll();
	}
	//fim teste	 
	 */
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		//expressão lambida
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		//semelhante abaixo:
		/*
		List<CategoryDTO> listDTO = new ArrayList<>();
		for(Category cat : list) {
			listDTO.add(new CategoryDTO(cat));
		}
		*/
		//fim demostração lambida		
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		//optional evita trabalhar com valor nulo
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.get();
		return new CategoryDTO(entity);
		
	}
}
