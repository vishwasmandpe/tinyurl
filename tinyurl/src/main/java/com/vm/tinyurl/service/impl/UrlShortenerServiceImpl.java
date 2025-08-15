package com.vm.tinyurl.service.impl;

import java.time.Instant;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.tinyurl.model.UrlMapping;
import com.example.tinyurl.util.Base62Encoder;
import com.vm.tinyurl.repo.UrlMappingRepository;

import reactor.core.publisher.Mono;

@Service
public class UrlShortenerServiceImpl implements com.vm.tinyurl.service.UrlShortenerService {

    private final UrlMappingRepository repository;
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 7;
    private final Random random = new Random();

    public UrlShortenerServiceImpl(UrlMappingRepository repository) {
        this.repository = repository;
    }

    // Generate a random token for the short URL
    private String generateToken() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            sb.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }

    // Shorten a given URL, ensuring unique token
    public Mono<String> shortenUrl(String originalUrl) {
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setCreatedAt(Instant.now());
        mapping.setVisitCount(0L);
        // Save to DB first to get the generated unique ID
        return repository.save(mapping)
            .map(saved -> {
                String token = Base62Encoder.encode(saved.getId());
                saved.setShortUrl(token);
                // Update saved record with the short URL token
                repository.save(saved).subscribe();
                return token;
            });
    }

    // Look up the original long URL by token and increment the visit count
    public Mono<String> getOriginalUrl(String token) {
    	long id = Base62Encoder.decode(token);
        return repository.findById(id)
            .flatMap(mapping -> {
                mapping.setVisitCount(mapping.getVisitCount() + 1);
                return repository.save(mapping).thenReturn(mapping.getOriginalUrl());
            });
    }
}

