package abc.userservice.bookingservice.exceptionhandler;

public class RestaurantServiceServerException extends RuntimeException {

    private String message;

    public RestaurantServiceServerException(String message) {
        super(message);
        this.message = message;
    }
}
