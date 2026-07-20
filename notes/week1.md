# Week 1

## Day 1

### Course 1: Introduction

#### Important aspects

##### OOP Principles (Object-Oriented Programming)
*   **Encapsulation:** Keeping data safe inside classes by restricting direct access (using `private` fields and exposing them through `public` getters/setters).
*   **Abstraction:** Hiding complex implementation details and showing only the essential features of an object through simple interfaces or abstract classes.
*   **Inheritance:** Preventing code duplication by establishing a parent-child ("is-a") relationship, allowing a subclass to reuse code from a superclass.
*   **Polymorphism:** The ability of different objects to respond to the same action/method call in their own unique way. This can happen at runtime (method overriding) or compile-time (method overloading).

##### Errors vs. Exceptions
Understanding the Java `Throwable` hierarchy:
*   **Error:** Indicates serious, systemic problems that a reasonable application should not try to catch or handle (e.g., system crashes, JVM running out of resources).
    *   *Example:* `OutOfMemoryError`
*   **Exception:** Represents abnormal conditions/events occurring during runtime that a well-written application should catch and handle gracefully.
    *   *Examples:* `SQLException` (checked exception), `IllegalArgumentException` (unchecked exception).

##### Concurrency
*   The ability of an application to execute multiple tasks/threads concurrently. 
*   It utilizes CPU threads to improve application performance, responsiveness, and resource utilization.

##### Java Streams
*   An efficient, modern, and declarative way to process collections of objects. 
*   They allow you to easily filter, transform (map), and aggregate (reduce) data without writing complex nested loops.

##### Lambda Expressions
*   Anonymous, on-the-spot functions that simplify the code. 
*   They provide a clear and concise way to represent one-method interfaces (Functional Interfaces) using only parameters and a body: `(int arg1, String arg 2) -> { System.out.println("Two arguments: " + arg1 + " and " + arg2); }`.

##### Optional Class
*   A container object used to represent the presence or absence of a value.
*   It prevents `NullPointerException` and forces a cleaner, more explicit null-handling logic.

##### Industry Best Practices
1.  **Use meaningful names:** Variable and method names should clearly describe their intent.
2.  **Keep methods short:** Each method should have a single responsibility (do one thing and do it well).
3.  **Favor composition over inheritance:** Achieve code reuse by designing classes that contain instances of other classes, rather than inheriting from them (flexible "has-a" relationship instead of rigid "is-a").
4.  **Handle exceptions properly:** Never swallow exceptions with empty catch blocks; log them or rethrow them meaningfully.
5.  **Avoid using `null`:** Use `Optional` to handle absent values cleanly.
6.  **Stick to coding conventions:** Write consistent code that aligns with Java's naming and formatting standards.
7.  **Use `final` where possible:** Declare variables, parameters, and classes as `final` when they shouldn't be changed, ensuring immutability and preventing bugs.

##### Practical Exercises
To solidify these concepts, I wrote several code examples showcasing custom Lambda expressions, streams, and basic Concurrency using runnable tasks and threads.

**[View Code](../projects/exercises/Course1.java)** 

---

#### Setup & Environment

After the introductory course, I started setting up my local development environment for the internship. This involved preparing the following tools:
*   **IDE:** IntelliJ IDEA
*   **Database Management:** pgAdmin (PostgreSQL)
*   **Build Tool & SDK:** Apache Maven & Java Development Kit (JDK 17/21)
*   **Containerization:** Rancher Desktop

#####  Issues Encountered & Resolution
*   **Problem:** I was unable to finalize the installation of Rancher Desktop and configure system environment variables for JDK/Maven because my corporate laptop restricted these actions.
*   **Resolution:** The only blocker on Day 1 was not having administrator privileges. I resolved this by submitting an internal IT Support Ticket requesting local admin rights so I can complete the development environment setup tomorrow.

---

## Day 2

### Course 2: Advanced Java Concepts & Streams Deep Dive

#### Important aspects

##### Generics
Generics ensure that your code works with different data types while providing several compile-time benefits:
*   **Compile-time type safety:** Catches type mismatches during compilation rather than throwing a `ClassCastException` at runtime.
*   **Reduced casting:** Eliminates the need for explicit type casting when retrieving objects from collections.
*   **Self-documenting APIs:** Clearly states what type of data a class or method expects and returns.
*   **Reusable algorithms:** Allows you to implement data structures and logic (like sorting or searching) that work independently of the specific data type.

