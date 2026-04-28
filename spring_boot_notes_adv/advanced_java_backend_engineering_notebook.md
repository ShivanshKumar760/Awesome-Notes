# Advanced Java Backend Engineering Notebook (Senior-Level Handbook)

---

# 1. JVM INTERNALS (DEEP DIVE)

## 1.1 Class Loading Process

### Phases:
1. Loading
2. Linking
   - Verification
   - Preparation
   - Resolution
3. Initialization

### Example
```java
public class A {
    static int x = 10;
}
```

### Bytecode (javap -c A)
```
0: bipush        10
2: putstatic     #2
```

---

## 1.2 Execution Engine

- Interpreter
- JIT Compiler

### JIT Optimization
- Method inlining
- Dead code elimination

---

# 2. MEMORY MANAGEMENT (ULTRA DEEP)

## Heap Structure

- Eden
- Survivor S0/S1
- Old Gen

### Allocation Flow
```
new Object() → Eden → Survivor → Old Gen
```

---

## 2.1 Garbage Collection Internals

### G1 GC

Regions instead of generations

### Important JVM Flags

```
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:InitiatingHeapOccupancyPercent=45
```

### ZGC (Low Latency)
```
-XX:+UseZGC
-XX:ZUncommitDelay=300
```

---

# 3. SPRING IOC + DI (FROM SOURCE LEVEL)

## 3.1 IoC Container Internals

Core Classes:
- BeanDefinition
- BeanFactory
- ApplicationContext

---

## 3.2 Bean Creation Flow

1. Load BeanDefinition
2. Instantiate Bean
3. Populate Dependencies
4. Initialize Bean
5. Add to Singleton Cache

---

## 3.3 Dependency Injection Internals

### Constructor Injection (Recommended)

```java
@Service
public class UserService {
    private final UserRepo repo;

    public UserService(UserRepo repo) {
        this.repo = repo;
    }
}
```

### Why Constructor Injection?
- Immutable
- Testable
- No null issues

---

## 3.4 Bean Lifecycle

```java
@PostConstruct
public void init() {}

@PreDestroy
public void destroy() {}
```

---

# 4. SPRING MVC INTERNAL FLOW

## DispatcherServlet Flow

1. Request hits DispatcherServlet
2. HandlerMapping finds controller
3. HandlerAdapter executes
4. Controller returns response
5. ViewResolver resolves view

---

## Example Controller

```java
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
```

---

# 5. SPRING BOOT INTERNALS

## Auto Configuration

- Uses @EnableAutoConfiguration
- Reads spring.factories

---

## Example

```java
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

---

# 6. HIKARI CONNECTION POOL (PRODUCTION)

## Config

```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=30000
```

---

# 7. JPA + HIBERNATE INTERNALS

## Entity Example

```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
}
```

---

## Dirty Checking

Hibernate tracks changes automatically

---

## N+1 Problem

Fix:
```java
@Query("SELECT u FROM User u JOIN FETCH u.posts")
```

---

# 8. SPRING SECURITY + JWT (FULL IMPLEMENTATION)

## JWT Utility

```java
public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 86400000))
        .signWith(SignatureAlgorithm.HS256, SECRET)
        .compact();
}
```

---

## Security Filter

```java
public class JwtFilter extends OncePerRequestFilter {
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
        // validate token
    }
}
```

---

# 9. FINAL PROJECT: URL SHORTENER (PRODUCTION GRADE)

## Architecture

- Controller
- Service
- Repository
- Security Layer

---

## Entity

```java
@Entity
public class Url {
    @Id
    private Long id;
    private String originalUrl;
    private String shortCode;
}
```

---

## Repository

```java
public interface UrlRepo extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String code);
}
```

---

## Service

```java
@Service
public class UrlService {

    public String shorten(String url) {
        String code = UUID.randomUUID().toString().substring(0,6);
        return code;
    }
}
```

---

## Controller

```java
@RestController
public class UrlController {

    @PostMapping("/shorten")
    public String shorten(@RequestBody String url) {
        return service.shorten(url);
    }
}
```

---

## Production Enhancements

- Redis Cache
- Rate Limiting
- Analytics
- CDN

---

# END (TO BE CONTINUED WITH MORE DEPTH)


