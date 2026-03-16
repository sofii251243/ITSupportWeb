package com.sofia.itsupport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;


@SpringBootApplication
public class ItSupportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItSupportApplication.class, args);
    }

}
