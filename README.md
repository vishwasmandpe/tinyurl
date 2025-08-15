# Tiny URL
This is a generic implementation of tiny URL service. This is just a simplistic implementation.
Define Requirements & Scope
Core Requirements:

Shorten a given URL and return a unique, shorter alias.

Redirect short URLs to the original long URL.

Handle high read and write throughput efficiently.

Support analytics (optional for MVP: click counts, user logs).

Non-Functional:

Scalability to millions of URLs.

Reliability/persistence (no data loss).

Speed (low latency for redirect).

Security (prevent abuse, handle malicious links).

2. High-Level System Design
Main Components
API Layer: Exposes endpoints to shorten and expand URLs.

Encoding Service: Generates unique short tokens.

Persistent Storage: Stores mappings from short to long URLs.

Redirect Service: Looks up the short token and redirects.

**APIs**:
-----

POST /shorten endpoint (input long URL, output short URL).

GET /{shortUrl} endpoint (redirects to long URL).

**Design Classes**
------------------
UrlMapping entity: fields for long URL, short token, creation date, optional usage count.

UrlMappingRepository: Spring Data JPA for CRUD.

UrlShortenerService: handles ID generation (hashing, counter, base62, etc.).

UrlController: REST controller for endpoints.


Capacity Estimations
1. Key Metrics & Assumptions
Expected Traffic: 10 million new URLs per month (approximate).

Read/Redirect Requests: 100x the write traffic (e.g., 1 billion per month).

Data Retention: URLs and click logs stored for at least 3 years.

Average URL Lengths:

Short URL token: 7 characters (base62 encoding).

Original URL: ~100 bytes (varies).

2. Storage Estimation
Monthly new URLs: 10 million.

Per URL Storage:

Long URL: 100 bytes.

Token: 7 bytes.

Metadata (timestamps, counters, extras): ~50 bytes.

Total per record: ~157 bytes (round to 200 bytes for safety).

Yearly Data:

10 million records/month × 12 = 120 million/year.

Storage/year: 120 million × 200 bytes ≈ 24 GB/year.

3 years: 72 GB.

3. Throughput Estimation
Writes (Shorten):

Peak: 10 million/month ≈ 4/sec.

Allow buffer for spikes: Plan for 50 writes/sec.

Reads (Redirects):

Peak: 1 billion/month ≈ 380/sec avg; real peak (traffic spikes, viral links): plan for 5,000+ reads/sec at scale.

4. Database Sizing
Small enough for a single modern server in MVP phase.

Easy to scale using sharding (on hash of token) for horizontal growth.

5. Bandwidth Estimation
Redirect Response: Small (HTTP redirect headers).

Heavy traffic only if adding advanced analytics/logging.

6. Caching Requirements
Top N URLs: Hot/cold distribution; cache most accessed tokens.

Consider Redis or Memcached—cache hit rate should minimize DB traffic.

7. Scaling Considerations
MVP: Single RDBMS or key-value store suffices.

At scale: Partition by token prefix, replicate for high availability.

Prepare endpoints and codebase for future distributed deployments.

**Next Enhancements**

1. Analytics & Reporting
2.  User Management
3.  Caching for Performance
4.  Monitoring & Metrics
5.  Security Features
6.  Distributed rate limiting (Redis-based)
