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
import com.devsuperior.dscatalog.services.exceptions.BadRequestException;
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
	

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) throws BadRequestException {
		//converter para o tipo Categoy(entidade)
		Category entity = new Category();
		
		List<Category> list = repository.findAll();
		
		//validação para impedir que coloquem categorias repetidas
//		for(int i = 0; i < list.size(); i++) {
//			if(dto.getName().equalsIgnoreCase(list.get(i).getName())) {
//				throw new EntityNotFoundException("Já existe esse nome cadastrado");
//			}
//		}
		
		for(Category category : list) {
			if(dto.getName().equalsIgnoreCase(category.getName())) {
				throw new BadRequestException("There is already a category with that name registered");
			}
		}
		
		//se não existir nome repetido então ele seta o nome na entity
		entity.setName(dto.getName());
		
		//salva o nome na entity
		//repository.save(entity); por padrão retorna uma referencia para a entidade salva, por isso fazer como na linha abaixo
		entity = repository.save(entity); //por padrão retorna uma referencia para a entidade salva
	
		//converter novamente para categoryDTO
		return new CategoryDTO(entity); 
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getOne(id); //cria um objeto provisório, sem ir no banco de dados, e somente quando eu mandar salvar que ele fará a busca na base, justamente para não precisar acessar a base duas vezes
			
			entity.setName(dto.getName());
			entity = repository.save(entity);
			
			return new CategoryDTO(entity);	
			
		} catch (javax.persistence.EntityNotFoundException e) {
			throw new EntityNotFoundException("Id " + id +" not found"); //é a minha exception personalizada
		}
		
	}

}




