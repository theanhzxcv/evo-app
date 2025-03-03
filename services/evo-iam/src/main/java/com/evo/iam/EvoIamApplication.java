package com.evo.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@SpringBootApplication
public class EvoIamApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvoIamApplication.class, args);
	}

}
