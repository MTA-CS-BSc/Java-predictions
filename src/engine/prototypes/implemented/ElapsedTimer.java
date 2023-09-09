package engine.prototypes.implemented;

import java.util.Timer;
import java.util.TimerTask;

public class ElapsedTimer {
    private Timer timer;
    private long startTime;
    private long elapsedTime;
    private boolean isRunning;

    public ElapsedTimer() {
        timer = new Timer();
        isRunning = false;
    }
    public void startOrResume() {
        if (!isRunning) {
            isRunning = true;
            startTime = System.currentTimeMillis();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    elapsedTime = System.currentTimeMillis() - startTime;
                }
            }, 0, 1000); // Update every 1000 milliseconds (1 second)
        }
    }

    public void pause() {
        if (isRunning) {
            timer.cancel();
            elapsedTime = System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}
