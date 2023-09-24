package abc.userservice.bookingservice.repository;

import abc.userservice.bookingservice.domain.Booking;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface BookingRepository extends ReactiveMongoRepository<Booking, String> {


    Flux<Booking> findByUserId(String userId);

    Flux<Booking> findByRestaurantId(String restaurantId);

    Flux<Booking> findByBookingId(String bookingId);
}
