package abc.userservice.bookingservice.util;

import abc.userservice.bookingservice.exceptionhandler.RestaurantServiceServerException;
import abc.userservice.bookingservice.exceptionhandler.UserServiceServerException;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;

import java.time.Duration;

public class RetryUtil {

    public static Retry retrySpecific(){
        return Retry.fixedDelay(3, Duration.ofSeconds(1))
                .filter(ex -> ex instanceof UserServiceServerException
                || ex instanceof RestaurantServiceServerException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        Exceptions.propagate(retrySignal.failure()));
    }
}
