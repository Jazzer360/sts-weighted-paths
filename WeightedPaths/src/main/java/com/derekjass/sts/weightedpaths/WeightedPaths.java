package com.derekjass.sts.weightedpaths;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.derekjass.sts.weightedpaths.helpers.RelicTracker;
import com.derekjass.sts.weightedpaths.ui.config.Config;
import com.derekjass.sts.weightedpaths.ui.menu.WeightsMenu;
import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.derekjass.sts.weightedpaths.paths.UnexpectedStateException;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import io.sentry.Sentry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpireInitializer
public class WeightedPaths implements PostInitializeSubscriber {

    private static final Logger logger = LogManager.getLogger(WeightedPaths.class.getName());

    private static List<MapPath> paths = new ArrayList<>();
    public static final Map<String, Double> weights = new HashMap<>();
    public static final Map<MapRoomNode, Double> roomValues = new HashMap<>();
    public static final Map<MapRoomNode, Double> storeGold = new HashMap<>();
    public static double maxValue;
    public static double minValue;

    private WeightedPaths() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new WeightedPaths();
    }

    public static void regeneratePaths() {
        try {
            paths = MapPath.generateAll();
        } catch (UnexpectedStateException e) {
            Sentry.captureException(e);
            paths = new ArrayList<>();
        }
        refreshPathValues();
        logTopPaths();
    }

    public static void refreshPathValues() {
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
                List<MapPath> filterPaths = paths.stream().filter(MapPath::hasEmerald).collect(Collectors.toList());
                paths = filterPaths.isEmpty() ? paths : filterPaths;
            }
        }
        for (MapPath path : paths) {
            path.valuate();
            for (MapRoomNode room: path) {
                Double val = WeightedPaths.roomValues.get(room);
                if (val == null || val < path.getValue()) {
                    WeightedPaths.roomValues.put(room, path.getValue());
                }
            }
        }
        maxValue = Collections.max(roomValues.values());
        minValue = Collections.min(roomValues.values());
        paths.sort(Collections.reverseOrder());
        logger.info("Paths evaluated and sorted.");
    }

    private static void logTopPaths() {
        for (int i = 0; i < Math.min(5, WeightedPaths.paths.size()); i++) {
            logger.info(WeightedPaths.paths.get(i));
        }
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
                event.setServerName(null);
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