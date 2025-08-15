package com.vm.tinyurl.controllerr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vm.tinyurl.service.RateLimiterService;
import com.vm.tinyurl.service.UrlShortenerService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v2")
public class UrlControllerRateLimited {
    private final UrlShortenerService service;
    
    @Autowired
    private RateLimiterService rateLimiterService;

    public UrlControllerRateLimited(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public Mono<ResponseEntity<String>> shorten(@RequestParam String url, ServerHttpRequest request) {
        String ip = request.getRemoteAddress() != null ? request.getRemoteAddress().getHostString() : "unknown";
        return rateLimiterService.isAllowed(ip)
            .flatMap(allowed -> {
                if (!allowed) {
                    return Mono.just(ResponseEntity.status(429).body("Rate limit exceeded. Please try later."));
                }
                return service.shortenUrl(url)
                    .map(token -> ResponseEntity.ok("http://localhost:8080/" + token));
            });
    }

    @GetMapping("/{token}")
    public Mono<ResponseEntity<Void>> redirect(@PathVariable String token) {
        return service.getOriginalUrl(token)
                .map(url -> ResponseEntity.status(302).header("Location", url).<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
