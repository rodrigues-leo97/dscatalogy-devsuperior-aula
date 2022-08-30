package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.net.URI;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductResource.class)
public class ProductResourceTests { //testes na CAMADA DE WEB(TESTE DE UNIDADE) -> carrega o contexto, porém, somente da camada web(teste unidade: CONTROLLER)

    //vamos chamar os ENDPOINTS, testar as REQUISIÇÕES
    @Autowired
    private MockMvc mockMvc;

    //como tem uma depêndencia com o ProductService temos que declara-lo aqui
    @MockBean //MockBean tem diferença do Mockito(@Mock), neste caso se usa esse para quando a classe é @WebMvcTest ou @SpringBootTest
    private ProductService service;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page; //Instanciando um obj concreto IMPL e por conta disso ele passa a aceitar o NEW(instanciando)

    @BeforeEach
    void setUp() throws Exception {//executa antes de cada um dos testes
        //SIMULAR O COMPORTAMENTO DO SERVICE
        //simulando comportamento do FINDALLPAGED do service que é chamado na resource
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO)); //o of permite que eu instancie uma lista já com um elemento dentro dela

        when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page); //o valor que o findAllPaged recebe de parametro não importa para essa finalidade, por isso se passa um ANY
        //quando eu chamar o findAllPaged com QUALQUER argumento(ANY) eu vou retornar um objeto page do tipo PageImpl<ProductDTO>
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
}

