package com.devsuperior.dscatalog.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		//Category category = new Category();
		List<Category> list = repository.findAll();		
		return list.stream()
				.map(x -> new CategoryDTO(x))
				.collect(Collectors.toList());
//		List<CategoryDTO> listDTO = new ArrayList<CategoryDTO>();
//		
//		for(Category category : list) {
//			listDTO.add(new CategoryDTO(category));
//		}
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> entityCategory = repository.findById(id);
		
		//Category category = entityCategory.get();
		Category category = entityCategory.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		return new CategoryDTO(category);
		
		//return new CategoryDTO(entityCategory.get()); //get() serve para pegar o objeto dentro do OPTIONAL, ai não irá retornar um OPTIONAL mas sim o objeto dentro dele
			
	}

}




