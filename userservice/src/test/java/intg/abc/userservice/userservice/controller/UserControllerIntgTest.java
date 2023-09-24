package abc.userservice.userservice.controller;

import abc.userservice.userservice.domain.User;
import abc.userservice.userservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class UserControllerIntgTest {

    private final String URL = "/v1/user";

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebTestClient webTestClient;

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
        userRepository.deleteAll();
    }

    @Test
    void addUserTest() {
        var user = new User("13", "baban", "babanf@gmail.com", "8987833042");

        webTestClient
                .post()
                .uri(URL)
                .bodyValue(user)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(User.class)
                .consumeWith(userEntityExchangeResult -> {
                    var res = userEntityExchangeResult.getResponseBody();
                    Assertions.assertNotNull(res);
                });
    }

    @Test
    void addUserTest_with_validation() {
        var user = new User("13", "", "babanfgmail.com", "8987833042");

        webTestClient
                .post()
                .uri(URL)
                .bodyValue(user)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(userEntityExchangeResult -> {
                    var res = userEntityExchangeResult.getResponseBody();
                    System.out.println("responseBody " + res);
                    Assertions.assertNotNull(res);
                    var expectedOutput= "user.email must be valid email id,user.name must be present";
                    Assertions.assertEquals(expectedOutput, res);
                });
    }

    @Test
    void getAllUsers() {
        webTestClient
                .get()
                .uri(URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(User.class)
                .hasSize(2)
                .consumeWith(listEntityExchangeResult -> {
                    var res = listEntityExchangeResult.getResponseBody();
                    System.out.println("Response " + res);
                });

    }

    @Test
    void getUserByName() {
        var uri = UriComponentsBuilder.fromUriString(URL)
                .queryParam("name", "ram")
                .buildAndExpand().toUri();
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(User.class)
                .hasSize(1);

    }

    @Test
    void getUserByPhone() {
        var uri = UriComponentsBuilder.fromUriString(URL)
                .queryParam("phone", "9087833042")
                .buildAndExpand().toUri();
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(User.class)
                .hasSize(1);
    }

    @Test
    void getUserById() {
        var userId = "3";
        webTestClient
                .get()
                .uri(URL + "/{id}", userId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("ram");
    }

    @Test
    void getUserByIdNotFound() {
        var userId = "1";
        webTestClient
                .get()
                .uri(URL + "/{id}", userId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void updateUserInfo() {
        var userId = "3";
        var user = new User(null, "ram", "updated@email.com ", "9090909009");
        webTestClient
                .put()
                .uri(URL + "/{id}", userId)
                .bodyValue(user)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .consumeWith(userEntityExchangeResult -> {
                    var res = userEntityExchangeResult.getResponseBody();
                    assert res != null;
                    assert res.getName() != null;
                });
    }

    @Test
    void updateUser_NotFound() {
        var userId = "33";
        var user = new User(null, "test", "notFoundted@email.com ", "4430909009");
        webTestClient
                .put()
                .uri(URL + "/{id}", userId)
                .bodyValue(user)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteUser() {

        var userId = "3";
        webTestClient
                .delete()
                .uri(URL + "/{id}", userId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}