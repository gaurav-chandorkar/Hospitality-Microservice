package abc.userservice.userservice.controller;

import abc.userservice.userservice.domain.User;
import abc.userservice.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @GetMapping("/user")
    public Flux<User> getUser(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "phone", required = false) String phone) {
        if (name != null) {
            return userService.getUserByName(name);
        } else if (phone != null) {
            return userService.getUserByPhone(phone);
        }
        return userService.getAllUser();
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {

        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
    }

    @PutMapping("/user/{id}")
    public Mono<ResponseEntity<User>> updateUser(@RequestBody User user, @PathVariable String id) {
        return userService.updateUser(user, id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
}
