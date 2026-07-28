package main.java;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//        runParalleldemo();
        runDeadlockDemo(true);
    }

    public static void runParalleldemo() {
        System.out.println("=== STARTING PARALLEL PROCESSING DEMO ===");
        SharedMetrics sharedMetrics = new SharedMetrics();
        Queue<Order> orderQueue = new LinkedList<>();

        for (int i = 1; i <= 10; i++) {
            orderQueue.add(new Order(i));
        }

        int numberOfWorkers = 3;
        Thread[] workers = new Thread[numberOfWorkers];

        for(int i = 0; i < numberOfWorkers; i++) {
            workers[i] = new Thread(new Worker(i + 1, orderQueue, sharedMetrics));
        }
        long startTime = System.currentTimeMillis();

        for (Thread worker : workers) {
            worker.start();
        }

        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("=== PARALLEL PROCESSING DEMO COMPLETED ===");
        System.out.println("Total time taken: " + (endTime - startTime) + " ms");
    }

    public static void runDeadlockDemo(boolean solved) {
        System.out.println("=== STARTING DEADLOCK DEMO (Solved: " + solved + ") ===");

        Object lock1 = new Object();
        Object lock2 = new Object();

        Thread worker1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Worker 1 acquired lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println("Worker 1 acquired lock2");
                }
            }
        });

        Thread worker2 = new Thread(() -> {
            Object firstLock = solved ? lock1 : lock2;
            Object secondLock = solved ? lock2 : lock1;

            String firstLockName = solved ? "lock1" : "lock2";
            String secondLockName = solved ? "lock2" : "lock1";

            synchronized (firstLock) {
                System.out.println("Worker 2 acquired " + firstLockName);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (secondLock) {
                    System.out.println("Worker 2 acquired " + secondLockName);
                }
            }
        });

        worker1.start();
        worker2.start();

        try {
            worker1.join(3000);
            worker2.join(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (worker1.isAlive() || worker2.isAlive()) {
            System.err.println("Deadlock detected! One or both workers are still alive after timeout.");
        } else {
            System.out.println("No deadlock detected. Both workers completed successfully.");
        }
        System.out.println("=== DEADLOCK DEMO COMPLETED ===");
    }
}