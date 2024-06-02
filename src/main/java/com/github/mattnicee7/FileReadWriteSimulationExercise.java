package com.github.mattnicee7;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.security.SecureRandom;

/**
 * A program that simulates a process of reading and writing to a shared file with threads.
 * It creates a file "data.txt" with some lines of text.
 * It also creates two threads: a "Reader" thread that reads the content of the file and
 * prints it on the screen and a "Writer" thread that adds new lines of text to the file.
 * The writer thread stops writing when the file reaches a maximum size.
 *
 * - Author: mattnicee7
 */
public class FileReadWriteSimulationExercise {

    private static final String TXT_FILE_PATH = "D:\\data.txt";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int FILE_MAX_SIZE = 30;

    @SneakyThrows
    public static void main(String[] args) {
        final File file = new File(TXT_FILE_PATH);

        final Thread readerThread = new Thread(new ReaderTask(file), "Reader Thread");
        final Thread writerThread = new Thread(new WriterTask(file), "Writer Thread");

        readerThread.start();
        writerThread.start();
    }

    @RequiredArgsConstructor
    private static class ReaderTask implements Runnable {
        private final File file;

        @Override
        public void run() {
            while (true) {
                String fullText = readFileContent(file);
                System.out.println("Reader Thread (Content): " + fullText);

                if (file.length() >= FILE_MAX_SIZE) {
                    break;
                }

                try {
                    Thread.sleep(500); // Pause to allow writer to write
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @RequiredArgsConstructor
    private static class WriterTask implements Runnable {
        private final File file;

        @Override
        public void run() {
            while (true) {
                if (file.length() >= FILE_MAX_SIZE) {
                    System.out.println("Writer Thread: The file reached the maximum size of " + FILE_MAX_SIZE);
                    break;
                }

                final String randomString = generateRandomString(3);

                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
                    bufferedWriter.write(randomString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(200); // Pause to allow reader to read
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @SneakyThrows
    private static String readFileContent(File file) {
        final StringBuilder fullText = new StringBuilder();

        @Cleanup BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String actualLine;

        while ((actualLine = bufferedReader.readLine()) != null) {
            fullText.append(actualLine).append(System.lineSeparator());
        }

        return fullText.toString();
    }

    private static String generateRandomString(int length) {
        final StringBuilder randomString = new StringBuilder();
        char[] characterArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345678abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (int i = 0; i < length; i++) {
            randomString.append(characterArray[SECURE_RANDOM.nextInt(characterArray.length)]);
        }

        return randomString.toString();
    }

}


