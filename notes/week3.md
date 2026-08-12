# Week 3

## Day 1: Team Project Orientation & Architecture Alignment

### Important aspects

##### Team Project Overview: Mini Factory Simulation
Today's session focused on launching our internal intern team project. The objective is to build a simulated **Mini Factory** system driven by **8 independent microservices** running on **Quarkus, PostgreSQL, Kafka, and Docker**. The workflow spans from initial customer orders and inventory/procurement to AGV logistics, multi-stage assembly, quality control, and a central Digital Twin/Data Warehouse layer.

##### Delivery Framework & Sprint Planning
Development is organized into **4 Agile Sprints**, with a live **Demo session** at the end of each sprint to showcase incremental progress:
*   **Sprint 1 — Foundation:** Setting up repositories, Quarkus build pipelines, coding standards, and initial domain models.
*   **Sprint 2 — Core Logic:** Implementing core business logic, domain-driven rules, and persistent storage.
*   **Sprint 3 — Integration & Analytics:** Connecting services via Kafka (asynchronous events) and REST APIs (synchronous queries), alongside analytics integration.
*   **Sprint 4 — Final Demonstration & Buffer:** End-to-end testing, performance optimization, bug fixes, and final presentation.

##### Architectural Principles & Tech Stack
*   **Domain-Driven Design (DDD):** Emphasizes clear service boundaries, API contracts, and complete database isolation across teams.
*   **Communication:** Dual-layer communication strategy utilizing **Kafka** for event-driven async workflows and **REST APIs** for synchronous validation.
*   **My Department Focus:** Assigned to the **Inventory & Procurement** domain, responsible for managing parts catalogs (e.g., automotive components), tracking stock availability, and coordinating supplier purchase orders.

## Day 2

### Course 9: Quarkus & Hibernate ORM with Panache

#### Important aspects

##### What is Quarkus?
Quarkus is a modern, cloud-native Java framework tailored specifically for GraalVM and OpenJDK HotSpot. Designed with a **Kubernetes-First** philosophy, it delivers ultra-fast startup times, extremely low memory footprints, and instantaneous live coding capabilities.

##### Quarkus vs. Traditional Java Frameworks (e.g., Spring Boot)
*   **Rapid Startup:** Optimized for serverless and containerized environments.
*   **Low Memory Footprint:** Uses compile-time optimization to minimize heap utilization.
*   **Live Reloading:** Instant developer feedback through continuous background compilation.
*   **Native Compilation:** Compiles Java applications into standalone native executables using **GraalVM**.
*   **Container-Friendly:** Pre-configured Docker files and automatic containerized development environments (Dev Services).

``` java
@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello, Quarkus!";
    }
}
```

##### Quarkus Project Structure

```
my-quarkus-app/
├── src/
│   └── main/
│       ├── java/                (Java source code & REST endpoints)
│       ├── resources/
│       │   └── application.properties (Global & environment configurations)
│       └── docker/              (Dockerfiles for JVM & native images)
```

##### Core Extensions
Quarkus uses modular extensions to add enterprise capabilities without adding unnecessary bloat:
*   `quarkus-resteasy-reactive`: High-performance RESTful web services (JAX-RS / Jakarta REST).
*   `quarkus-hibernate-orm-panache`: Simplified JPA persistence layer.
*   `quarkus-smallrye-health`: Built-in application health checks for Kubernetes probes.
*   `quarkus-micrometer`: Production metrics and monitoring integration.
*   `quarkus-oidc`: Enterprise security integration via OpenID Connect.

##### REST API Design Principles
*   **Stateless Communication:** Built using standard HTTP methods (`GET`, `POST`, `PUT`, `DELETE`).
*   **Clear Resource Mapping:** Standardized URL endpoints (e.g., `GET /users` returns user resources).
*   **Separation of Concerns:** Acts as the communication bridge between client interfaces and core backend microservices.

##### Observability & Monitoring
Out-of-the-box endpoints provided by SmallRye and Micrometer extensions:
*   **Health Endpoints:** `/q/health`, `/q/health/live` (Liveness probe), `/q/health/ready` (Readiness probe).
*   **Metrics Endpoint:** `/q/metrics` formatted for Prometheus scraper collection.

##### Hibernate ORM with Panache
Panache simplifies database persistence in Quarkus by drastically cutting down on boilerplate JPA code and DAO setup.

