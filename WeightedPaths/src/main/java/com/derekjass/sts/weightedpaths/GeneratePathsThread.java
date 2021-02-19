package com.derekjass.sts.weightedpaths;

import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.derekjass.sts.weightedpaths.paths.UnexpectedStateException;
import io.sentry.Sentry;

import java.util.ArrayList;

class GeneratePathsThread extends Thread {

    private boolean notified = false;

    GeneratePathsThread() {
        setDaemon(true);
    }

    @Override
    public synchronized void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            while (!notified) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notified = false;
            try {
                WeightedPaths.paths = MapPath.generateAll();
            } catch (UnexpectedStateException e) {
                Sentry.captureException(e);
                WeightedPaths.paths = new ArrayList<>();
            }
            WeightedPaths.refreshPathValues();
        }
    }

    private synchronized void wake() {
        notified = true;
        notify();
    }

    void restart() {
        interrupt();
        wake();
    }
}
