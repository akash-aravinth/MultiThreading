package com.akash.multithreading;

import java.util.LinkedList;

public class SimpleProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(5); 
        
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        
        producer.start();
        consumer.start();
    }
}

class Buffer {
    private LinkedList<Integer> items;
    private int capacity;

    public Buffer(int capacity) {
        this.capacity = capacity;
        items = new LinkedList<>();
    }

    public synchronized void produce(int item) throws InterruptedException {
        while (items.size() >= capacity) {
            wait();
        }
        items.add(item);
        System.out.println("Produced: " + item);
        notifyAll(); 
    }

    public synchronized int consume() throws InterruptedException {
        while (items.size() == 0) {
            wait();
        }
        int item = items.removeFirst();
        System.out.println("Consumed: " + item);
        notifyAll(); 
        return item;
    }
}

class Producer extends Thread {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                buffer.produce(i);
                Thread.sleep(1000); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer extends Thread {
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                int item = buffer.consume();
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
