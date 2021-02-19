package com.derekjass.sts.weightedpaths;

import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.derekjass.sts.weightedpaths.ui.config.Config;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import io.sentry.Sentry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

class RefreshValuesThread extends Thread {

    private static final Logger logger = LogManager.getLogger(RefreshValuesThread.class.getName());

    private boolean notified = false;

    RefreshValuesThread() {
        setDaemon(true);
    }

    @Override
    public synchronized void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            while (!notified) {
                try {
                    wait();
                } catch (InterruptedException ignored) {}
            }
            notified = false;
            refreshValues();
        }
    }

    private void refreshValues() {
        try {
            WeightedPaths.roomValues.clear();
            WeightedPaths.storeGold.clear();
            WeightedPaths.pathsLock.lock();
            if (WeightedPaths.paths.size() == 0) {
                logger.info("No paths to evaluate.");
                return;
            }
            logger.info("Evaluating paths.");
            if (Config.forceEmerald() && !Settings.hasEmeraldKey && AbstractDungeon.actNum == 3) {
                if (AbstractDungeon.getCurrMapNode() == null) {
                    Sentry.captureMessage("In act 3 and current map node is null.");
                } else if (!AbstractDungeon.getCurrMapNode().hasEmeraldKey) {
                    if (WeightedPaths.paths.stream().anyMatch(MapPath::hasEmerald)) {
                        WeightedPaths.paths = WeightedPaths.paths.stream()
                                .filter(MapPath::hasEmerald).collect(Collectors.toList());
                    }
                }
            }
            long start = System.currentTimeMillis();
            for (MapPath path : WeightedPaths.paths) {
                if (Thread.interrupted()) {
                    logger.info("Refresh values thread interrupted.");
                    return;
                }
                path.valuate();
                for (MapRoomNode room : path) {
                    WeightedPaths.roomValues.merge(room, path.getValue(), Math::max);
                }
            }
            logger.info(String.format("Evaluated %d paths in %dms", WeightedPaths.paths.size(),
                    System.currentTimeMillis() - start));
        } finally {
            WeightedPaths.pathsLock.unlock();
        }
    }

    private synchronized void wake() {
        notified = true;
        notify();
    }

    void restart() {
        if (this.getState() != State.WAITING) {
            interrupt();
        }
        wake();
    }
}
