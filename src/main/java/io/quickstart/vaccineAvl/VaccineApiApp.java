package io.quickstart.vaccineAvl;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import io.quickstart.service.EmailService;
import io.quickstart.service.VaccineService;

@SpringBootApplication
@EnableScheduling
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
	
	@Bean
	public EmailService getEmailService() {
		return new EmailService();
	}
}
