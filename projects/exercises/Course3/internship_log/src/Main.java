import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {
        LoggingBootstrap.configureMain();
        run();
    }

    static void run() {
        Logger logger = logger();
        logger.trace("Entering application bootstrap");
        logger.debug("Loading application settings");
        logger.info("Application started");
        logger.info("Database URL = jdbc:postgresql://localhost:5432/mydb");
        logger.info("Max connection pool size = 10");
        logger.warn("Demo application is using local sample data");

        processOrder("ORD-1234", 3);

        System.out.println("Application shutting down");
    }

    private static void processOrder(String orderId, int quantity) {
        Logger logger = logger();
        logger.trace("Starting processOrder for {}", orderId);
        logger.info("Processing order: {}", orderId);
        logger.debug("Entering processOrder(orderId={}, quantity={})", orderId, quantity);

        // Simulate a validation step
        logger.info("Starting input validation for order {}", orderId);
        if (quantity <= 0) {
            logger.error("Invalid quantity ({}) for order {}", quantity, orderId);
            return;
        }
        logger.info("Validation passed");

        // Simulate price calculation
        double pricePerUnit = 29.99;
        double total = pricePerUnit * quantity;
        logger.debug("Price per unit: {}, computed total: {}", pricePerUnit, total);

        // Simulate a database save
        logger.info("Saving order {} to database", orderId);
        try {
            saveToDatabase(orderId, total);
        } catch (Exception e) {
            logger.error("Failed to save order {}", orderId, e);
            return;
        }

        logger.info("Order {} processed successfully — total: ${}", orderId, total);
        logger.trace("Exiting processOrder for {}", orderId);
    }

    private static void saveToDatabase(String orderId, double total) {
        Logger logger = logger();
        // Simulate a successful save
        logger.debug("Preparing database insert for {}", orderId);
        logger.info("Executing INSERT for order {} with total {}", orderId, total);
        logger.info("SQL: INSERT INTO orders (id, total) VALUES ('{}', {})", orderId, total);
    }

    private static Logger logger() {
        return LoggerFactory.getLogger(Main.class);
    }
}