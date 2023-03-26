package com.example.translatorapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StringProcessor {
    private static final int NUM_THREADS = 10;

    public static List<String> processStrings(List<String> strings) throws InterruptedException {
        // Determine the size of each batch of strings
        int batchSize = (int) Math.ceil((double) strings.size() / NUM_THREADS);

        // Create a thread pool with NUM_THREADS threads
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        // Submit tasks to the thread pool
        List<String> result = new ArrayList<>();
        for (int i = 0; i < NUM_THREADS; i++) {
            int startIndex = i * batchSize;
            int endIndex = Math.min((i + 1) * batchSize, strings.size());

            List<String> subList = strings.subList(startIndex, endIndex);
            executorService.submit(new StringTask(subList, result));
        }

        // Shutdown the thread pool and wait for all tasks to complete
        executorService.shutdown();
        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
            // Some tasks are still running after the timeout period
            System.out.println("Some tasks are still running after the timeout period");
        }
        return result;
    }
}

