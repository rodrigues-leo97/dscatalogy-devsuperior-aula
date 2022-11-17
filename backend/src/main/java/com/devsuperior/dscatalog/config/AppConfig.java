package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration //por se tratar de uma classe de CONFIGURAÇÃO
public class AppConfig {

    @Bean //é uma annotation de MÉTODO, e dizendo que está instância é um componente gerenciado pelo SPRING e posso instancialo em outro componente
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //OS DOIS BEANS ABAIXO SÃO OBJETOS CAPAZES DE ACESSAR O TOKEN JWT, OU SEJA, LER, CRIAR OU DECODIFICAR UM TOKEN
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey("MY-JWT-SECRET"); //por enquanto a assinatura irá ficar hardcode aqui, depois iremos colocar ela no PROPERTIES(Serve para registrar a chave do token)
        return tokenConverter;
    }

    @Bean
    public JwtTokenStore tokenStore() { //serve para acessar o token também, e passa como argumento o método ácima
        return new JwtTokenStore(accessTokenConverter());
    }

}
