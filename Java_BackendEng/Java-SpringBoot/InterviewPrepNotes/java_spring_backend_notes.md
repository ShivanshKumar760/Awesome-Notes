# Java Backend Engineering — Complete Deep Lecture Notes

This document is a **complete backend engineering learning guide for Java + Spring ecosystem**. It covers fundamentals, intermediate topics, and deep backend internals.

------------------------------------------------------------

# PART 1 — JAVA FUNDAMENTALS

------------------------------------------------------------

# 1. What is Java

Java is a **high‑level, object‑oriented, strongly typed programming language** created by Sun Microsystems and now maintained by Oracle.

Key Properties

- Platform Independent
- Object Oriented
- Automatic Memory Management
- Secure
- Multithreaded

"Write Once Run Anywhere" is possible because Java runs on the **JVM (Java Virtual Machine)**.

------------------------------------------------------------

# 2. Java Compilation Pipeline

```
.java source file
      │
      ▼
javac compiler
      │
      ▼
.class bytecode
      │
      ▼
JVM executes bytecode
      │
      ▼
Machine code on OS
```

Important Components

- **JDK** – Java Development Kit
- **JRE** – Java Runtime Environment
- **JVM** – Java Virtual Machine

------------------------------------------------------------

# 3. Java Data Types

## Primitive Types

- int
- long
- float
- double
- boolean
- char
- byte
- short

Example

```java
int age = 25;
boolean active = true;
char grade = 'A';
```

## Reference Types

- Objects
- Arrays
- Classes
- Interfaces

Example

```java
String name = "Shiv";
```

------------------------------------------------------------

# 4. Control Flow

Java provides control statements for decision making and loops.

Example

```java
for (int i = 0; i < 5; i++) {
    System.out.println(i);
}
```

Example with condition

```java
if(age > 18) {
    System.out.println("Adult");
} else {
    System.out.println("Minor");
}
```

------------------------------------------------------------

# 5. Classes and Objects

A **class** is a blueprint and an **object** is an instance of that class.

Example

```java
class User {

    String name;
    int age;

    void speak() {
        System.out.println("Hello " + name);
    }
}
```

Creating object

```java
User user = new User();
user.name = "Shiv";
user.speak();
```

------------------------------------------------------------

# PART 2 — OBJECT ORIENTED PROGRAMMING

------------------------------------------------------------

# 1. Encapsulation

Encapsulation means **restricting direct access to fields**.

Example

```java
class BankAccount {

    private int balance;

    public void deposit(int amount) {
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }
}
```

Benefits

- Security
- Data hiding
- Maintainability

------------------------------------------------------------

# 2. Inheritance

Inheritance allows a class to **reuse properties of another class**.

Example

```java
class Animal {

    void speak() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {

    void bark() {
        System.out.println("Dog bark");
    }
}
```

------------------------------------------------------------

# 3. Polymorphism

Polymorphism allows objects to take **multiple forms**.

## Method Overloading (Compile time)

```java
class Calculator {

    int add(int a, int b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }
}
```

## Method Overriding (Runtime)

```java
class Animal {
    void sound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Dog barking");
    }
}
```

------------------------------------------------------------

# 4. Abstraction

Abstraction hides implementation details and exposes only necessary functionality.

## Abstract Class

```java
abstract class Vehicle {
    abstract void start();
}

class Car extends Vehicle {

    void start() {
        System.out.println("Car starting");
    }
}
```

## Interface

```java
interface Payment {
    void pay();
}

class CreditCardPayment implements Payment {

    public void pay() {
        System.out.println("Paid using credit card");
    }
}
```

------------------------------------------------------------

# Difference Between Interface and Abstract Class

Interface

- Supports multiple inheritance
- Only method declarations (before Java 8)

Abstract Class

- Can have implemented methods
- Single inheritance

------------------------------------------------------------

# PART 3 — GENERICS

Generics provide **compile‑time type safety**.

Without generics

```java
List list = new ArrayList();
list.add("Hello");
list.add(10);
```

With generics

```java
List<String> list = new ArrayList<>();
list.add("Hello");
```

Generic class example

```java
class Box<T> {

    T value;

    void set(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }
}
```

------------------------------------------------------------

# PART 4 — COLLECTIONS FRAMEWORK

Collections provide **data structures for storing objects**.

Core Interfaces

- Collection
- List
- Set
- Queue
- Map

------------------------------------------------------------

# List

Ordered collection that allows duplicates.