##### Invariance & Wildcards
*   **Invariance:** In Java, generics are invariant. This means `List<Integer>` is **not** a subtype of `List<Number>`, even though `Integer` extends `Number`. If it were allowed, you could accidentally insert a `Double` into a `List<Integer>`, breaking type safety.
*   **Wildcards (?):** Invariance forces us to express our intent explicitly using wildcards when we need flexibility:
    *   `? extends T` (**Producer Extends / PE**): Safe to **read** `T` from the structure, but unsafe to write to it.
    *   `? super T` (**Consumer Super / CS**): Safe to **write** `T` into the structure, but unsafe to read from it (returns `Object`).

##### Choosing the Right Data Structure
*   **List:** Ordered collection that allows duplicates and provides positional (index-based) access.
*   **Set:** Unordered collection that enforces a uniqueness constraint (no duplicates).
*   **Map:** An association of unique keys to values.
*   *Best Practice:* Switch to tree-based structures (like `TreeSet` or `TreeMap`) **only** when sorting or ordering queries are explicitly required, as they carry higher performance overhead ($O(\log n)$ vs $O(1)$).

##### Equality & Hashing
*   **The Contract:** If `a.equals(b)` is true, then `a.hashCode() == b.hashCode()` **must** always be true. If you override one, you must override the other.
*   **Comparisons:** `==` compares memory addresses (reference equality), while `.equals()` compares the actual values/state of the objects.
*   *Best Practice:* Use immutable fields as keys in a `Map`. Prefer **Java Records** (value objects) whenever possible because they generate clean, safe `equals()` and `hashCode()` implementations automatically.

##### Java Records
*   Shallowly **immutable** data carriers introduced to reduce boilerplate.
*   Includes a **canonical constructor**, getters (named after the fields, e.g., `id()`, not `getId()`), `equals()`, `hashCode()`, and `toString()` out of the box.
*   Does **not** have setters.
*   **Limitations & Capabilities:** Cannot extend other classes (since they implicitly extend `java.lang.Record`), but they **can implement** interfaces. You can still add static fields, static methods, and extra instance methods.

##### Date & Time API (java.time)
*   `Instant`: Use when you need to compare, log, or order events globally (represents a single point on the timeline in UTC).
*   `LocalDate`: Use when only the date matters (e.g., birthdays, holidays), completely independent of time zones.
*   `ZonedDateTime`: Use when planning events or meetings for people in a specific time zone (handles Daylight Saving Time automatically).

```java
// Example: Creating a localized meeting in Bucharest
LocalDateTime localMeeting = LocalDateTime.of(2026, 3, 28, 14, 0); 
ZoneId bucharestZone = ZoneId.of("Europe/Bucharest");
ZonedDateTime zonedMeeting = localMeeting.atZone(bucharestZone);
System.out.println("Meeting in Bucharest: " + zonedMeeting);
```

##### Streams & Operations
Streams are declarative pipelines used to process data collections efficiently through:
*   **Transformations:** `map()`, `filter()`, `flatMap()`
*   **Aggregations:** Grouping, counting, summing using collectors.
*   **Consistency checks:** `allMatch()`, `anyMatch()`, `noneMatch()`

Stream operations are strictly split into two categories:
1.  **Intermediate Operations (Lazy):** They do not process elements immediately but set up the pipeline. Examples: `map`, `filter`, `flatMap`, `distinct`, `sorted`, `limit`, `skip`, `peek`, `takeWhile`, `dropWhile`.
2.  **Terminal Operations (Eager):** They trigger the execution of the entire pipeline and close the stream. Examples: `collect`, `reduce`, `forEach`, `count`, `min`, `max`, `findFirst`, `findAny`, `toList`.
*   *Optimization Tip:* Always place cheap operations (like `filter`) early in the pipeline, and expensive operations (like complex `map` functions) late, to minimize data processing overhead.

##### Collectors & Parallel Streams
*   `Collectors.toMap()` is sharp: If the stream encounters duplicate keys, it will throw an `IllegalStateException` unless you explicitly provide a **merge function** to decide whether to keep the first, keep the last, or combine the values.
*   **Parallel Streams:** Run operations using multiple CPU cores via the Common ForkJoinPool. Use them **only** when dealing with large datasets, doing CPU-bound work, and when the stream operations are completely stateless and non-interfering.

