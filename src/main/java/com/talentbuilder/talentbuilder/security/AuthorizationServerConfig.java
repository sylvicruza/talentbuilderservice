package com.talentbuilder.talentbuilder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@EnableResourceServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.jwt.client-id}")
    private String clientId;

    @Value("${security.jwt.client-secret}")
    private String clientSecret;

    @Value("${security.jwt.grant-type}")
    private String grantType;

    @Value("${security.jwt.scope-read}")
    private String scopeRead;

    @Value("${security.jwt.scope-write}")
    private String scopeWrite = "write";

    @Value("${security.jwt.resource-ids}")
    private String resourceIds;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.inMemory()
            .withClient(clientId)
            .secret(passwordEncoder.encode(clientSecret))
            .authorizedGrantTypes(grantType, "refresh_token")
            .scopes(scopeRead, scopeWrite)
            //.accessTokenValiditySeconds(864000) //10 days
            .resourceIds(resourceIds);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
            .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter, tokenEnhancer()));
        endpoints.tokenStore(tokenStore)
            .accessTokenConverter(accessTokenConverter)
            .tokenEnhancer(enhancerChain)
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService);
    }

    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenEnhancer(tokenEnhancer());
        return tokenServices;
    }
    
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new AuthorizationTokenEnhancer();
    }
}
