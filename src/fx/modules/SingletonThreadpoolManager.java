package fx.modules;

import helpers.modules.ThreadPoolManager;

public abstract class SingletonThreadpoolManager {
    private static final ThreadPoolManager threadPoolManager = new ThreadPoolManager(10);

    public static void executeTask(Runnable task) {
        threadPoolManager.executeTask(task);
    }

    public static void shutdown() {
        threadPoolManager.shutdown();
    }
}
