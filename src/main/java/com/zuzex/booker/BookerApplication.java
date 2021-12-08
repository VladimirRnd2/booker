package com.zuzex.booker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BookerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookerApplication.class, args);
	}

}