###### Active Record Pattern (`PanacheEntity`)
In the Active Record pattern, the entity class itself contains both database field mappings and static methods for direct CRUD operations (`listAll()`, `findById()`, `persist()`).

``` java
@Entity
public class Person extends PanacheEntity {
    public String name;
    public LocalDate birth;

    public static Person findByName(String name) {
        return find("name", name).firstResult();
    }
}
```

##### Quarkus Best Practices
*   **Use DTOs:** Always decouple database entities from public API request and response bodies.
*   **Input Validation:** Validate incoming JSON payloads using Jakarta Bean Validation (`@NotNull`, `@Size`, `@Valid`).
*   **Externalize Configuration:** Store dynamic environment properties inside `application.properties` or environment variables.
*   **Keep Controllers Thin:** Offload business rules to specialized application services.
*   **Proactive Monitoring:** Always expose liveness and readiness health checks in cloud deployments.

## Day 3

### Course 10: Database Migrations with Flyway in Quarkus

#### Important aspects

##### What is Flyway?
Flyway is an open-source database migration tool designed to version-control relational databases in JVM environments. It allows teams to track, manage, and apply schema changes reliably across different environments (local development, testing, and production).

##### Why Use Flyway?
*   **Version Control for DB Schemas:** Keeps database structures synchronized with the application source code.
*   **Automated Execution:** Applies pending migrations automatically during application startup.
*   **Reproducibility & Safety:** Ensures every environment undergoes the exact same migration sequence, preventing manual SQL execution errors.

##### Migration Naming Conventions & Versioning
Flyway relies on strict naming conventions to determine execution order and script types. Migration files are placed in the `src/main/resources/db/migration` directory.

###### File Naming Format
`V<Version>__<Description>.sql` (e.g., `V1.0.0__create_persons_table.sql`)

###### Key Components
*   **Prefix:** `V` for Versioned migrations (runs once in order), `R` for Repeatable migrations (re-executed when checksum changes), or `U` for Undo migrations.
*   **Version:** Numerical or dot-separated version string (e.g., `1`, `1.1`, `2.0.0`).
*   **Separator:** **Two underscores (`__`)** are required between the version and the description.
*   **Description:** Text describing the migration (underscores or spaces separate words).
*   **Extension:** `.sql` file format.

##### Setting Up Flyway in Quarkus

###### Dependencies
Add the following extensions to your project configuration:
1.  **Flyway Extension:** `quarkus-flyway`
2.  **JDBC Driver Extension:** e.g., `quarkus-jdbc-postgresql`
3.  **Database-Specific Flyway Module:** Required for non-embedded databases (e.g., `flyway-database-postgresql`).

###### Core Configuration
To run schema migrations automatically when the Quarkus application starts, enable the startup flag in `application.properties`:

```properties
# Enable automatic migration execution at application startup
quarkus.flyway.migrate-at-start=true

# Optional: Ensure Flyway creates schemas if they do not exist
quarkus.flyway.create-schemas=true
```

## Day 4

### Course 11: Apache Kafka Fundamentals

#### Important aspects

##### What is Apache Kafka?
Apache Kafka is an open-source, distributed event streaming platform designed to handle real-time data feeds with high throughput, low latency, and strong fault tolerance. At its core, Kafka acts as a **durable, distributed commit log**. Messages persist on disk for a configurable retention period, even after being read by consumers.

##### Post Office Analogy
*   **Asynchronous Processing:** You drop a letter in a mailbox and leave—you do not wait for the recipient to open it immediately.
*   **Decoupled Consumption:** The recipient reads the letter whenever they are ready.
*   **Message Persistence:** The post office/mailbox safely stores the letter in the meantime.
*   **System Decoupling:** Producers (senders) do not know or care who listens to or processes their messages.

##### Core Architecture & Concepts
*   **Broker:** A single Kafka server that stores data and serves client requests.
*   **Cluster:** A group of interconnected brokers working together to provide high availability and load balancing.
*   **Topic:** A logical stream or category of messages. Topics are partitioned across brokers for scalability.
*   **Partition:** A physical subdivision of a topic stored on a specific broker. Partitions enable horizontal scaling and parallel processing.
*   **Offset:** An immutable, sequentially incrementing integer assigned to each message within a partition. It uniquely identifies a message's position.
*   **Producer:** An application/client that publishes (writes) events to a Kafka topic.

