package ro.fasttrackit.restaurantapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.restaurantapp.domain.Restaurant;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByName(String name);

    boolean existsByCity(String city);

    boolean existsByNameAndIdNot(String name, long id);

    boolean existsByCityAndIdNot(String city, long id);

    Page<Restaurant> findByCity(String city, Pageable pageable);

    Page<Restaurant> findByStarsIn(List<Integer> stars, Pageable pageable);
}
