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

---

## Day 2

### Course 5: Git & Version Control Systems

#### Important aspects

##### Benefits of Version Control
Version control systems (VCS) like Git provide critical infrastructure for modern software engineering:
*   **Single Source of Truth:** Full history of every code change from day one.
*   **Safe Experimentation:** Work on isolated branches without risking the stability of the main codebase.
*   **Parallel Collaboration:** Multiple developers can work on the same codebase simultaneously without stepping on each other's toes.
*   **Auditability:** Every single change can be peer-reviewed, reverted, or traced back using blame tools.

##### Core Concepts
*   **Repository:** The project directory tracked by Git (containing the hidden `.git` folder).
*   **Commit:** A saved, immutable snapshot of changes accompanied by an explanatory message.
*   **Branch:** An independent, parallel line of development pointing to a specific commit.
*   **Remote:** A version of the repository hosted on a cloud provider (e.g., GitHub, GitLab) used for synchronization.

##### Basic Git Workflow
The standard local-to-remote development flow follows four key phases:
`Working Directory (Edited Files) -> Staging Area (git add) -> Local Repository (git commit) -> Remote Repository (git push)`

##### 4. Advanced Git Operations

###### Git Rebase vs. Git Merge
*   **Git Merge:** Combines branches by creating a special "merge commit". Preserves complete chronological history, but can create a messy graph with many branching paths.
*   **Git Rebase:** Re-applies your feature branch commits on top of the latest target branch. Creates a clean, linear history.
*   *Golden Rule:* Never rebase a branch that is shared with other developers, as it rewrites public commit history!

###### Force Push Safety
*   `git push --force`: Overwrites remote history unconditionally. Dangerous—can destroy teammates' work.
*   `git push --force-with-lease`: Safer alternative. Refuses to overwrite remote history if someone else pushed new commits in the meantime.

###### Resolving Merge Conflicts
A conflict occurs when two branches modify the exact same line in a file, forcing manual intervention:
1. Identify conflict markers: `<<<<<<< HEAD` (your changes), `=======` (separator), `>>>>>>> branch-name` (incoming changes).
2. Manually edit the file to select or combine the correct code.
3. Remove all conflict markers.
4. Stage the file (`git add .`) and finalize with a commit (`git commit`).

###### Cherry-Pick
Used to select and apply a single specific commit from another branch into your current branch (e.g., pulling a critical bug fix into a release branch without merging the entire feature).

##### 5. Pull Request (PR) Workflow
The standard enterprise flow for merging code into shared branches:
`Push Feature Branch -> Open PR -> Run Automated CI Checks -> Peer Code Review -> Merge into Target Branch`

##### 6. Git Best Practices

###### Writing Great Commit Messages
*   Use imperative mood in the summary line (*"Add feature"* instead of *"Added feature"*).
*   Keep the summary line concise (~50 characters).
*   Explain *why* the change was made, not just *what* was changed.
*   Reference issue/ticket numbers when applicable (e.g., `[JIRA-102] Fix user authentication bug`).

###### Do's and Don'ts

| Practice | Do ✅ | Don't ❌ |
| :--- | :--- | :--- |
| **Commit Size** | Commit small, logical, and focused chunks of work | Make massive, multi-day commits mixing unrelated features |
| **Security** | Use `.gitignore` for secrets, build artifacts, and `node_modules` | Commit API keys, passwords, credentials, or `.env` files |
| **Syncing** | Run `git pull` frequently before starting new tasks | Work for days locally without pushing code to the remote |
| **Branching** | Keep main branches protected and perform code reviews | Force-push directly to `main` or shared team branches |

##### Practical Exercises
To master version control workflows, I practiced branch management, resolving merge conflicts, rebasing, and submitting structured PRs.
*   📂 **[View Homework: Course5 Folder](../proiecte/Course5/)** *(contains version control practice scenarios and scripting)*