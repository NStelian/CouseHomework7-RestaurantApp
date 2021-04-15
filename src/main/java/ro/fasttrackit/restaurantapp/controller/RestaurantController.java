package ro.fasttrackit.restaurantapp.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.restaurantapp.domain.Restaurant;
import ro.fasttrackit.restaurantapp.exceptions.ResourceNotFoundException;
import ro.fasttrackit.restaurantapp.model.RestaurantFilters;
import ro.fasttrackit.restaurantapp.service.CollectionResponse;
import ro.fasttrackit.restaurantapp.service.PageInfo;
import ro.fasttrackit.restaurantapp.service.RestaurantService;

@RestController
@RequestMapping("restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService service;

    @GetMapping
    CollectionResponse<Restaurant> getAll(
            RestaurantFilters filters,
            Pageable pageable) {
        System.out.println(filters);
        Page<Restaurant> productPage = service.getAll(filters, pageable);
        return CollectionResponse.<Restaurant>builder()
                .content(productPage.getContent())
                .pageInfo(PageInfo.builder()
                        .totalPages(productPage.getTotalPages())
                        .totalElements(productPage.getNumberOfElements())
                        .crtPage(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .build())
                .build();
    }

    @GetMapping("{restaurantId}")
    Restaurant getRestaurantById(@PathVariable Long restaurantId) {
        return service.getRestaurantById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find restaurant with id " + restaurantId));
    }

    @PostMapping
    Restaurant createRestaurant(@RequestBody Restaurant newRestaurant) {
        return service.createRestaurant(newRestaurant);
    }

    @PutMapping("{restaurantId}")
    Restaurant replaceRestaurant(@RequestBody Restaurant newRestaurant, @PathVariable Long restaurantId) {
        return service.replaceRestaurant(newRestaurant, restaurantId);
    }

    @DeleteMapping("{restaurantId}")
    void deleteRestaurantById(@PathVariable Long restaurantId) {
        service.deleteRestaurantById(restaurantId);
    }

    @PatchMapping("{restaurantId}")
    Restaurant patchRestaurant(@RequestBody JsonPatch patch, @PathVariable long restaurantId) {
        return service.patchRestaurant(patch, restaurantId);
    }
}
