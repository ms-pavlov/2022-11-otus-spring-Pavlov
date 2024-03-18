package ru.otus.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.entities.User;

import java.util.Collection;
import java.util.function.Function;

public class UserDetailsAdapter implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsAdapter(User user, Function<User, Collection<? extends GrantedAuthority>> authoritiesStrategy) {
        this.user = user;
        this.authorities = authoritiesStrategy.apply(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return null != user;
    }

    @Override
    public boolean isAccountNonLocked() {
        return null != user;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return null != user;
    }

    @Override
    public boolean isEnabled() {
        return null != user;
    }
}
