package main.java;

public class SharedMetrics
{
    public int totalOrdersProcessed = 0;
    public int totalOrdersFailed = 0;

    public synchronized void incrementProcessed() {
        totalOrdersProcessed++;
    }

    public synchronized void incrementFailed() {
        totalOrdersFailed++;
    }

    public synchronized int getTotalOrdersProcessed() {
        return totalOrdersProcessed;
    }

    public synchronized int getTotalOrdersFailed() {
        return totalOrdersFailed;
    }

    public synchronized void printSummary() {
        System.out.println("--- Final Metrics ---");
        System.out.println("Processed successfully: " + totalOrdersProcessed);
        System.out.println("Failed: " + totalOrdersFailed);
        System.out.println("Total handled: " + (totalOrdersProcessed + totalOrdersFailed));
    }
}
