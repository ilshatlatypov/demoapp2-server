package ru.jvdev.demoapp.server.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.jvdev.demoapp.server.model.User;
import ru.jvdev.demoapp.server.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 18.11.2016
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    @NonNull private final UserRepository userRepository;

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }
}
