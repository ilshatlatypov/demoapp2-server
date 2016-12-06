package ru.jvdev.demoapp.server.service;

import org.springframework.security.core.authority.AuthorityUtils;

import ru.jvdev.demoapp.server.model.User;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 18.11.2016
 */
@SuppressFBWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
