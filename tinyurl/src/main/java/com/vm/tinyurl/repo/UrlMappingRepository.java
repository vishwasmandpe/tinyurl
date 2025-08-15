package com.vm.tinyurl.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.tinyurl.model.UrlMapping;

import reactor.core.publisher.Mono;

public interface UrlMappingRepository extends ReactiveCrudRepository<UrlMapping, Long> {
    Mono<UrlMapping> findByShortUrl(String shortUrl);
}
