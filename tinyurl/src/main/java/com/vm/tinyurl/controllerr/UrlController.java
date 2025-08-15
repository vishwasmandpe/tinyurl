package com.vm.tinyurl.controllerr;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vm.tinyurl.service.UrlShortenerService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class UrlController {
    private final UrlShortenerService service;

    public UrlController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public Mono<String> shorten(@RequestParam String url) {
        return service.shortenUrl(url);
    }

    @GetMapping("/{token}")
    public Mono<ResponseEntity<Void>> redirect(@PathVariable String token) {
        return service.getOriginalUrl(token)
                .map(url -> ResponseEntity.status(302).header("Location", url).<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
