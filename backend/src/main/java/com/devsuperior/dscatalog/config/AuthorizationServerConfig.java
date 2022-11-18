package com.devsuperior.dscatalog.config;

import com.devsuperior.dscatalog.components.JwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

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

    //com essa anotação ele pega o valor da variável que está definido no application.properties
    @Value("${jwt.duration}")
    private int jwtDuration;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    private JwtTokenEnhancer tokenEnhancer;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId) //esperando que diga o nome da aplicação(id dela), na aplicação ao acessar terá que informar o nome
                .secret(bCryptPasswordEncoder.encode(clientSecret)) //para definir o clientSecret em hardcode mesmo
                .scopes("read", "write") //para informar que é um acesso de leitura e escrita
                .authorizedGrantTypes("password")//os tipos de acesso de login, nesse caso é password
                .accessTokenValiditySeconds(jwtDuration); //para informar o tempo de expiração do token, no caso 24h
    }

    //configuração para saber quem vai autorizar e qual o formato do token
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(Arrays.asList(accessTokenConverter, tokenEnhancer)); //esse argumento espera uma lista

        endpoints.authenticationManager(authenticationManager) //nesse caso será o authentication que processará a autenticação
                .tokenStore(tokenStore) //objetos responsáveis por processar o TOKEN
                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(chain); //irá acrescentar informações dentro do CICLO do token
    }
}
