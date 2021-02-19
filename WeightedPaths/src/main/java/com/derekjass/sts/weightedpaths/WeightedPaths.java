package com.derekjass.sts.weightedpaths;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.derekjass.sts.weightedpaths.helpers.RelicTracker;
import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.derekjass.sts.weightedpaths.ui.config.Config;
import com.derekjass.sts.weightedpaths.ui.menu.WeightsMenu;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.STSSentry;
import com.megacrit.cardcrawl.map.MapRoomNode;
import io.sentry.Sentry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@SpireInitializer
public class WeightedPaths implements PostInitializeSubscriber {

    static List<MapPath> paths = new ArrayList<>();
    private static final String machineId = STSSentry.getAnonymizedMachineName();
    private static final GeneratePathsThread regenThread = new GeneratePathsThread();
    private static final RefreshValuesThread refreshThread = new RefreshValuesThread();
    public static final Map<String, Double> weights = new HashMap<>();
    public static final ConcurrentMap<MapRoomNode, Double> roomValues = new ConcurrentHashMap<>();
    public static final ConcurrentMap<MapRoomNode, Double> storeGold = new ConcurrentHashMap<>();

    private WeightedPaths() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new WeightedPaths();
    }

    public static void regeneratePaths() {
        regenThread.restart();
    }

    public static void refreshPathValues() {
        refreshThread.restart();
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

    @Override
    public void receivePostInitialize() {
        initializeSentry();
        initializeWeights();
        RelicTracker.initialize();
        WeightsMenu.initialize();
        Config.initialize();
        regenThread.start();
        refreshThread.start();
    }
}