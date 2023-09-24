package abc.userservice.userservice.service;

import abc.userservice.userservice.domain.User;
import abc.userservice.userservice.repository.UserRepository;
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
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    void addUserTest() {
        var user = getUser();
        Mockito.when(userRepository.save(user)).thenReturn(Mono.just(user));
        var monoUser = userService.addUser(user).log();
        Assertions.assertNotNull(monoUser);
        StepVerifier.create(monoUser)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void getUserByIdTest() {
        var id = "3";
        var user = getUser();
        Mockito.when(userRepository.findById(id)).thenReturn(Mono.just(user));
        var monoUser = userService.getUserById(id);

        Assertions.assertNotNull(monoUser);
        StepVerifier.create(monoUser)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void getUserByIdNotFoundTest() {
        var id = "3";
        Mockito.when(userRepository.findById(id)).thenReturn(Mono.empty());
        var monoUser = userService.getUserById(id);

        Assertions.assertNotNull(monoUser);
        StepVerifier.create(monoUser)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void getAllUserTest() {
        Mockito.when(userRepository.findAll()).thenReturn(Flux.just(getUser()));
        var fluxUser = userService.getAllUser();

        Assertions.assertNotNull(fluxUser);
        StepVerifier.create(fluxUser)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByPhoneTest() {
        var phone = "9878547890";
        var user = getUser();
        Mockito.when(userRepository.findByPhone(phone)).thenReturn(Flux.just(user));
        var userFlux = userService.getUserByPhone(phone).log();

        Assertions.assertNotNull(userFlux);
        StepVerifier.create(userFlux)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void findByNameTest() {
        var name = "3";
        var user = getUser();
        Mockito.when(userRepository.findByName(name)).thenReturn(Flux.just(user));
        var userFlux = userService.getUserByName(name).log();

        Assertions.assertNotNull(userFlux);
        StepVerifier.create(userFlux)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void deleteUser() {
        String id = "3";
        Mockito.when(userRepository.deleteById(id)).thenReturn(Mono.empty());
        var userMono = userService.deleteUser(id).log();
        Assertions.assertNotNull(userMono);
        StepVerifier.create(userMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    private User getUser() {
        return User.builder().name("Gaurav")
                .phone("9878547890")
                .email("test@gmail.com").build();
    }

}
