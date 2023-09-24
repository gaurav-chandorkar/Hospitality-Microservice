package abc.userservice.userservice.repository;

import abc.userservice.userservice.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll().block();
        var users = List.of(new User(null, "sham", "email1@gmail.com", "9087833042"),
                new User("3", "ram", "emailsdf@gmail.com", "9187833042"));

        userRepository.saveAll(users)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        var userFlux = userRepository.findAll().log();
        StepVerifier.create(userFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {
        var userMono = userRepository.findById("3").log();
        StepVerifier.create(userMono)
                .assertNext(user -> {
                    assertEquals("ram", user.getName());
                })
                //.expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findByPhone() {
        var userMono = userRepository.findByPhone("9187833042").log();
        StepVerifier.create(userMono)
                .assertNext(user -> {
                    assertEquals("ram", user.getName());
                })
                .verifyComplete();
    }

    @Test
    void findByName() {
        var userMono = userRepository.findByName("ram").log();
        StepVerifier.create(userMono)
                .assertNext(user -> {
                    assertEquals("emailsdf@gmail.com", user.getEmail());
                })
                .verifyComplete();
    }

    @Test
    void saveUser() {
        final var user1 = new User(null, "Batman", "save@mail.com","9838492430");
        var userMono = userRepository.save(user1).log();

        StepVerifier.create(userMono)
                .assertNext(user -> {
                    assertNotNull(user.getUserId());
                    assertEquals("Batman", user.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateUser() {
        var user = userRepository.findById("3").block();
        user.setPhone("1000003234");

        var userMono = userRepository.save(user).log();
        StepVerifier.create(userMono)
                .assertNext(user1 -> {
                    assertNotNull(user1.getName());
                    assertEquals("1000003234", user1.getPhone());
                })
                .verifyComplete();
    }

    @Test
    void deleteUser(){
        var deletedUser = userRepository.deleteById("3").block();
        var findAll=userRepository.findAll().log();
        StepVerifier
                .create(findAll)
                .expectNextCount(1)
                .verifyComplete();
    }
}