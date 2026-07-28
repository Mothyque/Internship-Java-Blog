package main.java;

import java.util.Queue;

public class Worker implements Runnable
{
    private final int workerId;
    private final Queue<Order> orderQueue;
    private final SharedMetrics metrics;

    public Worker(int workerId, Queue<Order> orderQueue, SharedMetrics metrics) {
        this.workerId = workerId;
        this.orderQueue = orderQueue;
        this.metrics = metrics;
    }

    @Override
    public void run() {
        while (true) {
            Order order;

            synchronized (orderQueue) {
                if (orderQueue.isEmpty()) {
                    break;
                }
                order = orderQueue.poll();
            }

            if (order != null) {
                System.out.println("Worker " + workerId + " processing order " + order.getId());
                boolean success = order.process();

                if (success) {
                    metrics.incrementProcessed();
                    System.out.println("Worker " + workerId + " successfully processed order " + order.getId());
                } else {
                    metrics.incrementFailed();
                    System.out.println("Worker " + workerId + " failed to process order " + order.getId());
                }
            }
        }
    }
}

