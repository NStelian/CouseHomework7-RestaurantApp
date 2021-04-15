package ro.fasttrackit.restaurantapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.fasttrackit.restaurantapp.domain.Restaurant;
import ro.fasttrackit.restaurantapp.exceptions.ResourceNotFoundException;
import ro.fasttrackit.restaurantapp.model.RestaurantFilters;
import ro.fasttrackit.restaurantapp.repository.RestaurantRepository;

import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;
    private final RestaurantValidator validator;
    private final ObjectMapper mapper;

    public Page<Restaurant> getAll(RestaurantFilters filters, Pageable pageable) {
        if (filters.getCity() != null) {
            return repository.findByCity(filters.getCity(), pageable);
        } else if(!isEmpty(filters.getStars())){
            return repository.findByStarsIn(filters.getStars(), pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    public Optional<Restaurant> getRestaurantById(Long restaurantId) {
        return repository.findById(restaurantId);
    }

    public Restaurant createRestaurant(Restaurant newRestaurant) {
        validator.validateNewThrow(newRestaurant);

        return repository.save(newRestaurant);
    }

    public void deleteRestaurantById(Long restaurantId) {
        repository.deleteById(restaurantId);
    }

    public Restaurant replaceRestaurant(Restaurant newRestaurant, Long restaurantId) {
        newRestaurant.setId(restaurantId);
        validator.validateReplaceThrow(restaurantId, newRestaurant);

        Restaurant dbRestaurant = repository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find restaurant with id " + restaurantId));
        copyRestaurant(newRestaurant, dbRestaurant);
        return repository.save(dbRestaurant);
    }

    private void copyRestaurant(Restaurant newRestaurant, Restaurant dbRestaurant) {
        dbRestaurant.setName(newRestaurant.getName());
        dbRestaurant.setStars(newRestaurant.getStars());
        dbRestaurant.setCity(newRestaurant.getCity());
        dbRestaurant.setSince(newRestaurant.getSince());
    }

    @SneakyThrows
    public Restaurant patchRestaurant(JsonPatch patch, long restaurantId) {
        validator.validateExistsOrThrow(restaurantId);
        Restaurant dbRestaurant = repository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find restaurant with id " + restaurantId));

        JsonNode patchedRestaurantJson = patch.apply(mapper.valueToTree(dbRestaurant));
        Restaurant patchedRestaurant = mapper.treeToValue(patchedRestaurantJson, Restaurant.class);
        return replaceRestaurant(patchedRestaurant, restaurantId);
    }
}