Example

```java
List<String> names = new ArrayList<>();

names.add("Shiv");
names.add("Alex");
```

## ArrayList Internal Working

ArrayList uses a **dynamic array**.

When capacity exceeds limit:

1. A new array is created
2. Old elements are copied
3. Capacity increases

Time Complexity

- Access → O(1)
- Insert → O(n)

------------------------------------------------------------

# LinkedList

Implemented using a **doubly linked list**.

Each node stores:

- value
- next pointer
- previous pointer

Example

```java
LinkedList<Integer> list = new LinkedList<>();
list.add(10);
list.add(20);
```

------------------------------------------------------------

# Set

Set stores **unique elements only**.

Example

```java
Set<String> users = new HashSet<>();
users.add("A");
users.add("B");
```

Implementations

- HashSet
- TreeSet
- LinkedHashSet

------------------------------------------------------------

# Map

Map stores **key‑value pairs**.

Example

```java
Map<String, Integer> map = new HashMap<>();

map.put("apple", 10);
map.put("banana", 20);
```

------------------------------------------------------------

# HashMap Internal Working

HashMap internally uses:

- Array of buckets
- Linked list
- Red‑Black tree (after threshold)

Hashing determines **bucket index**.

------------------------------------------------------------

# PART 5 — JVM INTERNALS

JVM Architecture

- Class Loader
- Runtime Memory
- Execution Engine
- Garbage Collector

------------------------------------------------------------

# Class Loading Process

Steps

1. Loading
2. Linking
3. Initialization

Types of Class Loaders

- Bootstrap ClassLoader
- Extension ClassLoader
- Application ClassLoader

------------------------------------------------------------

# JVM Memory Areas

Heap

Stores objects.

Stack

Stores method frames.

Method Area

Stores class metadata.

Program Counter

Tracks current instruction.

------------------------------------------------------------

# Garbage Collection

Garbage collector removes unused objects.

Algorithms

- Mark and Sweep
- Generational GC
- G1GC
- ZGC

------------------------------------------------------------

# Memory Leak Example

```java
static List<Object> cache = new ArrayList<>();

public void store(Object obj) {
    cache.add(obj);
}
```

Objects never removed → memory leak.

------------------------------------------------------------

# PART 6 — JAVA CONCURRENCY

Java supports multithreading.

------------------------------------------------------------

# Creating Threads

Method 1 — Extending Thread

```java
class MyThread extends Thread {

    public void run() {
        System.out.println("Thread running");
    }
}
```

Method 2 — Implementing Runnable

```java
class Task implements Runnable {

    public void run() {
        System.out.println("Task running");
    }
}
```

------------------------------------------------------------

# Thread Pool

```java
ExecutorService service = Executors.newFixedThreadPool(5);

service.submit(() -> {
    System.out.println("Running task");
});
```

------------------------------------------------------------

# CompletableFuture

```java
CompletableFuture
    .supplyAsync(() -> fetchUser())
    .thenApply(user -> process(user));
```

------------------------------------------------------------

# Java Memory Model

Defines rules for:

- Thread visibility
- Instruction ordering

Keywords

- volatile
- synchronized

------------------------------------------------------------

# PART 7 — SPRING FRAMEWORK

Spring is a **dependency injection framework**.

------------------------------------------------------------

# Dependency Injection

Example

```java
@Service
class UserService {

    private final UserRepository repo;

    UserService(UserRepository repo) {
        this.repo = repo;
    }
}
```

------------------------------------------------------------

# Spring Bean Lifecycle

1. Instantiation
2. Dependency Injection
3. BeanPostProcessor
4. Initialization
5. Ready
6. Destroy

------------------------------------------------------------

# Spring MVC Architecture

```
Client
  │
  ▼
DispatcherServlet
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
Database
```

------------------------------------------------------------

# PART 8 — SPRING BOOT

Spring Boot simplifies Spring configuration.

Features

- Auto Configuration
- Embedded Servers
- Starter Dependencies

------------------------------------------------------------

# Spring Boot Project Structure

```
src/main/java

controller/
service/
repository/
model/
config/
```

------------------------------------------------------------

# AutoConfiguration Mechanism

Spring Boot reads:

```
META-INF/spring.factories
```

Then automatically loads configuration classes.

------------------------------------------------------------

# PART 9 — REST API DESIGN

HTTP Methods

- GET
- POST
- PUT
- DELETE
- PATCH
- OPTIONS

Example API

