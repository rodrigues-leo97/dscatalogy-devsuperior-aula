package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest  //teste de INTEGRAÇÃO
@AutoConfigureMockMvc //TESTE DA CAMADA WEB COLOCA ISSO
@Transactional
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired //pode usar o autowired aqui por conta do ObjectMapper ser um obj auxiliar e não uma dependência
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId; //para quando formos ter violação de banco de dados
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L; //pq temos 25 produtos na base
    }

    @Test
    public void findAllShouldReturnSortedPagedWhenSortByName() throws Exception{ //testar se a página no Json está vindo ordenada quando eu pedir ordenada por NOME
        ResultActions result =
                mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON) //mediaType = essa requisição vai aceitar como resposta o tipo Json
                );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content").exists()); //testando se existe o CONTENT, pois é ele que armazena a lista de resposta
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
        //no objeto da resposta temos um atributo chamado totalElements, que trás a soma total de elementos
        result.andExpect(jsonPath("$.totalElements").value(countTotalProducts)); //JsonPath para acessar o objeto da resposta, e estou verificando se é igual a 25 no caso
    }

    @Test
    public void updateShouldReturnProductWhenIdExisting() throws Exception {
        //o PUT recebe um corpo na requisição que temos que passar para testar aqui
        //recebemos um objeto em JAVA neste caso por mockar, ai temos que converter para um OBJ JSON
        ProductDTO productDTO = Factory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO); //converti um objeto Java para um String

        String expectedName = productDTO.getName(); //antes de fazer a requisição SALVAR o nome, já que se trata de um teste real
        String expectedDescription = productDTO.getDescription();

        ResultActions result =
                mockMvc.perform(put("/products/{id}", existingId)
                        .content(jsonBody) //irá receber meu corpo da requisição já convertido
                        .contentType(MediaType.APPLICATION_JSON) //tenho que informar os tipos de dados da REQUSIÇÃO e não somente da RESPOSTA
                        .accept(MediaType.APPLICATION_JSON) //informando o tipo de DADOS da RESPOSTA
                );

        result.andExpect(status().isOk());

        //confirmando se os valores estão compatíveis com os valores atualizados, ou seja, vendo se está atualizado na base
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(expectedName)); //para saber se o nome casa com o nome esperado
        result.andExpect(jsonPath("$.description").value(expectedDescription));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExisting() throws Exception {
        //o PUT recebe um corpo na requisição que temos que passar para testar aqui
        //recebemos um objeto em JAVA neste caso por mockar, ai temos que converter para um OBJ JSON
        ProductDTO productDTO = Factory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO); //converti um objeto Java para um String

        String expectedName = productDTO.getName(); //antes de fazer a requisição SALVAR o nome, já que se trata de um teste real
        String expectedDescription = productDTO.getDescription();

        ResultActions result =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody) //irá receber meu corpo da requisição já convertido
                        .contentType(MediaType.APPLICATION_JSON) //tenho que informar os tipos de dados da REQUSIÇÃO e não somente da RESPOSTA
                        .accept(MediaType.APPLICATION_JSON) //informando o tipo de DADOS da RESPOSTA
                );

        result.andExpect(status().isNotFound());
    }

}
