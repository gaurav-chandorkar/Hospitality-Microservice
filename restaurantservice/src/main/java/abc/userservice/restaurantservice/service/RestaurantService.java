package abc.userservice.restaurantservice.service;

import abc.userservice.restaurantservice.domain.Restaurant;
import abc.userservice.restaurantservice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public Mono<Restaurant> addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Flux<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Mono<Void> deleteRestaurantById(String id) {
        return restaurantRepository.deleteById(id);
    }

    public Flux<Restaurant> getRestaurantsByCity(String city) {
        return restaurantRepository.findByCity(city);
    }

    public Flux<Restaurant> getRestaurantsByName(String name) {
        return restaurantRepository.findByName(name);
    }

    public Flux<Restaurant> getRestaurantsByStars(Integer stars) {
        return restaurantRepository.findByStars(stars);
    }

    public Mono<Restaurant> getRestaurantById(String id) {
        return restaurantRepository.findById(id);
    }

    public Mono<Restaurant> updateRestaurantById(String id, Restaurant updatedRestaurant) {
        var restaurantDbEntity = restaurantRepository.findById(id);

        return restaurantDbEntity.flatMap(restaurant -> {
            restaurant.setCity(updatedRestaurant.getCity());
            restaurant.setName(updatedRestaurant.getName());
            restaurant.setStars(updatedRestaurant.getStars());
            restaurant.setTables(updatedRestaurant.getTables());
            return restaurantRepository.save(restaurant);
        });
    }

    public Mono<Void> deleteAll() {
        return restaurantRepository.deleteAll();
    }
}
