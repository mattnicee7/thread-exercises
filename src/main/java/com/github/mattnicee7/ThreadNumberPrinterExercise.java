package com.github.mattnicee7;

import lombok.RequiredArgsConstructor;

/*
Write a program that creates two threads, where each thread prints the numbers from 1 to 50.
Ensure that both threads are running simultaneously and that each thread prints the
numbers in ascending order.

- Author: mattnicee7
*/
public class ThreadNumberPrinterExercise {

    public static void main(String[] args) {
        final Thread thread1 = new Thread(
                new PrintNumberTask(1, 50),
                "Print Number Thread 1"
        );

        final Thread thread2 = new Thread(
                new PrintNumberTask(1, 50),
                "Print Number Thread 2"
        );

        thread1.start();
        thread2.start();
    }

    @RequiredArgsConstructor
    public static class PrintNumberTask implements Runnable {

        private final int startNumber;
        private final int maxNumber;

        @Override
        public void run() {
            for (int i = startNumber; i < maxNumber; i++)
                System.out.println(i);
        }

    }

}