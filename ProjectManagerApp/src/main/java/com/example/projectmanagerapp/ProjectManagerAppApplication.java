package com.example.projectmanagerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EntityScan("org.example.projectmanagerapp.entity")
@EnableJpaRepositories("org.example.projectmanagerapp.repository")
public class ProjectManagerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args);
    }

}
