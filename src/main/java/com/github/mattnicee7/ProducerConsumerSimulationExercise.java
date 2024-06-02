package com.github.mattnicee7;

import java.security.SecureRandom;
import java.util.*;

/*
Write a program that simulates a production and consumption process with threads. Create
a "Buffer" class that implements a circular buffer with a capacity of 10 elements.
Create two threads: a "Producer" thread that inserts random integers into the buffer
and a "Consumer" thread that removes the numbers from the buffer. Ensure that the buffer
is being used concurrently and that the producer thread stops producing when the buffer
is full and the consumer thread stops consuming when the buffer is empty.

- Author: mattnicee7
 */
public class ProducerConsumerSimulationExercise {

    private static final Buffer BUFFER = new Buffer(10);
    private static final int RANDOM_NUMBER_MAX = 100_000;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static void main(String[] args) {
        final Thread producerThread = new Thread(
                new ProducerTask(),
                "Thread Producer"
        );

        final Thread consumerThread = new Thread(
                new ConsumerTask(),
                "Thread Consumer"
        );

        producerThread.start();
        consumerThread.start();
    }

    private static class ProducerTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }

                synchronized (BUFFER) {
                    if (BUFFER.isFull()) {
                        System.out.println("Thread Producer: Buffer is full.");
                        continue;
                    }

                    int randomNumber;
                    do {
                        randomNumber = SECURE_RANDOM.nextInt(RANDOM_NUMBER_MAX);
                    } while (randomNumber == 0 || BUFFER.contains(randomNumber));

                    BUFFER.addValue(randomNumber);
                    System.out.println("Thread Producer produced and added the number: " + randomNumber);
                    BUFFER.notifyAll();
                }
            }
        }

    }

    private static class ConsumerTask implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }

                synchronized (BUFFER) {
                    if (BUFFER.isEmpty()) {
                        try {
                            BUFFER.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException(e);
                        }
                    }

                    Integer value = BUFFER.removeValue();
                    if (value != null) {
                        System.out.println("Thread Consumer removed the number: " + value);
                    }
                }
            }
        }

    }

    private static final class Buffer {
        private final List<Integer> data;
        private final int capacity;

        public Buffer(int capacity) {
            this.capacity = capacity;
            this.data = new ArrayList<>(capacity);
        }

        public void addValue(int value) {
            if (data.size() < capacity) {
                data.add(value);
            }
        }

        public Integer removeValue() {
            if (!data.isEmpty()) {
                return data.remove(0);
            }
            return null;
        }

        public boolean isFull() {
            return data.size() >= capacity;
        }

        public boolean isEmpty() {
            return data.isEmpty();
        }

        public boolean contains(int value) {
            return data.contains(value);
        }

    }
}


