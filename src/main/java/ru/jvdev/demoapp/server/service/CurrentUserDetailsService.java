package ru.jvdev.demoapp.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.jvdev.demoapp.server.model.User;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 18.11.2016
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CurrentUserDetailsService implements UserDetailsService {

    @NonNull private final UserService userService;

    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException(String.format("User with username=%s was not found", username)));
        return new CurrentUser(user);
    }

}
