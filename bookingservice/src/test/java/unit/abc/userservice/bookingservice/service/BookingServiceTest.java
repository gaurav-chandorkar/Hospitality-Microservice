package abc.userservice.bookingservice.service;

import abc.userservice.bookingservice.domain.Booking;
import abc.userservice.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    BookingService bookingService;

    @MockBean
    BookingRepository bookingRepository;


    @Test
    void saveNewBooking() {
        final var booking = getBooking();
        Mockito.when(bookingRepository.save(booking)).thenReturn(Mono.just(booking));

        final var bookingMono = bookingService.saveNewBooking(booking);
        Assertions.assertNotNull(bookingMono);
        StepVerifier.create(bookingMono)
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    void getBookingByBookingIdTest() {
        final var bookingId = "101";
        final var booking = getBooking();
        Mockito.when(bookingRepository.findByBookingId(bookingId)).thenReturn(Flux.just(booking));
        Flux<Booking> bookingFlux = bookingService.getBookingByBookingId(bookingId);
        Assertions.assertNotNull(bookingFlux);
        StepVerifier.create(bookingFlux)
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    void getBookingByUserIdTest() {
        final var userId = "u0001";
        final var booking = getBooking();
        Mockito.when(bookingRepository.findByUserId(userId)).thenReturn(Flux.just(booking));
        Flux<Booking> bookingFlux = bookingService.getBookingByUser(userId);

        Assertions.assertNotNull(bookingFlux);
        StepVerifier.create(bookingFlux)
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    void getBookingByRestaurantIdTest() {
        final var restId = "r10001";
        final var booking = getBooking();
        Mockito.when(bookingRepository.findByRestaurantId(restId)).thenReturn(Flux.just(booking));
        Flux<Booking> bookingFlux = bookingService.getBookingByRestaurantId(restId);

        Assertions.assertNotNull(bookingFlux);
        StepVerifier.create(bookingFlux)
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    void getAllBookingTest() {
        final var booking = getBooking();
        Mockito.when(bookingRepository.findAll()).thenReturn(Flux.just(booking));
        Flux<Booking> allBooking = bookingService.getAllBooking();

        Assertions.assertNotNull(allBooking);
        StepVerifier.create(allBooking)
                .expectNext(booking)
                .verifyComplete();
    }

    @Test
    void deleteBookingTest() {

        final var bookingId = "101";
        Mockito.when(bookingRepository.deleteById(bookingId)).thenReturn(Mono.empty());

        Mono<Void> deleteMono = bookingService.deleteBooking(bookingId);
        StepVerifier.create(deleteMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void bookingUpdateTest() {
        final var bookingId = "101";
        final var updatedBooking = getBooking();
        updatedBooking.setTableId("t111");
        updatedBooking.setUserId("u9093");
        updatedBooking.setRestaurantId("r000221");
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Mono.just(getBooking()));
        Mockito.when(bookingRepository.save(updatedBooking)).thenReturn(Mono.just(updatedBooking));
        var bookingUpdateMono = bookingService.updateBooking(updatedBooking, bookingId);
        Assertions.assertNotNull(bookingUpdateMono);

        StepVerifier.create(bookingUpdateMono)
                .assertNext(booking -> {
                    Assertions.assertNotNull(booking);
                    Assertions.assertEquals(updatedBooking.getUserId(), booking.getUserId());
                    Assertions.assertEquals(updatedBooking.getTableId(), booking.getTableId());
                    Assertions.assertEquals(updatedBooking.getRestaurantId(), booking.getRestaurantId());
                }).verifyComplete();

    }

    private Booking getBooking() {
        return Booking.builder().bookingId("101")
                .bookingDate("01/02/2023")
                .userId("u0001")
                .restaurantId("r10001")
                .build();
    }

}