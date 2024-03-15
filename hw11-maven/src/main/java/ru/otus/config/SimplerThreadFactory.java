package ru.otus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.util.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class SimplerThreadFactory implements ThreadFactory {
    private static final Logger log = LoggerFactory.getLogger(SimplerThreadFactory.class);
    private final AtomicLong threadIdGenerator = new AtomicLong(0);
    private final String namePrefix;

    public SimplerThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(@NonNull Runnable task) {
        var thread = new Thread(task);
        thread.setName(namePrefix + threadIdGenerator.incrementAndGet());
        log.info("Thread {} create", thread.getName());
        return thread;
    }
}
