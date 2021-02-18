package com.derekjass.sts.weightedpaths;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.derekjass.sts.weightedpaths.helpers.RelicTracker;
import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.derekjass.sts.weightedpaths.paths.UnexpectedStateException;
import com.derekjass.sts.weightedpaths.ui.config.Config;
import com.derekjass.sts.weightedpaths.ui.menu.WeightsMenu;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.STSSentry;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import io.sentry.Sentry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@SpireInitializer
public class WeightedPaths implements PostInitializeSubscriber {

    private static final Logger logger = LogManager.getLogger(WeightedPaths.class.getName());

    private static List<MapPath> paths = new ArrayList<>();
    private static final String machineId = STSSentry.getAnonymizedMachineName();
    private static Thread regenThread = new Thread(WeightedPaths::performRegeneration);
    private static Thread refreshThread = new Thread(WeightedPaths::performRefresh);
    public static final Map<String, Double> weights = new HashMap<>();
    public static final ConcurrentMap<MapRoomNode, Double> roomValues = new ConcurrentHashMap<>();
    public static final ConcurrentMap<MapRoomNode, Double> storeGold = new ConcurrentHashMap<>();
    public static volatile double maxValue;
    public static volatile double minValue;

    private WeightedPaths() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new WeightedPaths();
    }

    public static void regeneratePaths() {
        regenThread.interrupt();
        try {
            regenThread.join();
            regenThread = new Thread(WeightedPaths::performRegeneration);
            regenThread.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void performRegeneration() {
        try {
            paths = MapPath.generateAll();
        } catch (UnexpectedStateException e) {
            Sentry.captureException(e);
            paths = new ArrayList<>();
        }
        refreshPathValues();
    }

    public static void refreshPathValues() {
        refreshThread.interrupt();
        try {
            refreshThread.join();
            refreshThread = new Thread(WeightedPaths::performRefresh);
            refreshThread.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void performRefresh() {
        roomValues.clear();
        storeGold.clear();
        if (paths.size() == 0) {
            logger.info("No paths to evaluate.");
            return;
        }
        logger.info("Evaluating paths.");
        if (Config.forceEmerald() && !Settings.hasEmeraldKey && AbstractDungeon.actNum == 3) {
            if (AbstractDungeon.getCurrMapNode() == null) {
                Sentry.captureMessage("In act 3 and current map node is null.");
            } else if (!AbstractDungeon.getCurrMapNode().hasEmeraldKey) {
                if (paths.stream().anyMatch(MapPath::hasEmerald)) {
                    paths = paths.stream().filter(MapPath::hasEmerald).collect(Collectors.toList());
                }
            }
        }
        long start = System.currentTimeMillis();
        for (MapPath path : paths) {
            if (Thread.interrupted()) {
                return;
            }
            path.valuate();
            for (MapRoomNode room : path) {
                roomValues.merge(room, path.getValue(), Math::max);
            }
        }
        logger.info(String.format("Path evaluation took %dms", System.currentTimeMillis() - start));
        maxValue = Collections.max(roomValues.values());
        minValue = Collections.min(roomValues.values());
        paths.sort(Collections.reverseOrder());
        logger.info("Paths evaluated and sorted.");
    }

    private static void initializeWeights() {
        weights.put("M", 1.5);
        weights.put("?", 1.5);
        weights.put("E", 3.0);
        weights.put("R", 3.0);
        weights.put("T", 0.0);
        weights.put("$", 1.0);
    }

    private static void initializeSentry() {
        Sentry.init(options -> {
            options.setEnableExternalConfiguration(true);
            options.addInAppInclude("com.derekjass.sts.weightedpaths");
            options.setBeforeSend((event, hint) -> {
                event.setServerName(machineId);
                boolean sendToSentry = true;
                if (event.getThrowable() != null) {
                    sendToSentry = false;
                    for (StackTraceElement ste : event.getThrowable().getStackTrace()) {
                        if (ste.getClassName().startsWith("com.derekjass.sts.weightedpaths")) {
                            sendToSentry = true;
                            break;
                        }
                    }
                }
                return sendToSentry ? event : null;
            });
        });
        Sentry.setExtra("loaded-mods",
                Arrays.stream(Loader.MODINFOS).map(modInfo -> modInfo.Name).collect(Collectors.toList()).toString());
        Sentry.startSession();
    }

    public static boolean isCalculating() {
        return regenThread.isAlive() || refreshThread.isAlive();
    }

    @Override
    public void receivePostInitialize() {
        initializeSentry();
        initializeWeights();
        RelicTracker.initialize();
        WeightsMenu.initialize();
        Config.initialize();
    }
}