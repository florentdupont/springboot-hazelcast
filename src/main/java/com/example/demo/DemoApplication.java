package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class DemoApplication implements CommandLineRunner {

	@Autowired
	CountryService countryService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		//hazelcastInstance.toString();
		System.out.println("Hello World");


		countryService.findAllCountries();

		countryService.creerCountry(Country.builder().code("XX").label("Whatever").build());

		countryService.findAllCountries();

		countryService.creerCountry(Country.builder().code("00").label("Whatever 2").build());

		Country fr = countryService.getCountry("FR");

		countryService.findAllCountries();

		countryService.deleteCountry(fr);

		countryService.findAllCountries();


		countryService.getCountry("FR");

	}
}
