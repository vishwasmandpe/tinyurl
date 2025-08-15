package com.vm.tinyurl.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.Instant;

@Table("url_mapping")
public class UrlMapping {

    @Id
    private Long id;

    private String shortUrl;

    private String originalUrl;

    private Instant createdAt;

    private Long visitCount;

    public UrlMapping() {}

    public UrlMapping(String shortUrl, String originalUrl, Instant createdAt, Long visitCount) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.visitCount = visitCount;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getShortUrl() { return shortUrl; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Long getVisitCount() { return visitCount; }
    public void setVisitCount(Long visitCount) { this.visitCount = visitCount; }
}
