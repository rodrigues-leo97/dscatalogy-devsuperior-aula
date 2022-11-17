package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer //serve para realizar processamento informando que essa classe é quem representa o AuthorizationServe do OAuth
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private JwtTokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("dscatalog") //esperando que diga o nome da aplicação(id dela), na aplicação ao acessar terá que informar o nome
                .secret(bCryptPasswordEncoder.encode("dscatalog123")) //para definir o clientSecret em hardcode mesmo
                .scopes("read", "write") //para informar que é um acesso de leitura e escrita
                .authorizedGrantTypes("password")//os tipos de acesso de login, nesse caso é password
                .accessTokenValiditySeconds(86400); //para informar o tempo de expiração do token, no caso 24h
    }

    //configuração para saber quem vai autorizar e qual o formato do token
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) //nesse caso será o authentication que processará a autenticação
                .tokenStore(tokenStore) //objetos responsáveis por processar o TOKEN
                .accessTokenConverter(accessTokenConverter); //
    }
}
