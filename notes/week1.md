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

**[View Code](../projects/exercises/lambda_concurrency.java)** 

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