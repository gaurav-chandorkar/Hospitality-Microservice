package abc.userservice.userservice.repository;

import abc.userservice.userservice.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Flux<User> findByName(String name);

    Flux<User> findByPhone(String phone);



}
