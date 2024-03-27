package ru.otus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ru.otus.model.dto.input.UserRequestDto;
import ru.otus.securities.UsersService;

import java.util.List;

@Configuration
public class AdminConfig {

    @Autowired
    private UsersService usersService;

    @EventListener(ApplicationStartedEvent.class)
    public void runAfterStartup() {
        if (Boolean.FALSE.equals(usersService.existsByUsername("admin").block())) {
            usersService.create(new UserRequestDto("admin", "admin", List.of("ADMIN"))).block();
        }
    }
}
