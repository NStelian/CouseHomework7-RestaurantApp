package ro.fasttrackit.restaurantapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ro.fasttrackit.restaurantapp.domain.Restaurant;
import ro.fasttrackit.restaurantapp.exceptions.ValidationException;
import ro.fasttrackit.restaurantapp.repository.CitiesRepository;
import ro.fasttrackit.restaurantapp.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Optional.empty;

@Component
@RequiredArgsConstructor
public class RestaurantValidator {
    private final RestaurantRepository repository;
    private final CitiesRepository citiesRepository;
    private final Environment env;

    public void validateNewThrow(Restaurant newRestaurant) {
        validate(newRestaurant, true)
                .ifPresent(ex -> {
                    throw ex;
                });
    }

    public void validateReplaceThrow(long restaurantId, Restaurant newRestaurant) {
        exists(restaurantId)
                .or(() -> validate(newRestaurant, false))
                .ifPresent(ex -> {
                    throw ex;
                });
    }

    public void validateExistsOrThrow(long restaurantId) {
        exists(restaurantId).ifPresent(ex -> {
            throw ex;
        });
    }

    private Optional<ValidationException> validate(Restaurant restaurant, boolean newEntity) {
        if (restaurant.getName() == null) {
            return Optional.of(new ValidationException("Name cannot be null"));
        } else if (restaurant.getCity() == null) {
            return Optional.of(new ValidationException("City cannot be null"));
        }  else if (!citiesRepository.existsByCity(restaurant.getCity())) {
            return Optional.of(new ValidationException("City must be from " + Arrays.toString(env.getActiveProfiles())));
        } else if (restaurant.getStars() <=0 ) {
            return Optional.of(new ValidationException("Stars should not be lower than 1"));
        } else if (restaurant.getStars() >= 5) {
            return Optional.of(new ValidationException("Stars should not be higher than 5"));
        } else if (restaurant.getSince().isAfter(LocalDate.now())) {
            return Optional.of(new ValidationException("Since Date should be before than today" ));
        } else if (newEntity && repository.existsByName(restaurant.getName()) && repository.existsByCity(restaurant.getCity())) {
            return Optional.of(new ValidationException("Name cannot be duplicate in the same City"));
        } else if (!newEntity && repository.existsByNameAndIdNot(restaurant.getName(), restaurant.getId()) && repository.existsByCityAndIdNot(restaurant.getCity(), restaurant.getId())) {
            return Optional.of(new ValidationException("Name cannot be duplicate in the same City"));
        } else {
            return empty();
        }
    }

    private Optional<ValidationException> exists(long restaurantId) {
        return repository.existsById(restaurantId)
                ? empty()
                : Optional.of(new ValidationException("Restaurant with id " + restaurantId + " doesn't exist"));
    }
}
