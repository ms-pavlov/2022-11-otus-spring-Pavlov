package ru.otus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ru.otus.openapi.model.UserRequest;
import ru.otus.securities.services.UsersService;

import java.util.List;

@Configuration
public class AdminConfig {

    @Autowired
    private UsersService usersService;

    @EventListener(ApplicationStartedEvent.class)
    public void runAfterStartup() {
        if (Boolean.FALSE.equals(usersService.existsByUsername("admin"))) {
            usersService.create(
                    new UserRequest("admin", "admin", "admin")
                            .accesses(List.of(UserRequest.AccessesEnum.ADMIN)));
        }
    }
}
