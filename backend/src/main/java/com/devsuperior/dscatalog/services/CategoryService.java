package com.devsuperior.dscatalog.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.BadRequestException;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		//Category category = new Category();
		Page<Category> list = repository.findAll(pageable);
		return list
				.map(x -> new CategoryDTO(x)); //Page já é do tipo Stream()
		
//		return list.stream()
//				.map(x -> new CategoryDTO(x))
//				.collect(Collectors.toList());
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
				throw new BadRequestException("There is already a category with that name registered"); //JÁ EXISTE CATEGORIA REGISTRADA COM ESSE NOME
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

	public void delete(Long id) { //não usa o transactional, pq vou capturar uma exception lá do banco de dados e com transactional não é possível capturar
		
		//para caso tente deletar um id que não existe
		try {
			repository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) { //exception para caso tente deletar um ID que não existe
			throw new EntityNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) { //não posso deletar uma categoria pq os produtos irão ficar sem uma Category, isso irá gerar uma inconsistência nos dados
			//para capturar uma possível EXCEPTION de integridade para caso tente deletar algo que não pode deletar
			throw new DataBaseException("Integrity violation");
		}
		
	}
	
}




