# Week 5

## Day 1

### Sprint 2 Implementation: Material Reservation & Event Handshake

#### Important aspects

##### User Story Implementation
Implemented the material reservation engine within the **Inventory & Procurement** domain to guarantee stock availability when a vehicle production plan is triggered.

##### Main Principles & Components Implemented
*   **Transactional Stock Mutex & Atomic Allocation:** Built transactional material reservation logic guarded by `@Transactional` blocks, atomically transferring stock quantities (`availableQuantity -= quantity` and `reservedQuantity += quantity`) while maintaining the strict stock invariant (`available_quantity = initial - reserved - consumed`).
*   **Reservation Domain Entities:** Created `Reservation` domain records with an initial `ACTIVE` status to lock specific parts against vehicle production plans (`planId`) and vehicle instances (`vehicleId`).
*   **Event-Driven Integration & Outbound Handshake:** Connected the `production-planned` consumer to the reservation service and configured outbound event publishing to the `parts-reserved` topic (`PartsReservedEvent` / `PartsReservedPayload`) for downstream assembly handoffs.
*   **Query API & Idempotency:** Exposed REST verification endpoints (`GET /api/reservations?vehicleId={vehicleId}`) and ensured full event-processing idempotency based on `eventId`.
*   **Quality Assurance & DOD Compliance:** Wrote unit, integration, and high-concurrency reservation tests to verify invariant protection under parallel requests.

## Day 2

### Course 15: Transaction Management in Quarkus

#### Important aspects

##### Transaction Fundamentals & Lifecycle
A transaction is a single logical unit of work that guarantees data integrity through all-or-nothing execution.
1. **Begin Transaction:** Initializes the transaction boundary.
2. **Execute Operations:** Performs database read/write actions.
3. **Validate & Check Errors:** Evaluates business rules and catches runtime exceptions.
4. **Commit / Rollback:** Persists changes if successful; reverts all modifications if an error occurs.
5. **Release Resources:** Releases database connections and locks.

##### ACID Guarantees
* **Atomicity:** All operations within the transaction succeed, or none are applied.
* **Consistency:** Data strictly transitions from one valid state to another, respecting all constraints.
* **Isolation:** Concurrent transactions execute independently without uncommitted cross-visibility.
* **Durability:** Committed changes persist permanently, surviving crashes and system failures.

##### Transaction Types & Propagation Rules
Quarkus uses the standard **Jakarta Transactions API (`jakarta.transaction.Transactional`)** for declarative boundary management.

* **REQUIRED (Default):** Joins an active transaction or creates a new one if none exists.

```java
@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    InventoryRepository inventoryRepository;

    // Default: Joins existing transaction or creates a new one
    @Transactional
    public void placeOrder(Order order) {
        orderRepository.persist(order);
        inventoryRepository.decreaseStock(order.getProductId(), order.getQuantity());
    }
}
```

* **REQUIRES_NEW:** Suspends any active transaction and executes within a separate, isolated transaction.

```java
@ApplicationScoped
public class AuditService {

    // Runs in a separate, isolated transaction boundary
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void logEvent(String eventType, String payload) {
        AuditEvent event = new AuditEvent();
        event.type = eventType;
        event.payload = payload;
        event.persist();
    }
}
```

* **MANDATORY:** Requires an existing transaction; throws an exception if none is present.
* **SUPPORTS:** Executes within a transaction if present; otherwise runs non-transactionally.
* **NOT_SUPPORTED:** Suspends any active transaction and executes non-transactionally.
* **NEVER:** Throws an exception if executed within an active transaction.

##### Concurrency Control & Locking Strategies
* **Optimistic Locking:** Allows concurrent access without immediate database locks. Uses a `@Version` field on the entity to detect conflicting updates at commit time (throws `OptimisticLockException`).

```java
@Entity
@Table(name = "accounts")
public class Account extends PanacheEntity {
    public Long balance;

    @Version
    public Long version; 
}
```

* **Pessimistic Locking:** Obtains an immediate database-level lock (`PESSIMISTIC_WRITE` or `PESSIMISTIC_READ`) when querying the record, blocking concurrent modifications until the transaction completes.

```java
@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    @Transactional
    public Account findByIdForUpdate(Long id) {
        return find("id", id).withLock(LockModeType.PESSIMISTIC_WRITE).firstResult();
    }
}
```

##### Architectural Risks & Trade-offs
* **Problems Solved:** Ensures multi-repository consistency, prevents concurrency anomalies (like lost updates), and manages automatic rollback on failure.
* **Performance Risks:** Long-running transactions hold database locks and connections too long, leading to latency, connection pool exhaustion, and deadlocks. Keep transactional scopes as short as possible.

##### Quarkus vs. Spring Transaction Management

