package ru.otus.securities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.model.entities.User;

import java.util.Collection;
import java.util.function.Function;

public class UserDetailsAdapter implements UserDetails {

    private final User user;
    private final Function<User, Collection<? extends GrantedAuthority>> authoritiesStrategy;

    public UserDetailsAdapter(User user, Function<User, Collection<? extends GrantedAuthority>> authoritiesStrategy) {
        this.user = user;
        this.authoritiesStrategy = authoritiesStrategy;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritiesStrategy.apply(user);
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
