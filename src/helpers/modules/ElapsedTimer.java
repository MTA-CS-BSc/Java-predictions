package helpers.modules;

import javax.swing.*;

public class ElapsedTimer {
    private final Timer timer;
    private boolean isRunning;
    private long elapsedTime;

    public ElapsedTimer() {
        isRunning = false;
        elapsedTime = 0;

        timer = new Timer(1, e -> {
            elapsedTime++;
        });
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void start() {
        isRunning = true;
        timer.start();
    }

    public void pause() {
        isRunning = false;
        timer.stop();
    }

    public void resume() {
        start();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