```
GET /users
POST /users
PUT /users/1
DELETE /users/1
```

------------------------------------------------------------

# Pagination Example

```
GET /users?page=1&size=10
```

------------------------------------------------------------

# PART 10 — DATABASES

SQL Databases

- MySQL
- PostgreSQL
- Oracle

------------------------------------------------------------

# Transactions

ACID Properties

- Atomicity
- Consistency
- Isolation
- Durability

------------------------------------------------------------

# Isolation Levels

- Read Uncommitted
- Read Committed
- Repeatable Read
- Serializable

------------------------------------------------------------

# PART 11 — SPRING SECURITY

Spring Security handles authentication and authorization.

------------------------------------------------------------

# Security Filter Chain

```
Request
  │
  ▼
Security Filters
  │
  ▼
AuthenticationManager
  │
  ▼
Controller
```

------------------------------------------------------------

# JWT Authentication Flow

```
User Login
   │
   ▼
Server verifies credentials
   │
   ▼
JWT token generated
   │
   ▼
Client sends token
Authorization: Bearer <token>
```

------------------------------------------------------------

# OAuth2 Flow

```
User
  │
  ▼
Authorization Server
  │
  ▼
Access Token
  │
  ▼
Resource Server
```

------------------------------------------------------------

------------------------------------------------------------

# PART 12 — COLLECTIONS INTERNALS (ADVANCED)

## HashMap Deep Internals

Important fields inside HashMap

```java
transient Node<K,V>[] table;
transient int size;
int threshold;
final float loadFactor;
```

### Bucket Calculation

```java
index = (n - 1) & hash
```

This bit operation is faster than modulo.

### Collision Handling

When two keys produce same hash:

1. Stored in same bucket
2. Stored as linked list
3. If bucket size > 8 → converted to Red‑Black Tree

This prevents worst case **O(n)** lookups.

### Resize Operation

When size exceeds

```
capacity * loadFactor
```

HashMap doubles its capacity.

Default load factor

```
0.75
```

------------------------------------------------------------

## ConcurrentHashMap

Designed for **multi‑threaded environments**.

Older Java versions used **segment locks**.

Modern versions use:

• CAS (Compare And Swap)
• synchronized blocks on bins

Example

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

map.put("a",1);
map.put("b",2);
```

Benefits

• Thread safe
• Higher concurrency than Hashtable

------------------------------------------------------------

## TreeMap

TreeMap internally uses **Red‑Black Tree**.

Properties of Red Black Tree

• Balanced binary tree
• Root always black
• No two red nodes adjacent

Time complexity

```
Insert  O(log n)
Search  O(log n)
Delete  O(log n)
```

------------------------------------------------------------

# PART 13 — JVM DEEP INTERNALS

## JVM Execution Pipeline

```
Java Source
     │
     ▼
Bytecode
     │
     ▼
Class Loader
     │
     ▼
Interpreter
     │
     ▼
JIT Compiler
     │
     ▼
Native Machine Code
```

------------------------------------------------------------

## JIT Compiler

JIT = Just In Time compiler.

Instead of interpreting bytecode every time:

• Hot methods are compiled to machine code
• Stored in code cache

Benefits

• Huge performance improvement

------------------------------------------------------------

## Escape Analysis

JVM optimization technique.

Determines if object escapes thread scope.

If not → allocate on **stack instead of heap**.

Benefits

• Faster allocation
• Less garbage collection

------------------------------------------------------------

## Garbage Collection Algorithms

### Mark and Sweep

```
Mark reachable objects
Sweep unreachable objects
```

Problem

• Memory fragmentation

------------------------------------------------------------

### Generational GC

Heap divided into

```
Young Generation
Old Generation
```

Young generation collections are fast.

------------------------------------------------------------

### G1 Garbage Collector

Modern production GC.

Heap divided into regions.

Benefits

• Predictable pause times
• Parallel GC

------------------------------------------------------------

### ZGC

Low latency garbage collector.

Pause time

```
< 10ms
```

Used in high performance systems.

------------------------------------------------------------

## Common Memory Leaks

1. Static collections

```java
static List<Object> cache = new ArrayList<>();
```

2. Unclosed resources

3. Listeners not deregistered

------------------------------------------------------------

# PART 14 — ADVANCED JAVA CONCURRENCY

## Executor Framework

ExecutorService manages thread pools.

Example

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

executor.submit(() -> {
    System.out.println("Task executed");
});
```

------------------------------------------------------------

