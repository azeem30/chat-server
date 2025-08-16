package com.example.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
        System.setProperty("INIT_VECTOR", dotenv.get("INIT_VECTOR"));
        SpringApplication.run(ServerApplication.class, args);
    }

}
