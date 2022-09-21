package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //por se tratar de uma classe de configuração
public class AppConfig {

    @Bean //é uma annotation de MÉTODO, e dizendo que está instância é um componente gerenciado pelo SPRING e posso instancialo em outro componente
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
