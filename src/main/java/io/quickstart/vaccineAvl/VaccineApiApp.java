package io.quickstart.vaccineAvl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.quickstart.service.VaccineService;

@SpringBootApplication
public class VaccineApiApp {

	public static void main(String[] args) {
		SpringApplication.run(VaccineApiApp.class, args);

	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
    
	@Bean
	public VaccineService getVaccineService() {
		return new VaccineService();
	}
}
