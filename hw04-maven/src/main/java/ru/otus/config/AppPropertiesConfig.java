package ru.otus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Locale;

@Configuration
@PropertySource("application.yml")
@ConfigurationProperties(prefix = "application")
public class AppPropertiesConfig implements PropertiesConfig{
    private Locale locale;
    private String path;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
