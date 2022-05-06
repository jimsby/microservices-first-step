package com.example.resourcep.oauth;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/** Handles OAuth2 client credentials security flow using Spring Security 5x. */
@Configuration
public class OAuth2Config {
    /**
     * creates AuthManager in spring context for OAuth token management in InMemory cache.
     *
     * @param clientRegistrationRepository - repo to retrieve auto configured registrations in spring
     *     context.
     * @param authorizedClientService - service to fetch & refresh auth token in memory.
     * @return AuthorizedClientManager
     */
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            final ClientRegistrationRepository clientRegistrationRepository,
            final OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientService);
    }
}