| Feature | Quarkus (Jakarta) | Spring Framework |
| :--- | :--- | :--- |
| **Annotation Source** | `jakarta.transaction.Transactional` | `org.springframework.transaction.annotation.Transactional` |
| **Default Rollback** | Rollback on unchecked exceptions (`RuntimeException`, `Error`) | Rollback on unchecked exceptions (configurable with `rollbackFor`) |
| **Underlying Stack** | CDI + Hibernate ORM / Panache + Narayana JTA | Spring AOP + Spring Data JPA / PlatformTransactionManager |

## Day 3

### Course 16: Monolithic vs. Microservices Architecture

#### Important aspects

##### Architectural Paradigms Compared
*   **Monolithic Architecture:** A single, unified deployment unit where all business modules share the same process, runtime memory, and database. Easy to develop and test initially, but difficult to scale or release independently as domain complexity grows.
*   **Microservices Architecture:** An architectural pattern that decomposes an application into small, autonomous, single-purpose services. Each service owns its domain logic, persistent storage, and deployment pipeline, communicating across the network via REST APIs or asynchronous messaging.

##### Selection Criteria & Decision Matrix
*   **Choose a Monolith when:**
    *   Working with small teams or early-stage products (MVPs) prioritizing rapid delivery and iteration.
    *   The business domain is straightforward with minimal need for independent, targeted scaling.
    *   DevOps infrastructure, containerization expertise, and distributed system observability are limited.
*   **Choose Microservices when:**
    *   Multiple cross-functional teams require independent development, release, and deployment cycles.
    *   Specific high-demand components require granular, service-level scaling.
    *   Clear domain boundaries (Bounded Contexts) exist alongside mature CI/CD pipelines, container orchestration, and distributed tracing capabilities.

##### Distributed Systems Challenges
*   **Network Latency & Resilience:** Managing network calls, cascading failures, timeouts, and partial outages across service boundaries.
*   **Data Consistency:** Maintaining data integrity across independent service databases without shared database transactions (relying on eventual consistency and Saga patterns).
*   **Operational Overhead:** Increased complexity in distributed logging, correlation IDs, distributed tracing (e.g., OpenTelemetry), API versioning, and service discovery.

##### Inter-Service Communication Models
*   **Synchronous (REST):** Direct request-response communication (e.g., via MicroProfile REST Client). Simple to implement and reason about, but introduces runtime coupling and dependency on target service availability.

```java
@RegisterRestClient(configKey = "inventory-service")
public interface InventoryClient {

    @GET
    @Path("/stock/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    StockResponse getStock(@PathParam("productId") String productId);
}

@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    @Inject
    @RestClient
    InventoryClient inventoryClient;

    @GET
    @Path("/stock/{productId}")
    public StockResponse getStock(@PathParam("productId") String productId) {
        return inventoryClient.getStock(productId);
    }
}
```

*   **Asynchronous (Event-Driven / Kafka):** Message-driven communication via event streams. Services react to published events independently, maximizing system resilience, throughput, and decoupling, while introducing eventual consistency and schema governance requirements.

```java
@ApplicationScoped
public class OrderEventProducer {

    @Inject
    @Channel("order-events")
    Emitter<OrderCreatedEvent> orderEventEmitter;

    public void publishOrderCreated(OrderCreatedEvent event) {
        orderEventEmitter.send(event);
    }
}

@ApplicationScoped
public class InventoryEventConsumer {

    @Inject
    InventoryService inventoryService;

    @Incoming("order-events")
    public void onOrderCreated(OrderCreatedEvent event) {
        inventoryService.decreaseStock(event.productId(), event.quantity());
    }
}
```

## Day 4

### Course 17: Communication Essentials & Team Collaboration

#### Important aspects

##### Core Communication Styles
*   **Passive:** Avoiding direct confrontation, suppressing personal needs or opinions, and yielding to others at the expense of clarity and efficiency.
*   **Aggressive:** Expressing views forcefully or disrespectfully, ignoring others' input, and prioritizing self-interest over team consensus.
*   **Passive-Aggressive:** Expressing dissatisfaction indirect or subtle ways, leading to confusion, hidden friction, and broken trust within a team.
*   **Assertive (Target Standard):** Direct, transparent, and confident communication that clearly states needs, boundaries, and ideas while actively respecting others' perspectives.

##### Practical Assertiveness in Software Engineering
*   **Technical Blockers & Capacity:** Raising technical roadblocks early and setting realistic sprint commitments without fear of hesitation or pushback.
*   **Constructive Feedback Loops:** Delivering and receiving actionable, respectful feedback during code reviews, architectural discussions, and retrospectives.
*   **Active Listening & Empathy:** Listening intently to team members' viewpoints before proposing solutions, fostering a collaborative, non-defensive environment.

##### Impact on Agile Team Dynamics
*   **Conflict Resolution:** Addressing misunderstandings early and directly before they escalate into team friction or project delays.
*   **Psychological Safety:** Contributing to an environment where team members feel comfortable asking questions, admitting mistakes, and sharing innovative ideas.