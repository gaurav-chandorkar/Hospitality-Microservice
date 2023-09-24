package abc.userservice.userservice.service;

import abc.userservice.userservice.domain.User;
import abc.userservice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> addUser(User user) {
        return userRepository.save(user);
    }

    public Flux<User> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public Flux<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public Flux<User> getAllUser() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> updateUser(User updatedUser, String id) {
        return userRepository.findById(id)
                .flatMap(user -> {
                    user.setEmail(updatedUser.getEmail());
                    user.setName(updatedUser.getName());
                    user.setPhone(updatedUser.getPhone());
                    return userRepository.save(user);
                });
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id);
    }
}
