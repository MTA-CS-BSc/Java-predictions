package helpers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    protected ExecutorService executor;
    protected int threadsAmount;

    public ThreadPoolManager() {
        threadsAmount = Constants.NOT_SET;
    }
    public ThreadPoolManager(int threadsAmount) {
        setThreadsAmount(threadsAmount);
    }

    public void setThreadsAmount(int value) {
        threadsAmount = value;
        executor = Executors.newFixedThreadPool(threadsAmount);
    }

    public void executeTask(Runnable task) {
        if (threadsAmount > 0)
            executor.execute(task);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public int getThreadsAmount() {
        return threadsAmount;
    }
}
