package abc.userservice.bookingservice.exceptionhandler;

public class UserServiceServerException extends RuntimeException {
    private String message;

    public UserServiceServerException(String message){
        super(message);
        this.message= message;
    }
}
