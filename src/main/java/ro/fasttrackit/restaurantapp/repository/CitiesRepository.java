package ro.fasttrackit.restaurantapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.restaurantapp.domain.City;

@Repository
public interface CitiesRepository extends JpaRepository<City, Long> {
    boolean existsByCity(String city);
}
