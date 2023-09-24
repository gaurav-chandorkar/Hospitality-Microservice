package abc.userservice.restaurantservice.controller;

import abc.userservice.restaurantservice.domain.Restaurant;
import abc.userservice.restaurantservice.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/restaurant")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Restaurant> saveRestaurant(@RequestBody @Valid Restaurant restaurant) {
        log.info("adding new product");
        return restaurantService.addRestaurant(restaurant).log();
    }

    @GetMapping("/restaurant")
    public Flux<Restaurant> getAllRestaurant(@RequestParam(value = "city", required = false) String city,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "stars", required = false) Integer stars) {
        if (city != null) {
            log.info("get restaurants by city");
            return restaurantService.getRestaurantsByCity(city).log();
        } else if (name != null) {
            log.info("get restaurants by name");
            return restaurantService.getRestaurantsByName(name).log();
        } else if (stars != null) {
            log.info("get restaurants by stars");
            return restaurantService.getRestaurantsByStars(stars).log();
        }
        log.info("get all restaurants");

        return restaurantService.getAllRestaurants().log();
    }


    @GetMapping("/restaurant/{id}")
    public Mono<ResponseEntity<Restaurant>> findRestaurantById(@PathVariable String id) {
        return restaurantService.getRestaurantById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping("/restaurant/{id}")
    public Mono<ResponseEntity<Restaurant>> updateRestaurantById(@PathVariable String id,
                                                                 @RequestBody Restaurant updatedRestaurant) {
        return restaurantService.updateRestaurantById(id, updatedRestaurant)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/restaurant/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable String id) {
        if (id.equals("1")) {
            log.info("Delete all restaurants");
            return restaurantService.deleteAll().log();
        }
        return restaurantService.deleteRestaurantById(id).log();
    }
}

