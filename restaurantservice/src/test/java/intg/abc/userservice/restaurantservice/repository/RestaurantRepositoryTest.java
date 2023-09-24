package abc.userservice.restaurantservice.repository;

import abc.userservice.restaurantservice.domain.Restaurant;
import abc.userservice.restaurantservice.domain.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
class RestaurantRepositoryTest {


    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll().block();
        var restaurants = List.of(new Restaurant("1", "Welcome", "Pune", 5,
                        List.of(new Table("T1", 4,true, "10/07/2023"),
                                new Table("T2", 4,true, "12/07/2023"))),
                new Restaurant(null, "Spice", "Thane", 4,
                        List.of(new Table("T3", 2,true, "14/07/2023"),
                                new Table("T4", 4,true, "16/07/2023"))));
        restaurantRepository.saveAll(restaurants).blockLast();
    }

    @AfterEach
    void tearDown() {

        restaurantRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        var restaurantFlux = restaurantRepository.findAll().log();
        StepVerifier.create(restaurantFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {
        var restaurantMono = restaurantRepository.findById("1").log();
        StepVerifier.create(restaurantMono)
                .assertNext(restaurant -> {
                    assertEquals("Welcome", restaurant.getName());
                })
                .verifyComplete();
    }

    @Test
    void findByName() {
        var restaurantFlux = restaurantRepository.findByName("Spice").log();
        StepVerifier.create(restaurantFlux)
                .assertNext(restaurant -> {
                    assertEquals("Thane", restaurant.getCity());
                })
                .verifyComplete();
    }

    @Test
    void findByStars() {
        var restaurantFlux = restaurantRepository.findByStars(5).log();
        StepVerifier.create(restaurantFlux)
                .assertNext(restaurant -> {
                    assertEquals("Pune", restaurant.getCity());
                })
                .verifyComplete();
    }

    @Test
    void findByCity() {
        var restaurantFlux = restaurantRepository.findByCity("Pune").log();
        StepVerifier.create(restaurantFlux)
                .assertNext(restaurant -> {
                    assertEquals(5, restaurant.getStars());
                })
                .verifyComplete();
    }

    @Test
    void saveRestaurant() {
        final var restaurant = new Restaurant("1", "Palace", "Kolkata", 5,
                List.of(new Table("T1", 4,true, "05/07/2023"),
                        new Table("T2", 4,true, "04/07/2023")));
        var restaurantMono = restaurantRepository.save(restaurant).log();

        StepVerifier.create(restaurantMono)
                .assertNext(restaurant1 -> {
                    assertNotNull(restaurant1.getName());
                    assertEquals("Palace", restaurant1.getName());
                    assertEquals("Kolkata", restaurant1.getCity());
                })
                .verifyComplete();
    }

    @Test
    void updateUser() {
        var restaurant = restaurantRepository.findById("1").block();
        restaurant.setStars(2);
        restaurant.setCity("Nagpur");

        var restaurantMono = restaurantRepository.save(restaurant).log();
        StepVerifier.create(restaurantMono)
                .assertNext(restaurant1 -> {
                    assertNotNull(restaurant1.getName());
                    assertEquals(2, restaurant1.getStars());
                    assertEquals("Nagpur", restaurant1.getCity());
                })
                .verifyComplete();
    }

    @Test
    void deleteRestaurant() {
        var deletedRestaurant = restaurantRepository.deleteById("1").block();
        var findAll = restaurantRepository.findAll().log();
        StepVerifier
                .create(findAll)
                .expectNextCount(1)
                .verifyComplete();
    }
}