##### Practical Exercises
To practice these concepts, I started working on the homework assignments focusing on advanced stream operations.

**[View Code](../projects/exercises/Course2)** 

---

#### Setup & Environment

Following up on Day 1's blocker, the internal IT ticket was resolved this morning, and I received local administrator privileges. 
*   Successfully finalized the environment setup by installing **Rancher Desktop**.
*   Configured all system environment variables for **JDK** and **Apache Maven**.
*   Verified the installations through the terminal — all applications are now fully operational, and my development environment is 100% complete.  

---

## Day 3

### Course 3: Logging & Concurrency

#### Important aspects

##### Logging vs. System.out.println()
In enterprise Java applications, using `System.out.println()` for tracking application state is considered an anti-pattern. Professional applications rely on robust logging frameworks for several critical reasons:

*   **System.out.println():** 
    *   **No Severity Levels:** Cannot distinguish between a critical crash and a simple informational message.
    *   **No Filtering:** It is all-or-nothing; you cannot turn off minor logs in production without changing the code.
    *   **No Routing capabilities:** Writes strictly to standard output (`stdout`), making it difficult to route logs to external files or tracking systems.
    *   **Performance & Clutter:** Synchronous by nature, which can slow down applications and clutter production environment consoles.
*   **Logging Frameworks:**
    *   Provide a **structured** and standard way of formatting logs.
    *   Create a **persistent record** of application behavior over time.
    *   Essential for debugging, proactive monitoring, and security/performance analysis.

##### Core Logging Concepts (Java Util Logging / SLF4J Standard)
A logging system is built around three core components:
*   **Log Levels (Hierarchy from high to low severity):**
    *   `SEVERE` / `ERROR`: System crashes, data corruption, or critical issues requiring immediate action.
    *   `WARNING`: Unexpected behavior or non-fatal errors that allow the app to continue running but indicate a potential issue.
    *   `INFO` / `CONFIG`: General application milestones (e.g., application startup, environment configuration).
    *   `FINE` / `FINER` / `FINEST` (or `DEBUG` / `TRACE`): Highly detailed technical information used exclusively during development or deep debugging sessions.
*   **Log Configuration Anatomy:**
    *   **Handlers / Appenders:** Determine *where* the logs go (e.g., Console, File, Network Socket, Remote Log Aggregator).
    *   **Levels:** Set threshold rules to filter out less severe messages (e.g., setting the level to `INFO` hides all `DEBUG`/`FINE` logs).
    *   **Formatters / Layouts:** Control *how* the log entry looks (e.g., Plain Text, JSON format, adding timestamps, thread IDs, or class names).

##### The Power of Logging
Proper logging directly enhances:
*   **Observability:** Understanding the internal state of a running system just by looking at its outputs.
*   **Bug Identification:** Tracking down errors along with their complete stack traces.
*   **Behavioral Analysis:** Observing how users interact with the app or how resources fluctuate.
*   **Logical Tracing:** Following the precise end-to-end path of a specific request or user action across multiple classes.

##### Advanced Concurrency: Concurrency vs. Parallelism
*   **Concurrency:** The art of **handling** multiple tasks at the same time. It is about *structure*. By sharing a single processing resource (interleaved execution on a single CPU core), the system gives the illusion that tasks are running at once.
*   **Parallelism:** The art of **executing** multiple tasks simultaneously. It is about *execution*. It requires multiple hardware processing units (CPU cores) running separate jobs at the exact same physical millisecond.

##### The Concurrency Toolkit Core Entities
Managing raw `Thread` objects manually is inefficient. Modern Java uses high-level abstractions:
*   **Thread:** The physical execution worker doing the actual processing job.
*   **Runnable / Callable:** The "job description" or task definition. `Runnable` represents a task that does not return a result, while `Callable` can return a value and throw checked exceptions.
*   **ExecutorService:** The manager or controller group. Instead of creating new threads manually for every task, you submit your `Runnable` jobs to an `ExecutorService` (Thread Pool). It safely manages the worker threads, queues pending tasks, and optimizes hardware resource usage.

##### Practical Exercises
To practice thread pool optimization, asynchronous tasks, and proper log filtering levels, I completed the coding challenges assigned for this course.

**[View Code](../projects/exercises/Course3/)** 