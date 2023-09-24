package abc.userservice.bookingservice.repository;

import abc.userservice.bookingservice.domain.Booking;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.List;

@DataMongoTest
@ActiveProfiles("test")
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll().block();
        var bookings = List.of(Booking.builder()
                        .bookingId("101").bookingDate("09/09/2023")
                        .restaurantId("r400")
                        .tableId("t001")
                        .userId("u101")
                        .build(),
                Booking.builder()
                        .bookingId("102").bookingDate("10/09/2023")
                        .restaurantId("r402")
                        .tableId("t002")
                        .userId("u102")
                        .build(),
                Booking.builder()
                        .bookingId("103").bookingDate("11/09/2023")
                        .restaurantId("r403")
                        .tableId("t003")
                        .userId("u103")
                        .build());
        bookingRepository.saveAll(bookings).blockLast();

    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll().block();
    }

    @Test
    void findByUserId() {
        var bookingMono = bookingRepository.findByUserId("u103").log();
        StepVerifier.create(bookingMono)
                .assertNext(booking1 -> {
                    Assertions.assertEquals("r403", booking1.getRestaurantId());
                    Assertions.assertEquals("11/09/2023", booking1.getBookingDate());
                }).verifyComplete();
    }

    @Test
    void findByRestaurantId() {

        var bookingMono = bookingRepository.findByRestaurantId("r402").log();
        StepVerifier.create(bookingMono)
                .assertNext(booking1 -> {
                    Assertions.assertEquals("u102", booking1.getUserId());
                    Assertions.assertEquals("10/09/2023", booking1.getBookingDate());
                }).verifyComplete();
    }

    @Test
    void findByBookingId() {

        var bookingMono = bookingRepository.findByBookingId("101").log();
        StepVerifier.create(bookingMono)
                .assertNext(booking1 -> {
                    Assertions.assertEquals("t001", booking1.getTableId());
                    Assertions.assertEquals("09/09/2023", booking1.getBookingDate());
                }).verifyComplete();
    }

    @Test
    void findAll() {
        var bookingFlux = bookingRepository.findAll().log();
        StepVerifier.create(bookingFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void deleteRestaurant() {
        var deletedRestaurant = bookingRepository.deleteById("101").block();
        var findAll = bookingRepository.findAll().log();
        StepVerifier
                .create(findAll)
                .expectNextCount(2)
                .verifyComplete();
    }
}