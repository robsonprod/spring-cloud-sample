package com.atlantico.desafio.users;

import com.atlantico.desafio.persistence.EnablePostgres;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableDiscoveryClient
@EnablePostgres
@RefreshScope
public class UsersApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
    }
}
