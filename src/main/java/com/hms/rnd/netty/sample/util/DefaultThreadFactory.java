package com.hms.rnd.netty.sample.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create thread for application
 */
public class DefaultThreadFactory implements ThreadFactory {
    private String prefix;
    private AtomicInteger count = new AtomicInteger(0);

    public DefaultThreadFactory(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            throw new RuntimeException("Prefix can not be empty");
        }
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, prefix + "-" + count.getAndIncrement());
    }
}
