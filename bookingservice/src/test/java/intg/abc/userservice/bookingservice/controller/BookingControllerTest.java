package abc.userservice.bookingservice.controller;

import abc.userservice.bookingservice.domain.Booking;
import abc.userservice.bookingservice.domain.restaurant.Restaurant;
import abc.userservice.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084)
@TestPropertySource(
        properties = {"restClient.restaurantUrl= http://localhost:8084/v1/restaurant"}
)
class BookingControllerTest {

    private static final String URL = "/v1/booking";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {

        var bookings = List.of(Booking.builder().bookingDate("01/08/2023")
                        .bookingId("1").restaurantId("101").userId("a").build(),
                Booking.builder().bookingDate("02/08/2023")
                        .bookingId("2").restaurantId("102").userId("b").build());
        bookingRepository.saveAll(bookings)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
    }

    @Test
    void retrieveAvailableTableByRestaurantId(){
        var restaurantId="64cb913019c6f7469f812c21";
        /*stubFor(get(urlEqualTo("v1/restaurant/"+restaurantId))
                .willReturn(aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBodyFile("restaurant.json")));*/

        var movieId = "abc";
        stubFor(get(urlEqualTo("/v1/restaurant/"+movieId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("restaurant.json")));

        webTestClient.get()
                .uri(URL+"/available-table-by-restaurant/{id}",restaurantId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Restaurant.class);
    }
    @Test
    void getAllBookingInfo() {
        webTestClient.get().uri(URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Booking.class)
                .hasSize(2);

    }
}