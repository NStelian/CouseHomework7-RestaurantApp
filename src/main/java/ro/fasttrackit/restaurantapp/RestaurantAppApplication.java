package ro.fasttrackit.restaurantapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ro.fasttrackit.restaurantapp.domain.City;
import ro.fasttrackit.restaurantapp.domain.Restaurant;
import ro.fasttrackit.restaurantapp.repository.CitiesRepository;
import ro.fasttrackit.restaurantapp.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class RestaurantAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantAppApplication.class, args);
	}

	@Profile("Romania")
	@Bean
	CommandLineRunner atStartup(RestaurantRepository repository, CitiesRepository citiesRepository) {
		citiesRepository.saveAll(List.of(new City("Oradea"), new City("Timisoara")));
		return args -> {
			repository.saveAll(List.of(
					new Restaurant("Old mill", 4, "Oradea", LocalDate.now()),
					new Restaurant("Astoria", 5, "Oradea", LocalDate.now())
			));
		};
	}

	@Profile("Ungaria")
	@Bean
	CommandLineRunner atStartup2(RestaurantRepository repository, CitiesRepository citiesRepository) {
		citiesRepository.saveAll(List.of(new City("Nagyvárad"), new City("Hajdu")));
		return args -> {
			repository.saveAll(List.of(
					new Restaurant("Old mill", 4, "Nagyvárad", LocalDate.now()),
					new Restaurant("Astoria", 5, "Nagyvárad", LocalDate.now())
			));
		};
	}
}
