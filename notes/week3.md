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
package com.example.greeting;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
package com.example.person;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import java.time.LocalDate;

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