## ForkJoinPool

Used for **parallel divide and conquer algorithms**.

Example

```java
ForkJoinPool pool = new ForkJoinPool();
pool.submit(() -> {
    System.out.println("Parallel task");
});
```

Uses **work stealing algorithm**.

------------------------------------------------------------

## Atomic Variables

Avoid locks using CAS.

Example

```java
AtomicInteger counter = new AtomicInteger(0);

counter.incrementAndGet();
```

------------------------------------------------------------

## Locks API

```java
ReentrantLock lock = new ReentrantLock();

lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

------------------------------------------------------------

# PART 15 — SPRING INTERNALS

## How IoC Container Works

Spring container performs:

1. Scan classes
2. Create bean definitions
3. Instantiate beans
4. Inject dependencies

------------------------------------------------------------

## Component Scanning

```java
@ComponentScan("com.app")
```

Spring scans package and registers beans.

------------------------------------------------------------

## Bean Creation Pipeline

```
BeanDefinition
     │
     ▼
Instantiation
     │
     ▼
Dependency Injection
     │
     ▼
BeanPostProcessor
     │
     ▼
Ready Bean
```

------------------------------------------------------------

## Spring Proxy Mechanism

Spring uses proxies for

• AOP
• Transactions
• Security

Types

### JDK Dynamic Proxy

Used when class implements interface.

### CGLIB Proxy

Used when class has no interface.

------------------------------------------------------------

# PART 16 — SPRING BOOT INTERNALS

## Spring Factories Loader

Spring Boot loads auto configuration using

```
META-INF/spring.factories
```

Example entry

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

------------------------------------------------------------

## Conditional Beans

Spring Boot activates configuration only if conditions match.

Examples

```java
@ConditionalOnClass
@ConditionalOnBean
@ConditionalOnMissingBean
```

------------------------------------------------------------

## Starter Dependencies

Starters bundle common dependencies.

Example

```
spring-boot-starter-web
```

Includes

• Spring MVC
• Jackson
• Tomcat

------------------------------------------------------------

# PART 17 — PRODUCTION REST API DESIGN

## REST Maturity Model

Level 0 — Single endpoint

Level 1 — Resources

Level 2 — HTTP verbs

Level 3 — Hypermedia

------------------------------------------------------------

## Idempotency

Operations that can be repeated safely.

```
GET
PUT
DELETE
```

------------------------------------------------------------

## API Versioning

Strategies

```
/api/v1/users
```

or

```
Header Versioning
```

------------------------------------------------------------

## Rate Limiting

Limit API usage.

Example algorithms

• Token Bucket
• Leaky Bucket
• Fixed Window

------------------------------------------------------------

# PART 18 — DATABASE DEEP DIVE

## Indexing Internals

Most databases use **B‑Tree indexes**.

Structure

```
Root
  ├─ Internal Nodes
  └─ Leaf Nodes
```

Benefits

• Fast search
• Logarithmic lookup

------------------------------------------------------------

## Query Optimization

Bad query

```sql
SELECT * FROM users WHERE name = 'shiv';
```

Better with index

```sql
CREATE INDEX idx_users_name ON users(name);
```

------------------------------------------------------------

## Transactions Example

```sql
BEGIN;
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;
```

------------------------------------------------------------

# PART 19 — SPRING SECURITY INTERNALS

## Security Filter Chain

```
Request
   │
   ▼
SecurityContextPersistenceFilter
   │
   ▼
UsernamePasswordAuthenticationFilter
   │
   ▼
AuthorizationFilter
   │
   ▼
Controller
```

------------------------------------------------------------

## JWT Filter Example

```java
public class JwtFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) {
        // extract token
        // validate token
        // set authentication
    }
}
```

------------------------------------------------------------

## OAuth2 Authorization Code Flow

```
User
  │
  ▼
Client Application
  │
  ▼
Authorization Server
  │
  ▼
Authorization Code
  │
  ▼
Access Token
  │
  ▼
Resource Server
```

------------------------------------------------------------

# PART 20 — PRODUCTION BACKEND ARCHITECTURE

Typical Spring Boot production stack

```
Client
   │
   ▼
API Gateway
   │
   ▼
Spring Boot Services
   │
   ├─ Redis Cache
   ├─ Kafka Message Queue
   ├─ PostgreSQL Database
   └─ Elasticsearch
```

------------------------------------------------------------

END OF SENIOR JAVA BACKEND ENGINEER HANDBOOK


