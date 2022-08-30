package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.BadRequestException;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        //Product product = new Product();
        Page<Product> list = repository.findAll(pageable);
        return list
                .map(x -> new ProductDTO(x, x.getCategories())); //Page já é do tipo Stream()
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> entityProduct = repository.findById(id);

        //Product Product = entityProduct.get();
        Product entity = entityProduct.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories()); //usando o construtor personalizado para ter as Categorias, e passará a retornar um DTO com uma lista de categorias
        //return new ProductDTO(entityProduct.get()); //get() serve para pegar o objeto dentro do OPTIONAL, ai não irá retornar um OPTIONAL mas sim o objeto dentro dele
    }


    @Transactional
    public ProductDTO insert(ProductDTO dto) throws BadRequestException {
        //converter para o tipo Product(entidade)
        Product entity = new Product();

        //método para setar os dados do Product, para que assim não precise repetir código em outros métodos
        //o método já serve para que isso seja feito pensando no relacionamento entre as tabelas
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity); //por padrão retorna uma referencia para a entidade salva

        //converter novamente para ProductDTO
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getOne(id); //cria um objeto provisório, sem ir no banco de dados, e somente quando eu mandar salvar que ele fará a busca na base, justamente para não precisar acessar a base duas vezes

            //método para setar os dados do Product, para que assim não precise repetir código em outros métodos
            //o método já serve para que isso seja feito pensando no relacionamento entre as tabelas
            copyDtoToEntity(dto, entity);

            // entity.setName(dto.getName());
            entity = repository.save(entity);

            return new ProductDTO(entity);

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

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        // só não irá setar o ID
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUri());
        entity.setDate(dto.getDate());

        //setando as categorias, copiando as categorias do DTO para a ENTIDADE
        //para garantir que vai copiar as categorias que vieram no DTO, ai por isso limpanos a entidade como abaixo:
        entity.getCategories().clear(); //para limpar a entidade
        for(CategoryDTO catDto : dto.getCategories()) { //se chegou as categorias no ProductDTO eu percorro a lista para instanciar uma categoria
            Category category  = categoryRepository.getOne(catDto.getId()); //getOne para não acessar o BD, apenas salva em memória e posterior adiciona quando for necessário
            entity.getCategories().add(category);
        }

    }

}
