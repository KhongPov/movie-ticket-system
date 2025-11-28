package com.movieticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MovieTicketSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTicketSystemApplication.class, args);
    }
}
