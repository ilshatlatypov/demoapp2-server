package ru.jvdev.demoapp.server.service;

import java.util.Optional;

import ru.jvdev.demoapp.server.model.User;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 18.11.2016
 */
interface UserService {
    Optional<User> getUserByUsername(String username);
}
