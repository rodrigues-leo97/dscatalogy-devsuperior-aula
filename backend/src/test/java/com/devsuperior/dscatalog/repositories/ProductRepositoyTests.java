package com.devsuperior.dscatalog.repositories; //usa os mesmos nomes de pacote das repositories ou qualquer outra classe para acessar os métodos private caso tenham

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest  //para testar somente a repositories, não carrega a parte dos controladores e services nem o contexto da aplicação
public class ProductRepositoyTests {
    //o teste será neste caso em cima dos dados que estão sendo usados para fazer o seed inicial da base de dados ao inicializar a aplicação

    @Autowired
    private ProductRepository repository;

    private long existingId; //foi inicializado dentro do beforeEach
    private long nonExistingId; //foi inicializado dentro do beforeEach
    private long countTotalProducts; //foi inicializado dentro do beforeEach

    @BeforeEach //executa antes de cada um dos testes
    void setUp() throws Exception {
        //inicializo as variáveis aqui dentro
            //Agora cada classe que precisar de um id existente para teste já temos instanciado aqui
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() { //salvar com autoincremento quando o id for nulo (quando estiver fazendo um insert)
        // Product product = new Product(); -> criar classe auxiliar para não precisar ficar instanciando toda hora
        Product product = Factory.createProduct();
        product.setId(null); //para garantir que estou inserindo um produto com id nulo
        product = repository.save(product);

        Assertions.assertNotNull(product.getId()); //testando se o id não é nulo
        Assertions.assertEquals(countTotalProducts + 1, product.getId()); //pois o próximo id do que tem na classe import.sql seria o 26 para acrescentar no banco
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() { //testar se o delete está realmente deletando
        //Long existingId = 1L; -> está instanciado dentro do beforeEach

        repository.deleteById(existingId); //deletando o Id de número 1

        Optional<Product> result = repository.findById(existingId); //testando para ver se meu id ainda existe ou não na base de dados
        Assertions.assertFalse(result.isPresent()); //testa para ver se existe um obj dentro do OPTIONAL, neste caso o assertFalse está esperando receber um booleano FALSE
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists() {
        //Long nonExistingId = 1000L; -> está instanciado dentro do beforeEach

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> { //exception que é lançada para quando não é encontrado o id buscado
            repository.deleteById(nonExistingId); //passar um id que não existe para lançar uma EXCEPTION
        });
    }

    @Test
    public void findByIdWhenIdExists() {
       Optional<Product> result =  repository.findById(existingId);
       Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdWhenIdNotExists() {
        Optional<Product> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());

    }

}
