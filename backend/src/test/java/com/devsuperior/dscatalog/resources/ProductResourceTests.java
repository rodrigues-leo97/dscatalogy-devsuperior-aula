package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.net.URI;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductResource.class)
public class ProductResourceTests { //testes na CAMADA DE WEB(TESTE DE UNIDADE) -> carrega o contexto, porém, somente da camada web(teste unidade: CONTROLLER)

    //vamos chamar os ENDPOINTS, testar as REQUISIÇÕES
    @Autowired
    private MockMvc mockMvc;

    //como tem uma depêndencia com o ProductService temos que declara-lo aqui
    @MockBean //MockBean tem diferença do Mockito(@Mock), neste caso se usa esse para quando a classe é @WebMvcTest ou @SpringBootTest
    private ProductService service;

    @Autowired //pode usar o autowired aqui por conta do ObjectMapper ser um obj auxiliar e não uma dependência
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId; //para quando formos ter violação de banco de dados
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page; //Instanciando um obj concreto IMPL e por conta disso ele passa a aceitar o NEW(instanciando)

    @BeforeEach
    void setUp() throws Exception {//executa antes de cada um dos testes
        //SIMULAR O COMPORTAMENTO DO SERVICE
        //simulando comportamento do FINDALLPAGED do service que é chamado na resource
        productDTO = Factory.createProductDTO();
        existingId = 1L;
        dependentId = 3L;
        nonExistingId = 1000L;
        page = new PageImpl<>(List.of(productDTO)); //o of permite que eu instancie uma lista já com um elemento dentro dela

        when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page); //o valor que o findAllPaged recebe de parametro não importa para essa finalidade, por isso se passa um ANY
        //quando eu chamar o findAllPaged com QUALQUER argumento(ANY) eu vou retornar um objeto page do tipo PageImpl<ProductDTO>

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //usa o eq() pois só com o ANY e o tipo de id passado ele irá acusar erro
        when(service.update(eq(existingId), any())).thenReturn(productDTO); //any() para simular o comportamento de qualquer obj, e não precisar passar o productDTO
        when(service.update(eq(nonExistingId), any())).thenThrow(EntityNotFoundException.class);

        //delete é um método VOID, e quando é VOID se muda a sequência
        doNothing().when(service).delete(existingId); //não faça nada quando encontrar o ID existente para deletar
        doThrow(EntityNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DataBaseException.class).when(service).delete(dependentId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON) //mediaType = essa requisição vai aceitar como resposta o tipo Json
                );

        //dividir e colocar em uma variável como está acima, para fazer em baixo como se fosse a ASSERTIONS
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExisting() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products/{id}", existingId) //fazendo um binding com o id desejado para realizar a busca
                        .accept(MediaType.APPLICATION_JSON) //mediaType = essa requisição vai aceitar como resposta o tipo Json
                );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists()); //jsonPath ele analisa o corpo da resposta e verificar se tem os dados retornados do JSON, o $ acessa o OBJETO DA RESPOSTA (o JSON DE RETORNO), ou seja, verificando se o id existe no corpo da resposta
        result.andExpect(jsonPath("$.name").exists()); //testando se o nome existe
        result.andExpect(jsonPath("$.description").exists()); //testando se a description existe
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExisting() throws Exception {
        ResultActions result =
            mockMvc.perform(get("/products/{id}", nonExistingId) //fazendo um binding com o id desejado para realizar a busca
                    .accept(MediaType.APPLICATION_JSON) //mediaType = essa requisição vai aceitar como resposta o tipo Json
            );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductWhenIdExisting() throws Exception {
        //o PUT recebe um corpo na requisição que temos que passar para testar aqui
        //recebemos um objeto em JAVA neste caso por mockar, ai temos que converter para um OBJ JSON
        String jsonBody = objectMapper.writeValueAsString(productDTO); //converti um objeto Java para um String

        ResultActions result =
                mockMvc.perform(put("/products/{id}", existingId)
                        .content(jsonBody) //irá receber meu corpo da requisição já convertido
                        .contentType(MediaType.APPLICATION_JSON) //tenho que informar os tipos de dados da REQUSIÇÃO e não somente da RESPOSTA
                        .accept(MediaType.APPLICATION_JSON) //informando o tipo de DADOS da RESPOSTA
                );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateIdShouldReturnNotFoundWhenIdDoesNotExisting() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions result =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() throws Exception{
        ResultActions result =
                mockMvc.perform(delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isNoContent());

    }

    @Test
    public void deleteIdShouldReturnNotFoundWhenIdDoesNotExisting() throws Exception{
        ResultActions result =
                mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
                );

        result.andExpect(status().isNotFound());

    }

}

