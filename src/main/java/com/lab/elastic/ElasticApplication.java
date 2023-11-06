package com.lab.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ElasticApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticApplication.class, args);
	}

}
