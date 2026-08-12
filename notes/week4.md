# Week 4

## Day 1

### Sprint 1 Implementation: Parts Catalog & Core Domain API

#### Important aspects

##### User Story Implementation
Started development on the **Parts Catalog** feature to enable core material management across the factory ecosystem.

##### Main Principles & Components Implemented
*   **Domain & Persistence Layer:** Defined the `Part` entity and implemented the Panache Repository pattern for database access, featuring automatic business ID generation and unique `partCode` enforcement.
*   **Layered Architecture & DTO Pattern:** Separated domain entities from the API layer using dedicated request (`CreatePartRequest`) and response (`PartResponse`) DTOs, managed by a service layer for validation and business logic.
*   **RESTful Endpoints & Error Handling:** Exposed REST endpoints (`POST /api/parts`, `GET /api/parts`, `GET /api/parts/{partId}`) with proper HTTP status codes (including `409 Conflict` for duplicate part codes).
*   **API Documentation & Testing (DoD):** Documented all endpoints using OpenAPI/Swagger annotations and added unit/integration tests to satisfy the Definition of Done.

## Day 2

### Course 12: Threading Model & Concurrency in Quarkus

#### Important aspects

##### Reactive Engine & Event Loop
Quarkus is built on top of **Eclipse Vert.x** and **Netty**, using a reactive event-driven architecture at its core. By default, incoming requests are handled by non-blocking **Event Loop threads**.

*   **Event Loop Threads:** Small, fixed number of threads (usually equal to CPU cores). Intended for CPU-bound, quick, and non-blocking I/O operations.
*   **Golden Rule:** **Never block an Event Loop thread!** Performing blocking operations (e.g., standard JDBC calls, long-running computations, thread sleeping) on the event loop starves the engine and severely degrades performance.

##### Worker Thread Pool
To execute traditional synchronous and blocking workloads without stalling the event loop, Quarkus offloads execution to a dedicated **Worker Thread Pool**.

*   **Worker Threads:** Larger, dynamic thread pool designed for blocking I/O (e.g., blocking database queries via standard Hibernate ORM/JDBC).
*   **Execution Strategy:** Annotating methods with `@Blocking` or using traditional imperative JAX-RS signatures automatically dispatches execution to a worker thread.

##### Virtual Threads 
With modern Java versions, Quarkus seamlessly integrates with **Virtual Threads** to handle high-concurrency blocking workloads efficiently.

*   **Lightweight Concurrency:** Virtual threads allow thousands of concurrent blocking requests to run with near-zero overhead without exhausting OS-level thread limits.
*   **Usage in Quarkus:** Simply annotate endpoints or service methods with `@RunOnVirtualThread` to execute blocking code on a virtual thread rather than a traditional platform worker thread.

## Day 3

### Course 13: Stress Management & Sustainable Productivity

#### Important aspects

##### Productivity & Focus Techniques
*   **Pomodoro Technique:** Working in structured, focused intervals (e.g., 20 minutes of deep work followed by a 5-minute break) to sustain cognitive energy, minimize distractions, and prevent fatigue during complex task execution.
*   **Pacing & Micro-Breaks:** Taking regular, short pauses to reset attention spans and maintain consistent productivity throughout long development sprints.

##### Somatic & Stress Regulation Tools
*   **4-4-6 Breathing Exercise:** A practical breathwork pattern (4-second inhale, 4-second hold, 6-second exhale) designed to trigger the parasympathetic nervous system, rapidly reducing acute anxiety and restoring composure under high-pressure project situations or tight deadlines.

##### Teamwork & Workplace Well-Being Best Practices
*   **Open Communication & Boundaries:** Proactively raising blockers and setting realistic capacity expectations within the team to avoid overload and burnout.
*   **Psychological Safety:** Fostering a collaborative team environment where asking questions, seeking support, and sharing workload concerns are encouraged.
*   **Sustainable Execution:** Prioritizing steady, sustainable work patterns over constant crunches to maintain code quality and long-term project success.

## Day 4

### Course 14: Data Querying Paradigms in Quarkus & JPA

#### Important aspects

##### ORM & JPA Fundamentals
*   **Object-Relational Mapping (ORM):** Bridges object-oriented Java code with relational database schemas, automating object-to-table transformations and eliminating manual SQL boilerplate.
*   **Java Persistence API (JPA / Jakarta Persistence):** The enterprise standard specification for Java data persistence, with **Hibernate** serving as the industry-standard underlying implementation.

```java
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
```

##### Core Entity Annotations
*   `@Entity` & `@Table`: Marks a Java class as a persistent database entity and maps it to a specific table.
*   `@Id` & `@GeneratedValue`: Defines the primary key field and its automated generation strategy (e.g., `IDENTITY`, `SEQUENCE`).
*   `@Column`: Customizes column mapping details (name, nullability, length).
*   `@ManyToOne` / `@OneToMany`: Establishes entity relationships and configures fetch types (e.g., `FetchType.LAZY` to avoid premature data loading).
*   `@CreationTimestamp` / `@UpdateTimestamp`: Automatically manages record creation and modification timestamps.

