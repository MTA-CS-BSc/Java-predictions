package helpers.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    protected ExecutorService executor;
    protected int threadsAmount;
    protected List<String> recordedSimulationIds;

    public ThreadPoolManager() {
        this(1);
    }

    public ThreadPoolManager(int threadsAmount) {
        setThreadsAmount(threadsAmount);
        recordedSimulationIds = new ArrayList<>();
    }

    public void executeTask(Runnable task) {
        if (threadsAmount > 0)
            executor.execute(task);
    }

    public void addRunSimulationToQueue(Runnable task, String uuid) {
        if (threadsAmount > 0) {
            executor.execute(task);
            recordedSimulationIds.add(uuid);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    public int getThreadsAmount() {
        return threadsAmount;
    }

    public void setThreadsAmount(int value) {
        threadsAmount = value;
        executor = Executors.newFixedThreadPool(threadsAmount);
    }

    public boolean isSimulationRecorded(String uuid) {
        return recordedSimulationIds.contains(uuid);
    }
}
