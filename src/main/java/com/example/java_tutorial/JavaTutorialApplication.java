package com.example.java_tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JavaTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaTutorialApplication.class, args);
	}

}
