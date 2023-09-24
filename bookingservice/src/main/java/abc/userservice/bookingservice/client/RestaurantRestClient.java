package abc.userservice.bookingservice.client;

import abc.userservice.bookingservice.domain.restaurant.Restaurant;
import abc.userservice.bookingservice.domain.restaurant.Table;
import abc.userservice.bookingservice.exceptionhandler.RestaurantServiceClientException;
import abc.userservice.bookingservice.exceptionhandler.RestaurantServiceServerException;
import abc.userservice.bookingservice.util.RetryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantRestClient {

    private final WebClient webClient;

    @Value("${restClient.restaurantUrl}")
    private String restaurantUrl;


    public Flux<Restaurant> searchRestaurantByCity(String city) {
        var url = UriComponentsBuilder.fromUriString(restaurantUrl).queryParam("city", city).toUriString();
        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("status Code is: {}", clientResponse.statusCode().value());
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new RestaurantServiceClientException(
                                "there is no restaurant with city: " + city,
                                clientResponse.statusCode().value()));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(
                                    new RestaurantServiceClientException(
                                            responseMessage, clientResponse.statusCode().value())));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("status Code is: {}", clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(
                                    new RestaurantServiceServerException(
                                            responseMessage)));
                })
                .bodyToFlux(Restaurant.class)
                .retryWhen(RetryUtil.retrySpecific())
                .log();
    }

    public Flux<Restaurant> searchTablesByRestaurantId(String id) {
        var url = UriComponentsBuilder.fromUriString(restaurantUrl+"/"+id).toUriString();
        log.info("grv searchTablesByRestaurantId url "+url);
        Flux<Restaurant> restaurantFlux= webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("status Code is: {}", clientResponse.statusCode().value());
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new RestaurantServiceClientException(
                                "there is no restaurant with id: " + id,
                                clientResponse.statusCode().value()));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(
                                    new RestaurantServiceClientException(
                                            responseMessage, clientResponse.statusCode().value())));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("status Code is: {}", clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(
                                    new RestaurantServiceServerException(
                                            responseMessage)));
                })
                .bodyToFlux(Restaurant.class)
                .retryWhen(RetryUtil.retrySpecific())
                .log();

        return restaurantFlux.flatMap(restaurant -> {
           var tables= restaurant.getTables()
                   .stream().
                   filter(Table::isAvailable)
                   .collect(Collectors.toList());
           restaurant.setTables(tables);
               return Flux.just(restaurant);
        });
    }
}
