package com.vm.tinyurl.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a simple fixed window, per-IP rate limiter designed for a Spring WebFlux application.
 * Tracks requests from each IPs & fixed time window.
**/

@Component
public class RateLimiterService {
    private final int LIMIT = 100; // max requests per window
    private final long WINDOW_MILLIS = 60 * 1000; // 1 minute window

    private final Map<String, Window> ipWindows = new ConcurrentHashMap<>();

    private static class Window {
        long windowStart;
        int count;
    }

    public Mono<Boolean> isAllowed(String ip) {
        long now = Instant.now().toEpochMilli();
        Window window = ipWindows.computeIfAbsent(ip, k -> {
            Window w = new Window();
            w.windowStart = now;
            w.count = 0;
            return w;
        });

        if (now - window.windowStart > WINDOW_MILLIS) {
            // Start new window
            window.windowStart = now;
            window.count = 1;
            return Mono.just(true);
        } else {
            if (window.count < LIMIT) {
                window.count++;
                return Mono.just(true);
            } else {
                return Mono.just(false);
            }
        }
    }
}
