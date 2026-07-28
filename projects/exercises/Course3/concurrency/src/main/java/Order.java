package main.java;

import java.util.Random;

public class Order
{
    private final int id;
    private static final Random random = new Random();

    public Order(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean process() {
        try {
            Thread.sleep(100 + random.nextInt(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        return random.nextInt(10) < 8;
    }
}
