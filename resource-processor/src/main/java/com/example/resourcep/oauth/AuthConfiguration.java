package com.example.resourcep.oauth;


import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthConfiguration {

    private final OAuth2Provider oauth2Provider;
    private final String AUTHZ_SERVER_NAME = "keycloak";

    @Bean
    public RequestInterceptor barAuthZInterceptor() {
        return (requestTemplate) ->
                requestTemplate.header(
                        HttpHeaders.AUTHORIZATION, oauth2Provider.getAuthenticationToken(AUTHZ_SERVER_NAME));
    }
}
