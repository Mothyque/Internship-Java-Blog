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