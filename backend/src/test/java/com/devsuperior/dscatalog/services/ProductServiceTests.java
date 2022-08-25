package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class) //testes de UNIDADE -> testa somente a classe específica sem carregar outro componente do qual ela depende
public class ProductServiceTests {
    //MOCKITO PARA MOCKAR AS DEPENDÊNCIAS
    //TESTES DE UNIDADE NÃO ACESSAM O REPOSITORY QUE POSTERIOR ACESSAM O BANCO

    //NÃO SE USA O AUTOWIRED, não pode injeta-los ai tem que simular o comportamento deles em um obj mockado, ex: @Mock
    @InjectMocks
    private ProductService service;

    @Mock //se usa ele quando a classe é ExtendWith, pois não carrega o contexto da aplicação
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private PageImpl<Product> page; //PageImpl -> Tipo concreto que representa uma página de dados para ser usada nos testes
    private Product product;
    private Category category;

    @BeforeEach //executa antes de cada um dos testes
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 4L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        category = Factory.createCategory();

        //CONFIGURANDO comportamentos simulado chamando mockito para o OBJETO MOCKADO REPOSITORY
        //when -> para chamar algum método no obj mockado e falar oq esse método DEVERIA RETORNAR
        //como o delete não retorna nada por ser VOID, temos que chamar o doNothing(não fará nada)
        Mockito.doNothing().when(repository).deleteById(existingId); //não vai retornar exception e nem nada quando o id exist
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId); //retornará uma exception para quando o id não existir na base de dados
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId); //quando o id tem uma identidade que depende dele, oq gera a violação de integridade

        //para quando um método não é VOID, ou seja, quando tem um retorno o WHEN vem antes da ação(doNothing, doThrow)
        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);  //chamando o findAll passando qualquer valor(ArgumentMatcher.anu()) do tipo Pageable, terá que retornar o page
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product)); //retorna um optional de Product NÃO VAZIO
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty()); //retorna um optional de Product VAZIO
        Mockito.when(repository.getOne(existingId)).thenReturn(product);  //simulando comportamento do getOne quando o id é existente

        //Mockito.doThrow(javax.persistence.EntityNotFoundException.class).when(repository).getOne(nonExistingId);  //simulando comportamento do getOne quando o id não existe
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(javax.persistence.EntityNotFoundException.class); //simulando comportamento do getOne quando o id não existe

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);  //simulando comportamento do getOne quando a id da Categoria é existente, para isso tem que INSTANCIAR também uma CATEGORY
        Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(javax.persistence.EntityNotFoundException.class); //simulando comportamento do getOne quando a id da Categoria não existe
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {  //não lança nenhuma exception
            service.delete(existingId); //o deleteById que será chamado será o do mockito, que foi configurado no beforeEach
        });

        //verifica se o método deleteById foi chamado na ação colocada no teste
        //Mockito.times() -> quantidade de vezes que espero que esse método tenha sido chamado, da para passar mais de um param pois esse método aceita algumas sobrecargas
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId); //o argumento é o mock(repository)
        // OBS -> Mockito.never() -> para que não seja chamado nenhuma vez
    }

    @Test
    public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionIdDependentId() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            service.delete(dependentId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPage() { //deverá retornar uma página
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result); //tem que retornar TRUE

        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExist() {
        // ele irá chamar a simulação da minha chamada -> Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product)); //retorna um optional de Product NÃO VAZIO e NÃO O REPOSITORY REAL
        ProductDTO productDTO = service.findById(existingId);
        Assertions.assertNotNull(productDTO); //terá que retornar não null

        Mockito.verify(repository, Mockito.times(1)).findById(existingId);

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesExists() {
        //Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty()); //ele CHAMA ESSE COMPORTAMENTO SIMULADO
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
       Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist() {
        //posso passar por aqui ou posso passar lá no Before
        ProductDTO productDTO = Factory.createProductDTO(); //instanciei aqui mas poderia ter instanciado no beforeEach
        ProductDTO result = service.update(existingId, productDTO);

        Assertions.assertNotNull(result); //terá que retornar não null

        Mockito.verify(repository, Mockito.times(1)).getOne(existingId);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesExists() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            ProductDTO productDTO = Factory.createProductDTO();
            service.update(nonExistingId, productDTO);
        });
        Mockito.verify(repository, Mockito.times(1)).getOne(nonExistingId);
    }

}