``` java
@ApplicationScoped
public class OrderProducer {

    @Inject
    @Channel("orders-out")
    Emitter<String> orderEmitter;

    public void sendOrder(String orderJson) {
        orderEmitter.send(orderJson);
    }
}
```

*   **Consumer:** An application/client that subscribes to and processes events from a Kafka topic.

``` java
@ApplicationScoped
public class OrderConsumer {

    @Incoming("orders-in")
    public void processOrder(String orderJson) {
        System.out.println("Received order event: " + orderJson);
    }
}
```

*   **Replication Factor:** The total number of copies made of each partition across different brokers to prevent data loss if a broker fails.

##### The Parallelism Rule (Consumer Groups)
The number of active consumers in a single consumer group **cannot exceed** the total number of partitions in the target topic.
*   If `Consumers == Partitions`: Each consumer reads from exactly 1 partition (ideal state).
*   If `Consumers > Partitions`: The extra consumers remain idle/inactive until another consumer drops.
*   If `Consumers < Partitions`: Some consumers will read from multiple partitions.

##### Message Lifecycle Route
1. **Creation:** The producer constructs a message payload and an optional partition key.
2. **Partition Selection:** Kafka assigns the message to a partition based on the key (hash-based) or round-robin algorithm.
3. **Offset Assignment:** The message receives a unique offset within its partition.
4. **Replication:** The leader partition replicates the message to follower partitions across other brokers.
5. **Consumption:** Consumers within a consumer group read the message from their assigned partition.
6. **Offset Commit:** The consumer commits its processed offset back to Kafka to mark successful processing.

##### Delivery Guarantees

| Guarantee | Data Loss Possible? | Duplicates Possible? | Typical Use Cases |
| :--- | :--- | :--- | :--- |
| **At-most-once** | Yes | No | Non-critical logs, real-time metrics |
| **At-least-once** | No | Yes | Standard enterprise default (requires idempotent handling) |
| **Exactly-once** | No | No | Financial transactions, strict payment workflows |

*   **Production Standard:** **At-least-once** delivery combined with **idempotent consumer logic** (ensuring duplicate messages do not cause duplicate business operations).

##### Operations & Error Handling
*   **Consumer Lag:** The delta between the latest offset written by producers and the offset currently processed by consumers. High or rising lag indicates consumer bottlenecks.
*   **Retry Mechanism:** Automatically retries transient errors a defined number of times before giving up.
*   **Dead Letter Topic (DLT):** Unprocessable messages (poison pills) are routed to a separate DLT for manual analysis, preventing pipeline blockage.

##### Best Practices
*   **Naming Conventions:** Use clear, standardized topic and partition naming conventions.
*   **Key Selection:** Use an explicit message key when strict message ordering is required (messages with the same key always go to the same partition).
*   **Idempotency:** Design consumers to handle duplicate messages safely without side effects.
*   **Proactive Monitoring:** Actively track consumer lag and broker metrics.
*   **Production Replication:** Maintain a `replication.factor >= 3` and `min.insync.replicas = 2` for high availability.
*   **Error Boundaries:** Always implement a Retry strategy + Dead Letter Topic pattern.

##### Common Pitfalls
*   Creating a heavily trafficked topic with only a single partition.
*   Publishing oversized message payloads (Kafka is optimized for small, lightweight events).
*   Assuming global ordering across an entire topic (ordering is **only** guaranteed per partition).
*   Ignoring consumer idempotency in at-least-once delivery pipelines.
*   Scaling consumer instances beyond the total number of topic partitions.

## Day 5

### Project Setup & Sprint 1 Kickoff

#### Important aspects

##### Project Initialization & Agile Planning
Today marked the official transition from theoretical training to practical project execution. Our team focused on laying the foundation for our **Parts & Components Management** microservice within the broader **Mini Factory** ecosystem.

##### Key Activities
*   **Sprint Planning & Task Decomposition:** Broke down the high-level Sprint 1 (Foundation) deliverables into actionable developer tasks, defining clear acceptance criteria and priorities.
*   **Team Work Distribution:** Assigned initial tasks across team members to establish parallel workflows and ownership from day one.
*   **Repository & Project Setup:** Initialized the project repository, set up the base Quarkus application structure, and configured essential build tool dependencies.
*   **Environment & Pipeline Baseline:** Configured standard coding styles, `.gitignore` rules, and local development configurations (`application.properties`) to ensure seamless team collaboration.