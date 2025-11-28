package com.movieticket.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;  // Correct for Spring Boot 3+
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @PostConstruct
    public void init() {
//        Stripe.apiKey = stripeApiKey;
    }
}