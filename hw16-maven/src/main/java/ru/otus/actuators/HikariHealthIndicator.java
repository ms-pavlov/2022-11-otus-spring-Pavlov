package ru.otus.actuators;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HikariHealthIndicator implements HealthIndicator {

    private final HikariDataSource hikariDataSource;

    @Override
    public Health health() {
        if (null == hikariDataSource || !hikariDataSource.isRunning()) {
            return Health.down().withDetail("message", "Отсутствует подключение к БД").build();
        }
        var pool = hikariDataSource.getHikariPoolMXBean();
        if (pool.getTotalConnections() == pool.getIdleConnections()) {
            return Health.down().withDetail("message", "Простой").build();
        }
        if (pool.getTotalConnections() == pool.getActiveConnections()) {
            return Health.down().withDetail("message", "Высокая нагрузка").build();
        }
        return Health.up().withDetail("message", "Работаем").build();
    }
}
