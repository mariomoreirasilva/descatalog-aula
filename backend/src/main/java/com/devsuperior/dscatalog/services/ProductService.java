package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;



@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
		
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		Page<Product> list = repository.findAll(pageRequest);		
		return list.map(x -> new ProductDTO(x));		
		}
	
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		//optional evita trabalhar com valor nulo
		Optional<Product> obj = repository.findById(id);
		//o metodo get pega o objeto do optional
		//Product entity = obj.get(); //outra implantação do Optional abaixo com expressão lambida
		Product entity = obj.orElseThrow( () -> new ResourceNotFoundException("Entidade não encontrada."));
		
		return new ProductDTO(entity, entity.getCategories());
		
	}
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		
		Product entity = new Product();
		//entity.setName(dto.getName());
		//o metodo save retorna uma referencia para a entidade salva, por isso entity = repository.save(entity); e não simplesmente repository.save(entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
		
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		//caso não ache o código da categoria, por isso o bloco try
		try {
			 Product entity = repository.getReferenceById(id);
			// entity.setName(dto.getName());
			 entity = repository.save(entity);	
			 return new ProductDTO(entity);
		
			} catch(EntityNotFoundException e) 
				{
					throw new ResourceNotFoundException("Categoria não encontrada: " + id);
				}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
	        	repository.deleteById(id);    		
		}
	    	catch (DataIntegrityViolationException e) {
	        	throw new DatabaseException("Falha de integridade referencial");
	   	}
	}

	
}
	