##### Querying Paradigms Comparison

*   **Panache Repository Pattern:** Quarkus-native abstraction over Hibernate ORM that drastically reduces DAO boilerplate while providing out-of-the-box helper methods (`listAll()`, `findById()`, `persist()`).

```java
@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    // Custom finder method extending built-in Panache capabilities
    public List<Product> findByPriceThreshold(double minPrice) {
        return list("price > ?1", minPrice);
    }
}
```
*   **JPQL (Jakarta Persistence Query Language):** Object-oriented query language that operates directly on Java entities and properties rather than database tables and columns.
*   **Criteria API:** A programmatic, type-safe query builder checked at compile time, ideal for constructing dynamic filters and surviving IDE refactoring.
*   **Native SQL:** Raw SQL queries executed directly against the database engine, reserved for performance-critical paths, database-specific functions, or complex analytical reporting.

```java
@ApplicationScoped
public class ProductQueryService {

    @Inject
    EntityManager entityManager;

    // JPQL Example
    public List<Product> findByCategoryNameJpql(String categoryName) {
        TypedQuery<Product> query = entityManager.createQuery(
            "SELECT p FROM Product p WHERE p.category.name = :categoryName", Product.class);
        query.setParameter("categoryName", categoryName);
        return query.getResultList();
    }

    // Criteria API Example (Type-safe)
    public List<Product> findAllProductsCriteria() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);
        cq.select(product);
        return entityManager.createQuery(cq).getResultList();
    }

    // Native SQL Example
    @SuppressWarnings("unchecked")
    public List<Object[]> findHighValueProductsNative(double minPrice) {
        return entityManager.createNativeQuery(
            "SELECT p.id, p.name, p.price FROM products p WHERE p.price > ?1")
            .setParameter(1, minPrice)
            .getResultList();
    }
}
```

##### Query Selection Decision

| Query Approach | Primary Use Case | Key Advantages |
| :--- | :--- | :--- |
| **Panache Repository** | Standard Quarkus CRUD operations | Minimal boilerplate, rapid development |
| **JPQL** | Standard relational queries | Object-oriented, high readability |
| **Criteria API** | Dynamic filtering & multi-conditional queries | Compile-time type safety, refactoring-proof |
| **Native SQL** | Complex reporting & DB-specific optimizations | Maximum execution performance, direct DB feature access |

## Day 5

### Sprint 1 Completion & Sprint 2 Execution

#### Important aspects

##### Task 2 (Sprint 1): Supplier Management Domain
Completed the final core deliverable for Sprint 1, establishing the supplier integration component of the **Inventory & Procurement** domain.
*   **Supplier Persistence & Identifier Generation:** Mapped the `Supplier` entity with automatic unique `supplierCode` generation and unique constraint enforcement.
*   **Layered Service Architecture:** Implemented standard DTO mappings (`CreateSupplierRequest`, `SupplierResponse`) managed through a dedicated service layer for validation and business logic execution.
*   **RESTful Interfaces & OpenAPI Spec:** Developed `POST /api/suppliers`, `GET /api/suppliers`, and `GET /api/suppliers/{supplierId}` endpoints with proper error responses (e.g., `409 Conflict` on duplicates) and OpenAPI documentation.
*   **Quality Assurance & DoD Compliance:** Developed unit and integration tests to fulfill the Definition of Done.

##### Domain Schema Refactoring (Sprint 2 Transition)
Initiated Sprint 2 by performing targeted entity and database schema refactoring, adding missing core fields to align with cross-departmental specifications before proceeding with core business logic.

##### Task 1 (Sprint 2): Event-Driven Material Demand Consumption
Implemented the event-driven consumer for the **Inventory & Procurement** domain to automatically ingest production plans and track material demand for upcoming vehicle manufacturing.
*   **Reactive Kafka Consumer:** Configured a Quarkus reactive message consumer using `@Incoming("production-planned")` assigned to a dedicated consumer group (`inventory-production-planned`).
*   **Event Deserialization & Payload Validation:** Deserialized incoming `EventEnvelope` payloads and validated message integrity prior to executing business logic.
*   **Idempotency Enforcement:** Implemented deduplication logic driven by `eventId` to guarantee safe, idempotent processing during message retries or duplicate deliveries.
*   **Domain Linking & Persistence:** Mapped and persisted `MaterialDemand` records linked to their corresponding Production Plan, Order, and Vehicle identifiers.
*   **Traceability & Testing (DoD):** Integrated structured operational logging for traceability and authored consumer integration tests to satisfy the Definition of Done.