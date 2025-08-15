package com.vm.tinyurl.service;

import reactor.core.publisher.Mono;

public interface UrlShortenerService {
	public Mono<String> shortenUrl(String originalUrl);
	public Mono<String> getOriginalUrl(String token);
}

