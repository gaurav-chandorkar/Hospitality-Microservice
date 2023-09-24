package abc.userservice.bookingservice.controller;

import abc.userservice.bookingservice.client.RestaurantRestClient;
import abc.userservice.bookingservice.domain.Booking;
import abc.userservice.bookingservice.domain.restaurant.Restaurant;
import abc.userservice.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/")
public class BookingController {

    private final RestaurantRestClient restaurantClient;
    private final BookingService bookingService;

    @GetMapping("booking/restaurant-by-city/{city}")
    public Flux<Restaurant> getAllRestaurantsByCity(@PathVariable String city) {
        log.info("request received for get all restaurant by city: {}", city);
        return restaurantClient.searchRestaurantByCity(city);

    }

    @GetMapping("booking/available-table-by-restaurant/{id}")
    public Flux<Restaurant> getAvailableTableByRestaurantId(@PathVariable String id) {
        log.info("Request received for available table by restaurant id: {}", id);

        return restaurantClient.searchTablesByRestaurantId(id);
    }

    @PostMapping("/booking")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Booking> saveNewBooking(@RequestBody Booking booking) {
        return bookingService.saveNewBooking(booking);
    }

    @GetMapping("/booking")
    public Flux<Booking> getAllBooking(@RequestParam(value = "bookingId", required = false) String bookingId,
                                       @RequestParam(value = "restaurantId", required = false) String restaurantId,
                                       @RequestParam(value = "userId", required = false) String userId) {

        log.info("Request received with param bookingId : {}, restaurantId :{}, userId :{}",
                bookingId, restaurantId, userId);
        if (bookingId != null) {
            return bookingService.getBookingByBookingId(bookingId);
        } else if (restaurantId != null) {
            return bookingService.getBookingByRestaurantId(restaurantId);
        } else if (userId != null) {
            return bookingService.getBookingByUser(userId);
        } else
            return bookingService.getAllBooking();
    }

    @PutMapping("/booking/{id}")
    public Mono<ResponseEntity<Booking>> updateBookingInfo(
            @RequestBody Booking booking, @PathVariable String id) {

        return bookingService.updateBooking(booking, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
    }

    @DeleteMapping("/booking/{id}")
    public Mono<Void> deleteBooking(@PathVariable String id){

        return bookingService.deleteBooking(id);
    }

}
