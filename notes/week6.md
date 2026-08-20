# Week 6

## Day 1

### Sprint 3 Implementation: Material Consumption & Reservation Lifecycle

#### Important aspects

##### User Story Implementation
Implemented the physical material consumption workflow within the **Inventory & Procurement** domain to track parts used during assembly operations and update reservation statuses in real time.

##### Main Principles & Components Implemented
*   **Reservation Lifecycle Transitions:** Extended `ReservationService` to handle status transitions from `ACTIVE` to `CONSUMED`, enforcing strict domain validation rules (`409 Conflict` on invalid reservation states).
*   **Atomic Stock Mutex & Invariant Preservation:** Safely decremented `reserved_quantity` within a `@Transactional` boundary without violating the continuous stock invariant.
*   **Idempotent Consumption Tracking:** Ensured reliable consumption updates across assembly stages, preventing duplicate decrements on repeated delivery events.
*   **Post-Consumption Threshold Verification:** Automated post-consumption low-stock evaluations (`availableQuantity < minimumQuantity`), conditionally triggering outbound `inventory-low-stock` events.

## Day 2

### Code Refactoring, Custom Error Handling & Demo Preparation

#### Important aspects

##### Enterprise Error Handling Strategy
Standardized exception management across the service by replacing generic runtime exceptions with structured custom domain exceptions.
*   **Custom Business Exceptions:** Implemented a unified `BusinessException` hierarchy with explicit machine-readable error codes (e.g., `PART_NOT_FOUND`, `INVALID_RESERVATION_STATUS`, `DUPLICATE_CODE`) and user-friendly error messages.
*   **Centralized Exception Mapping:** Integrated Jakarta REST `ExceptionMapper` components to translate domain exceptions into consistent, predictable JSON error payloads with appropriate HTTP status codes (`400 Bad Request`, `404 Not Found`, `409 Conflict`).

##### Clean Code & Architecture Polishing
*   **Codebase Hardening:** Applied clean code principles across boundary, control, and entity layers by removing dead code, optimizing import statements, and clarifying method responsibilities.
*   **Logging & Observability Standards:** Standardized structured logging across REST endpoints and Kafka consumers to ensure transparent request tracing during end-to-end integration flows.

##### Final Sprint Preparation & Demo Readiness
*   **End-to-End Verification:** Validated complete service workflows locally (parts catalog registration, demand ingestion, atomic stock reservation, physical consumption, and low-stock event emissions).
*   **Demo Alignment:** Prepared seed data and verified OpenAPI/Swagger documentation to ensure a seamless cross-team demonstration in the upcoming Sprint Demo session.

## Day 3

### Demo Dry Run, Workflow Architecture & Video Recording

#### Important aspects

##### Demo Dry Run & End-to-End Simulation
*   **Pipeline Rehearsal:** Executed a full dry-run simulation of the cross-service **Mini Factory** workflow, validating the integration between REST API endpoints and asynchronous Kafka event streams.
*   **Scenario Verification:** Verified edge cases across data flows, ensuring all state changes (demand ingestion, reservations, and inventory consumption) reflected accurately across services.

##### Presentation Materials & Video Walkthrough
*   **System Architecture Slide Deck:** Prepared a comprehensive presentation detailing the domain boundaries, data models, stock invariants, and the event-driven communication strategy.
*   **Application Video Demonstration:** Recorded a step-by-step video demonstration showcasing live interactions, API calls via Swagger/OpenAPI, and real-time Kafka event processing for archival and presentation backup.

---

## Day 4

### Cross-Departmental Live Demo & Final Retrospective

#### Important aspects

##### Live Multi-Service Demonstration
*   **Cross-Team Presentation:** Successfully delivered the live project demonstration alongside all 8 participating microservice teams across diverse technology stacks.
*   **Live Orchestration:** Showcased the complete product lifecycle from production planning to material reservation, delivery ingestion, and final assembly consumption in real time.
*   **Technical Defense & Q&A:** Answered architectural inquiries regarding data consistency, idempotency handling with `eventId`, and transaction propagation under concurrent loads.

##### Post-Demo Reflections & Feedback
*   **Integration Success:** Evaluated the overall performance of the distributed system, validating the strengths of clear API contracts and asynchronous decoupling.
*   **Technical Feedback:** Gathered constructive feedback from mentors and peers regarding error resilience, API standardization, and observability practices.

---

## Day 5

### Program Offboarding, Equipment Handover & Final Takeaways

#### Important aspects

##### Formal Program Wrap-Up & Offboarding
*   **Asset Handover:** Completed administrative check-out procedures, handed over project assets and development equipment, and concluded sprint lifecycle tracking.
*   **Team Debriefing:** Conducted a final round of retrospective discussions with mentors and colleagues to review overall project velocity, team dynamics, and delivery milestones.

##### Key Takeaways & Program Summary
*   **Technical Mastery:** Acquired solid, practical expertise in **Java 21, Quarkus, Hibernate ORM with Panache, Flyway, Apache Kafka, and Docker**.
*   **Distributed Systems Architecture:** Gained direct, hands-on experience solving real-world challenges in Event-Driven Architecture, eventual consistency, concurrency locking, and domain boundary design (DDD).
*   **Agile & Professional Growth:** Built strong habits in sprint planning, assertive technical communication, clean code practices, and cross-functional team collaboration.