package abc.userservice.bookingservice.service;

import abc.userservice.bookingservice.domain.Booking;
import abc.userservice.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public Mono<Booking> saveNewBooking(Booking booking) {

        return bookingRepository.save(booking);
    }

    public Flux<Booking> getBookingByBookingId(String bookingId) {
        return bookingRepository.findByBookingId(bookingId);
    }

    public Flux<Booking> getBookingByRestaurantId(String restaurantId) {
        return bookingRepository.findByRestaurantId(restaurantId);
    }

    public Flux<Booking> getBookingByUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Flux<Booking> getAllBooking() {
        return bookingRepository.findAll();
    }

    public Mono<Booking> updateBooking(Booking booking, String id) {
        return bookingRepository.findById(id)
                .flatMap(booking1 -> {
                    booking1.setBookingDate(booking.getBookingDate());
                    booking1.setRestaurantId(booking.getRestaurantId());
                    booking1.setUserId(booking.getUserId());
                    booking1.setTableId(booking.getTableId());
                    return bookingRepository.save(booking1);
                });
    }

    public Mono<Void> deleteBooking(String id) {
        return bookingRepository.deleteById(id);
    }
}
