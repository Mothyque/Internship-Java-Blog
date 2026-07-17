import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Course1 {

    public static void main(String[] args) {
        runLambdaAndStreamExamples();
        runConcurrencyExample();
    }

    private static void runLambdaAndStreamExamples() {
        List<String> technologies = Arrays.asList("Java", "Spring Boot", "Quarkus", "Kubernetes", "Docker", "PostgreSQL", "MySQL", "Maven", "Git");

        // Filter: Keep only technologies starting with 'J' or 'Q'
        // Map: Convert them to uppercase
        List<String> filteredList = technologies.stream()
                .filter(tech -> tech.startsWith("J") || tech.startsWith("Q"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println("Filtered & Mapped: " + filteredList);

        // Match: Check if any technology contains "SQL"
        boolean hasDatabase = technologies.stream()
                .anyMatch(tech -> tech.contains("SQL"));
        
        System.out.println("Contains SQL: " + hasDatabase);
    }

    private static void runConcurrencyExample() {
        // Concurrency demo: Create a simple Runnable task that simulates a background operation
        Runnable backgroundTask = () -> {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " completed successfully.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread thread = new Thread(backgroundTask, "Worker-Thread");
        thread.start();
    }
}