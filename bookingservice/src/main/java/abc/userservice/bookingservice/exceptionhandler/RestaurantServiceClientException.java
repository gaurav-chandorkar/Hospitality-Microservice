package abc.userservice.bookingservice.exceptionhandler;

public class RestaurantServiceClientException extends RuntimeException {

    private String message;
    private  int statusCode;

    public RestaurantServiceClientException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
