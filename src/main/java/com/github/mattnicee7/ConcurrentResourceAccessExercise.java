package com.github.mattnicee7;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/*
Write a program that simulates a scenario of concurrent access to a shared resource.
Create a class "SharedResource" with a method "access()" that prints a message on the screen.
Create 5 threads that access the shared resource concurrently and run the program.
Observe the results and check if there are any issues with simultaneous access to the resource.

- Author: mattnicee7
 */
public class ConcurrentResourceAccessExercise {

    public static void main(String[] args) {
        final SharedResource sharedResource = new SharedResource();

        for (int j = 0; j < 100; j++) {

            for (int i = 0; i < 5; i++) {

                new Thread(
                        new Task(sharedResource),
                        "Thread - " + (j + 1) + ":" + (i + 1)
                ).start();

            }

        }

    }

    private static final class SharedResource {

        @SneakyThrows
        public void access() {
            final String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " is accessing the shared resource");

            Thread.sleep(100L);

            System.out.println(threadName + " stopped accessing the shared resource");
        }

    }

    @RequiredArgsConstructor
    private static class Task implements Runnable {

        private final SharedResource sharedResource;

        @SneakyThrows
        @Override
        public void run() {
            sharedResource.access();
        }

    }

}
