package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.BadRequestException;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        //Product product = new Product();
        Page<Product> list = repository.findAll(pageRequest);
        return list
                .map(x -> new ProductDTO(x)); //Page já é do tipo Stream()
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
        //converter para o tipo Categoy(entidade)
        Product entity = new Product();

        List<Product> list = repository.findAll();

        for(Product product : list) {
            if(dto.getName().equalsIgnoreCase(product.getName())) {
                throw new BadRequestException("There is already a Product with that name registered"); //JÁ EXISTE CATEGORIA REGISTRADA COM ESSE NOME
            }
        }

        //se não existir nome repetido então ele seta o nome na entity
        // entity.setName(dto.getName());

        entity = repository.save(entity); //por padrão retorna uma referencia para a entidade salva

        //converter novamente para ProductDTO
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getOne(id); //cria um objeto provisório, sem ir no banco de dados, e somente quando eu mandar salvar que ele fará a busca na base, justamente para não precisar acessar a base duas vezes

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

}
