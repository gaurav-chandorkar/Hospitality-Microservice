package abc.userservice.restaurantservice.repository;

import abc.userservice.restaurantservice.domain.Restaurant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


public interface RestaurantRepository extends ReactiveMongoRepository<Restaurant, String> {
   Flux<Restaurant> findByName(String name);

   Flux<Restaurant> findByStars(int stars);

   Flux<Restaurant> findByCity(String city);

}
