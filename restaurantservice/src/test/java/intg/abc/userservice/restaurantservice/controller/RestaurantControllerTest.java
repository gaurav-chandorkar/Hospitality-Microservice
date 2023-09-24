package abc.userservice.restaurantservice.controller;

import abc.userservice.restaurantservice.domain.Restaurant;
import abc.userservice.restaurantservice.domain.Table;
import abc.userservice.restaurantservice.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class RestaurantControllerTest {

    private final String URL = "/v1/restaurant";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll().block();
        var restaurants = List.of(new Restaurant("1", "Welcome", "Pune", 5,
                        List.of(new Table("T1", 4, true, "01/07/2023"),
                                new Table("T2", 4, false, "08/06/2023"))),
                new Restaurant(null, "Spice", "Thane", 4,
                        List.of(new Table("T3", 2, true,"02/07/2023"),
                                new Table("T4", 4, true, "04/07/2023"))));
        restaurantRepository.saveAll(restaurants).blockLast();
    }

    @AfterEach
    void tearDown() {

        restaurantRepository.deleteAll().block();
    }

    @Test
    void saveRestaurant() {
        var restaurant = new Restaurant("1", "test", "Pune", 5,
                List.of(new Table("T1", 4,true, "01/07/2023"),
                        new Table("T2", 4,true, "01/07/2023")));
        webTestClient
                .post()
                .uri(URL)
                .bodyValue(restaurant)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Restaurant.class)
                .consumeWith(restaurantEntityExchangeResult -> {
                    var res = restaurantEntityExchangeResult.getResponseBody();
                    Assertions.assertNotNull(res);
                });
    }

    @Test
    void saveRestaurant_with_validation() {
        var restaurant = new Restaurant("1", "", "", 5,
                List.of(new Table("T1", 4,true, "02/07/2023"),
                        new Table("T2", 4,true, "05/07/2023")));
        webTestClient
                .post()
                .uri(URL)
                .bodyValue(restaurant)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(restaurantEntityExchangeResult -> {
                    var res = restaurantEntityExchangeResult.getResponseBody();
                    Assertions.assertNotNull(res);
                    var expectedOutput= "restaurant.city must not be blank,restaurant.name must not be blank";
                    Assertions.assertEquals(expectedOutput, res);
                });
    }

    @Test
    void getAllRestaurant() {
        webTestClient
                .get()
                .uri(URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Restaurant.class)
                .hasSize(2)
                .consumeWith(listEntityExchangeResult -> {
                    var res = listEntityExchangeResult.getResponseBody();
                    System.out.println("Response " + res);
                });
    }

    @Test
    void getRestaurantByName() {
        var uri = UriComponentsBuilder.fromUriString(URL)
                .queryParam("name", "Welcome")
                .buildAndExpand().toUri();
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Restaurant.class)
                .hasSize(1);

    }

    @Test
    void getRestaurantByCity() {
        var uri = UriComponentsBuilder.fromUriString(URL)
                .queryParam("city", "Thane")
                .buildAndExpand().toUri();
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Restaurant.class)
                .hasSize(1)
                .consumeWith(listEntityExchangeResult ->
                {
                    var res = listEntityExchangeResult.getResponseBody();
                    Restaurant restaurant = res.get(0);
                    Assertions.assertEquals("Spice", restaurant.getName());
                    Assertions.assertEquals(4, restaurant.getStars());
                });
    }

    @Test
    void getRestaurantByStars() {
        var uri = UriComponentsBuilder.fromUriString(URL)
                .queryParam("stars", 4)
                .buildAndExpand().toUri();
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Restaurant.class)
                .hasSize(1)
                .consumeWith(listEntityExchangeResult ->
                {
                    var res = listEntityExchangeResult.getResponseBody();
                    Restaurant restaurant = res.get(0);
                    Assertions.assertEquals("Spice", restaurant.getName());
                    Assertions.assertEquals("Thane", restaurant.getCity());
                });
    }

    @Test
    void findRestaurantById() {
        var restaurantId = "1";
        webTestClient
                .get()
                .uri(URL + "/{id}", restaurantId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Welcome");
    }

    @Test
    void getRestaurantByIdNotFound() {
        var restaurantId = "11";
        webTestClient
                .get()
                .uri(URL + "/{id}", restaurantId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void updateRestaurantById() {
        var restaurantId = "1";
        var restaurant = new Restaurant("1", "Welcome", "Kolkata", 3,
                List.of(new Table("T10", 4,true, "01/07/2023"),
                        new Table("T20", 4, true, "01/07/2023")));
        webTestClient
                .put()
                .uri(URL + "/{id}", restaurantId)
                .bodyValue(restaurant)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Restaurant.class)
                .consumeWith(restaurantEntityExchangeResult -> {
                    var res = restaurantEntityExchangeResult.getResponseBody();
                    assert res != null;
                    Assertions.assertEquals("Kolkata", res.getCity());
                    Assertions.assertEquals(3, res.getStars());
                    Assertions.assertEquals("T10", res.getTables().get(0).getTableId());
                });
    }

    @Test
    void updateRestaurantById_NotFound() {
        var restaurantId = "0";
        var restaurant = new Restaurant("0", "Welcome", "Kolkata", 3,
                List.of(new Table("T10", 4, true, "24/06/2023"),
                        new Table("T20", 4, true, "20/06/2023")));
        webTestClient
                .put()
                .uri(URL + "/{id}", restaurantId)
                .bodyValue(restaurant)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteById() {
        var restaurantId = "3";
        webTestClient
                .delete()
                .uri(URL + "/{id}", restaurantId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}