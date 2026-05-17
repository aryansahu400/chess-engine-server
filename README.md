# Chess Engine Server

A reactive chess backend built using Spring Boot WebFlux, Redis, Virtual Threads, and AI integrations.

The project provides:
- Chess move suggestions using Stockfish
- AI-powered move analysis
- Streaming responses using Server-Sent Events (SSE)
- Distributed rate limiting using Redis
- Structured exception handling and logging
- Git-based version tracking
- Reactive and scalable architecture

---

# Features

## 1. AI Move Analysis

Endpoint:
```http
POST /analyse-move
```

Streams a detailed chess move analysis using AI.

The service accepts:
- FEN before the move
- FEN after the move

The AI responds with:
- Move rating
- Strategic analysis
- Good aspects
- Weaknesses
- Positional/game impact

Streaming is implemented using:
- Spring AI
- Flux<String>
- TEXT_EVENT_STREAM

This allows real-time token streaming to the frontend.

---

## 2. Best Move Suggestion

Endpoint:
```http
POST /suggest-move
```

Uses Stockfish APIs to:
- Analyze board position
- Suggest strongest move
- Return evaluation score
- Return engine explanation

Request:
```json
{
  "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
}
```

Response:
```json
{
  "move": "e2e4",
  "evaluation": 0.4,
  "explanation": "bestmove e2e4"
}
```

---

# Architecture

## Reactive Stack

The application is fully reactive using:
- Spring WebFlux
- Project Reactor
- Reactive Redis
- Non-blocking HTTP communication

Reactive types used:
- Mono
- Flux

Benefits:
- High concurrency
- Low memory usage
- Better scalability under load

---

## Virtual Thread Integration

The application integrates Java Virtual Threads with Reactor.

Configuration:
```java
Schedulers.fromExecutor(
    Executors.newThreadPerTaskExecutor(
        Thread.ofVirtual().factory()
    )
)
```

Used for:
- AI analysis tasks
- External API communication
- Concurrent request handling

This improves scalability while maintaining readable code.

---

# Rate Limiting

Implemented using:
- Redis
- ReactiveStringRedisTemplate
- WebFilter

Strategy:
1. Each client receives a unique cookie
2. Request count stored in Redis
3. TTL-based expiration window
4. Requests exceeding limit return HTTP 429

Configuration:
```yaml
rate-limit:
  enabled: true
  requests: 10
  duration-seconds: 10000
```

Advantages:
- Distributed
- Stateless backend
- Cloud scalable
- Prevents abuse

---

# AI Integration

The AI analysis service uses Spring AI ChatClient.

Configured via:
```yaml
spring:
  ai:
    openai:
```

Supports:
- OpenAI-compatible APIs
- Ollama-compatible APIs
- Local LLM deployments

The AI prompt is engineered to produce structured markdown responses for frontend rendering.

---

# Error Handling

Global exception handling implemented using:
- @RestControllerAdvice

Features:
- Standardized error response
- Transaction IDs
- Git branch tracking
- Commit tracking
- Structured stack traces

Example response:
```json
{
  "version": "main-a1b2c3",
  "timestamp": "2026-05-17T10:30:00Z",
  "transactionId": "abc123",
  "message": "Internal Server Error"
}
```

---

# Logging System

Custom error normalization utility:
- Captures class name
- Method name
- Line number
- Full stack trace
- Git metadata

Useful for:
- Production debugging
- Observability
- Traceability
- CI/CD deployments

---

# Git Metadata Tracking

Git details are injected from:
```properties
git.properties
```

Tracked:
- Git branch
- Commit ID

Included in:
- Error responses
- Logs
- Debugging metadata

---

# Redis Usage

Redis is used for:
- Distributed rate limiting
- Request tracking
- Expiration-based counters

Reactive Redis integration:
```yaml
spring:
  data:
    redis:
```

---

# Configuration

## Environment Variables

### AI Configuration

```bash
OPEN_AI_API_KEY=
OPEN_AI_BASE_URL=
OPEN_AI_MODEL=
```

### Redis

```bash
REDIS_HOST=
REDIS_PORT=
```

### Rate Limiting

```bash
RATE_LIMIT_ENABLED=
RATE_LIMIT_VALUE=
RATE_LIMIT_TIME_WINDOW=
COOKIE_NAME=
```

---

# Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core language |
| Spring Boot | Application framework |
| Spring WebFlux | Reactive APIs |
| Project Reactor | Reactive programming |
| Redis | Distributed caching/rate limiting |
| Spring AI | AI integration |
| Virtual Threads | High concurrency |
| WebClient | Non-blocking HTTP |
| Lombok | Boilerplate reduction |

---

# API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/analyse-move` | AI move analysis stream |
| POST | `/suggest-move` | Best move suggestion |

---

# Example Flow

## Move Suggestion
1. Client sends FEN
2. Service calls Stockfish API
3. Parses engine response
4. Returns best move and evaluation

## Move Analysis
1. Client sends before/after FEN
2. Prompt generated dynamically
3. AI streams markdown response
4. Frontend renders live analysis

---

# Scalability Features

- Fully non-blocking architecture
- Virtual thread execution
- Redis-backed distributed rate limiting
- Streaming responses
- Stateless services
- Cloud-ready configuration

---

# Future Improvements

Potential enhancements:
- PGN support
- Opening database integration
- Multi-engine analysis
- Puzzle generation
- Game review pipeline
- WebSocket streaming
- Persistent game storage
- Player profiles
- Matchmaking
- Kubernetes deployment support

---

# Running the Application

## Requirements

- Java 21+
- Redis
- Maven

---

## Start Redis

```bash
docker run -p 6379:6379 redis
```

Because obviously suffering through manual setup is optional now.

---

## Run Application

```bash
./mvnw spring-boot:run
```

---

# Design Goals

The system is designed to:
- Handle high concurrency efficiently
- Stream AI responses in real time
- Remain cloud-native and horizontally scalable
- Keep latency low
- Support modern reactive frontend applications

---

# Project Structure

```text
configuration/
controller/
exception/
filter/
model/
prompt/
service/
util/
```

---

# License

MIT License

---

# Final Notes

This project combines:
- Reactive systems
- AI integration
- Distributed rate limiting
- Real-time streaming
- Virtual thread execution

Into one chess backend that is absurdly overengineered for moving wooden horses diagonally.  
Which, frankly, is exactly how software engineering should be.
