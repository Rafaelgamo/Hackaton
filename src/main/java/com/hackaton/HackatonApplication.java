package com.hackaton;

import com.hackaton.configuration.DocumentedAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class  HackatonApplication implements DocumentedAPI {

	public static void main(String[] args) {
		SpringApplication.run(HackatonApplication.class, args);
	}

}
