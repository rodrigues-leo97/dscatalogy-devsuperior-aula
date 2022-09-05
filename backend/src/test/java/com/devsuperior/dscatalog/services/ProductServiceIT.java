package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest //para carregar o contexto da aplicação, esse teste demora mais
@Transactional //após cada teste ele dará um rollBack no banco para que volte ao estado original OBS: tem que ser do SPRINGFRAMEWORK o import
public class ProductServiceIT { //Classe para testar a integração e comportamento entre outros componentes
    //por estar na service e querer que ele converse de verdade com a REPOSITORY não iremos mockar o REPOSITORY
    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts; //total de produtos no banco, pois temos a classe para saber quantos tem

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L; //pq temos 25 produtos na base
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);

        //para testar se deletou mesmo, podemos pegar a quantidade(countTotalProducts) - 1, para saber se virou 24 produtos
        //valor esperado(countTotalProduct - 1) / valor real repository.count() -> count retorna a quantidade total de registro no banco
        Assertions.assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPagedWhenPage0Size10() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty()); //para saber se não está vazia a página, ou seja, tem que retornar um false
        Assertions.assertEquals(0, result.getNumber()); //valor esperado é a página zero, o getNumber serve para pegar o número da página
        Assertions.assertEquals(10, result.getSize()); //testando se o tamanho da página é realmente 10

        //OBS: se rodar todos os testes de uma vez esse método assim dará erro, pq a função de cima deletou um elemento da base
        //para isso fazer com que cada teste seja TRANSACIONAL, ou seja, após cada teste ele dará um rollBack no banco para que volte ao estado original
        Assertions.assertEquals(countTotalProducts, result.getTotalElements()); //para saber se tem 25 elementos no banco
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageDoesNotExisting() { //quando a página não existir deverá retornar uma página vazia
        PageRequest pageRequest = PageRequest.of(50, 10);
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    //testar se os dados estão realmente ordenados quando eu mando buscar ordenado
    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName() { //quando a página não existir deverá retornar uma página vazia
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name")); //para trazer ordenado pelo nome, por isso o Sort.by
        Page<ProductDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());

        //testar os primeiros três produtos para ver se estão ordenados, mas pode testar mais produtos
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

}
