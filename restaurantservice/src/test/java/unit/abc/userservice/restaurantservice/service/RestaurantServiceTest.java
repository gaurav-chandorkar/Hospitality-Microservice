package abc.userservice.restaurantservice.service;

import abc.userservice.restaurantservice.domain.Restaurant;
import abc.userservice.restaurantservice.domain.Table;
import abc.userservice.restaurantservice.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @MockBean
    RestaurantRepository restaurantRepository;

    @Test
    void addRestaurant() {

        var restaurant = getRestaurant();
        Mockito.when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));
        var restaurantMono = restaurantService.addRestaurant(restaurant).log();
        Assertions.assertNotNull(restaurantMono);
        StepVerifier.create(restaurantMono)
                .expectNext(restaurant)
                .verifyComplete();
    }

    @Test
    void getAllRestaurants() {
        Mockito.when(restaurantRepository.findAll()).thenReturn(Flux.just(getRestaurant()));
        var restaurantFlux = restaurantService.getAllRestaurants();

        Assertions.assertNotNull(restaurantFlux);
        StepVerifier.create(restaurantFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void deleteRestaurantById() {
        String id = "1";
        Mockito.when(restaurantRepository.deleteById(id)).thenReturn(Mono.empty());
        var userMono = restaurantService.deleteRestaurantById(id).log();
        Assertions.assertNotNull(userMono);
        StepVerifier.create(userMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void getRestaurantsByCity() {
        var city = "Pune";
        var restaurant = getRestaurant();
        Mockito.when(restaurantRepository.findByCity(city)).thenReturn(Flux.just(restaurant));
        var restaurantFlux = restaurantService.getRestaurantsByCity(city).log();

        Assertions.assertNotNull(restaurantFlux);
        StepVerifier.create(restaurantFlux)
                .expectNext(restaurant)
                .verifyComplete();
    }

    @Test
    void getRestaurantsByName() {
        var name = "Welcome";
        var restaurant = getRestaurant();
        Mockito.when(restaurantRepository.findByName(name)).thenReturn(Flux.just(restaurant));
        var restaurantFlux = restaurantService.getRestaurantsByName(name).log();

        Assertions.assertNotNull(restaurantFlux);
        StepVerifier.create(restaurantFlux)
                .expectNext(restaurant)
                .verifyComplete();
    }

    @Test
    void getRestaurantsByStars() {
        var stars = 4;
        var restaurant = getRestaurant();
        Mockito.when(restaurantRepository.findByStars(stars)).thenReturn(Flux.just(restaurant));
        var restaurantFlux = restaurantService.getRestaurantsByStars(stars).log();

        Assertions.assertNotNull(restaurantFlux);
        StepVerifier.create(restaurantFlux)
                .expectNext(restaurant)
                .verifyComplete();
    }

    @Test
    void getRestaurantById() {
        var id = "1";
        var restaurant = getRestaurant();
        Mockito.when(restaurantRepository.findById(id)).thenReturn(Mono.just(restaurant));
        var restaurantFlux = restaurantService.getRestaurantById(id).log();

        Assertions.assertNotNull(restaurantFlux);
        StepVerifier.create(restaurantFlux)
                .expectNext(restaurant)
                .verifyComplete();
    }

    @Test
    void updateRestaurantById() {
        final var id = "1";
        var updatedRestaurant = getRestaurant().toBuilder()
                .restaurantId("1")
                .name("Welcome Again")
                .stars(4)
                .tables(List.of(new Table("T1", 4, true, "01/07/2023"),
                        new Table("T2", 4, true, "01/07/2023"))).build();

        Mockito.when(restaurantRepository.findById(id)).thenReturn(Mono.just(getRestaurant()));
        Mockito.when(restaurantRepository.save(updatedRestaurant)).thenReturn(Mono.just(updatedRestaurant));
        final var restaurantMono = restaurantService.updateRestaurantById(id, updatedRestaurant);

        Assertions.assertNotNull(restaurantMono);
        StepVerifier.create(restaurantMono)
                .assertNext(restaurant -> {
                    Assertions.assertNotNull(restaurant);
                    Assertions.assertEquals(updatedRestaurant.getRestaurantId(), restaurant.getRestaurantId());
                    Assertions.assertEquals(updatedRestaurant.getName(), restaurant.getName());
                    Assertions.assertEquals(updatedRestaurant.getStars(), restaurant.getStars());
                }).verifyComplete();

    }

    private Restaurant getRestaurant() {
        return new Restaurant("1", "Welcome", "Pune", 5,
                List.of(new Table("T1", 4, true, "01/07/2023"),
                        new Table("T2", 4, true, "01/07/2023")));
    }
}