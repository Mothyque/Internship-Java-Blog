# Week 2

## Day 1

### Course 4: Testing & Quality Assurance

#### Important aspects

##### Types of Testing Overview
Testing strategies are divided based on their scope and purpose:

*   **By Scope:**
    *   **Unit Testing:** Verifies individual functions, methods, or classes in complete isolation.
    *   **Integration Testing:** Validates interactions and data flow between multiple components or external systems (e.g., Database, Messaging Queues).
    *   **System Testing:** Tests the complete, integrated application as a whole.
    *   **End-to-End (E2E) Testing:** Validates the entire application flow from the end-user's perspective, mimicking real-world usage.
*   **By Purpose:**
    *   **Functional:** Verifies what the system does and if it works correctly according to business rules.
    *   **Non-Functional:** Evaluates how the system performs (performance, security, scalability, usability).
    *   **Regression:** Ensures that new code changes or bug fixes haven't broken existing functionality.
    *   **Acceptance:** Verifies whether the software meets all business requirements before release.

##### The Testing Pyramid
The testing pyramid illustrates the optimal distribution of tests in a healthy test suite:

*   **E2E Tests (~10%):** Slow, brittle, expensive to run and maintain. Focus strictly on critical business paths.
*   **Integration Tests (~20%):** Medium speed and cost. Focus on component wiring and database/service interactions.
*   **Unit Tests (~70%):** Fast, stable, cheap. Form the base of the pyramid — test edge cases and complex business logic here.

##### Test-Driven Development (TDD)
Writing tests before production code ensures high test coverage, living documentation, and confidence when refactoring. Ideal for complex business logic and bug fixes.

*   **The Red-Green-Refactor Cycle:**
    1.  **RED:** Write a unit test that fails because the feature doesn't exist yet.
    2.  **GREEN:** Write the minimum amount of production code needed to make the test pass.
    3.  **REFACTOR:** Clean up the code (remove duplication, improve naming) while keeping the test passing.

##### Core Testing Principles

###### F.I.R.S.T Principle
*   **Fast:** Tests should run in milliseconds to keep the feedback loop tight.
*   **Isolated / Independent:** Tests must not depend on each other or run in a specific order.
*   **Repeatable:** Produces the exact same result every time, regardless of the execution environment.
*   **Self-Validating:** Clear PASS or FAIL output without requiring manual log inspection.
*   **Timely:** Written close to or ahead of the production code.

###### AAA Pattern (Arrange - Act - Assert)
Structure every test method into three distinct phases: Arrange test data, Act on the behavior, and Assert the outcome.

``` java
@Test
public void shouldCalculateDiscountedPrice() {
    // ARRANGE: Set up test data and dependencies
    Product product = new Product("Laptop", 1000.0);
    DiscountService service = new DiscountService();
    
    // ACT: Execute the target behavior
    double finalPrice = service.applyDiscount(product, 0.2);
    
    // ASSERT: Verify the expected outcome
    assertEquals(800.0, finalPrice);
}
```

##### JUnit 5 Framework

###### Key Annotations
*   `@Test`: Marks a method as a test method.
*   `@BeforeEach`: Executes before each individual test method.
*   `@AfterEach`: Executes after each individual test method.
*   `@BeforeAll`: Executes once before all tests in the class (must be static).
*   `@AfterAll`: Executes once after all tests in the class (must be static).
*   `@Disabled`: Skips the execution of a test method or class.

###### Assertion Methods
```java
// Equality checks
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);

// Boolean conditions
assertTrue(condition);
assertFalse(condition);

// Nullability
assertNull(object);
assertNotNull(object);

// Exceptions
assertThrows(IllegalArgumentException.class, () -> service.process(-1));

// Multiple assertions (grouped execution)
assertAll(
    () -> assertEquals("Laptop", product.getName()),
    () -> assertEquals(800.0, product.getPrice())
);

// Timeouts
assertTimeout(Duration.ofMillis(100), () -> service.fastOperation());
```

###### Parameterized Tests
Allows running the same test logic multiple times with different input arguments using sources like `@ValueSource`, `@CsvSource`, or `@MethodSource`.


```java
// Equality checks
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);

// Boolean conditions
assertTrue(condition);
assertFalse(condition);

// Nullability
assertNull(object);
assertNotNull(object);

// Exceptions
assertThrows(IllegalArgumentException.class, () -> service.process(-1));

// Multiple assertions (grouped execution)
assertAll(
    () -> assertEquals("Laptop", product.getName()),
    () -> assertEquals(800.0, product.getPrice())
);

// Timeouts
assertTimeout(Duration.ofMillis(100), () -> service.fastOperation());
```

##### Mocking with Mockito
Mockito allows isolating the class under test by replacing real dependencies with controllable mock objects.

``` java
@Test
public void shouldSaveUserSuccessfully() {
    // Create mocks
    UserRepository mockRepository = Mockito.mock(UserRepository.class);
    EmailService mockEmailService = Mockito.mock(EmailService.class);
    UserService userService = new UserService(mockRepository, mockEmailService);

    User user = new User("johndoe@example.com");

    // Stubbing behavior
    when(mockRepository.save(user)).thenReturn(user);

    // Execute method under test
    User saved = userService.registerUser(user);

    // Assertions and Verifications
    assertNotNull(saved);
    verify(mockRepository).save(user);
    verify(mockEmailService).sendWelcomeEmail(user);
}
```

###### Mocking Rules of Thumb
*   ✅ **DO:** Mock external dependencies (DB, APIs), use `@Mock` and `@InjectMocks` annotations for cleaner code, and use `verify()` to assert side-effects.
*   ❌ **DON'T:** Mock value objects, simple DTOs/POJOs, or the class currently under test. Avoid over-mocking or verifying every single method call.

##### API Testing with REST Assured
REST Assured is a Java library used for testing and validating REST APIs via a fluent **Given-When-Then** BDD syntax.

```java
@Test
public void shouldReturnUserById() {
    given()
        .header("Content-Type", "application/json")
        .pathParam("id", 1)
    .when()
        .get("/api/users/{id}")
    .then()
        .statusCode(200)
        .body("name", equalTo("John Doe"))
        .body("email", equalTo("johndoe@example.com"));
}
```

##### E2E Testing & Integration Scenarios
E2E testing validates entire workflows in a production-like environment:
*   **Order Processing Flow:** Create order -> Process payment -> Update inventory -> Send email notification.
*   **Multi-Service Data Flow:** REST API Call -> Kafka Event -> Database Persistence -> Query Result.
*   **Resilience & Failure Handling:** Service failure -> Retry logic -> Dead Letter Queue (DLQ).

##### Testing Best Practices Summary

| Practice | Do ✅ | Don't ❌ |
| :--- | :--- | :--- |
| **Pyramid** | Stick to the Testing Pyramid (mostly Unit Tests) | Write inverted pyramids (too many E2E, zero Unit) |
| **Isolation** | Ensure complete isolation; use Testcontainers for integration | Depend on global state or external shared databases |
| **Design** | Refactor code if it is hard to test | Ignore failing or flaky tests |
| **Naming** | Use descriptive test names showing intent | Test getters, setters, or third-party framework code |
| **CI/CD** | Run tests automatically on every `git commit` | Couple test assertions tightly to private implementation details |
