package com.github.mattnicee7;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Program that calculates the sum of a large number of integers in parallel.
 * Creates an array of integers with 1,000,000 elements and divides the sum
 * work among 4 threads. Each thread sums a part of the array and returns the
 * partial result. In the end, the partial results are summed to obtain the total sum.
 *
 * - Author: mattnicee7
 */
public class ParallelSumCalculatorExercise {

    private static final int ARRAY_SIZE = 1_000_000;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) {
        int[] numbers = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            numbers[i] = i + 1;
        }

        final SumTask[] threads = new SumTask[NUM_THREADS];
        int chunkSize = ARRAY_SIZE / NUM_THREADS;

        for (int i = 0; i < NUM_THREADS; i++) {
            int start = i * chunkSize;
            int end = (i == NUM_THREADS - 1) ? ARRAY_SIZE : start + chunkSize;
            threads[i] = new SumTask(numbers, start, end);
            threads[i].start();
        }

        long totalSum = 0;
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                threads[i].join();
                totalSum += threads[i].getPartialSum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Total Sum: " + totalSum);
    }

    @RequiredArgsConstructor
    private static class SumTask extends Thread {
        private final int[] numbers;
        private final int start;
        private final int end;

        @Getter
        private long partialSum;

        @Override
        public void run() {
            partialSum = 0;
            for (int i = start; i < end; i++) {
                partialSum += numbers[i];
            }
        }

    }

}

