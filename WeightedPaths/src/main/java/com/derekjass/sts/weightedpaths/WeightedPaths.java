package com.derekjass.sts.weightedpaths;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.derekjass.sts.weightedpaths.helpers.RelicTracker;
import com.derekjass.sts.weightedpaths.menu.WeightsMenu;
import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.derekjass.sts.weightedpaths.paths.UnexpectedStateException;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import io.sentry.Sentry;
import io.sentry.SentryClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpireInitializer
public class WeightedPaths implements PostInitializeSubscriber {

    private static final Logger logger = LogManager.getLogger(WeightedPaths.class.getName());

    private static List<MapPath> paths;
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
            Sentry.capture(e);
        } finally {
            paths = null;
        }
        refreshPathValues();
        logTopPaths();
    }

    public static void refreshPathValues() {
        roomValues.clear();
        storeGold.clear();
        if (paths == null || paths.size() == 0) {
            logger.info("No paths to evaluate.");
            return;
        }
        logger.info("Evaluating paths.");
        if (Config.forceEmerald() && !Settings.hasEmeraldKey && AbstractDungeon.actNum == 3 &&
                AbstractDungeon.getCurrMapNode() != null && !AbstractDungeon.getCurrMapNode().hasEmeraldKey) {
            List<MapPath> filterPaths = paths.stream().filter(MapPath::hasEmerald).collect(Collectors.toList());
            paths = filterPaths.isEmpty() ? paths : filterPaths;
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

    public void initializeWeights() {
        weights.put("M", 1.5);
        weights.put("?", 1.5);
        weights.put("E", 3.0);
        weights.put("R", 3.0);
        weights.put("T", 0.0);
        weights.put("$", 1.0);
    }

    private static void initializeSentry() {
        Sentry.init((SentryClientFactory) options -> {
            // NOTE: Replace the test DSN below with YOUR OWN DSN to see the events from this app in
            // your Sentry project/dashboard
            options.setDsn("https://f7f320d5c3a54709be7b28e0f2ca7081@sentry.io/1808954");

            // All events get assigned to the release. See more at
            // https://docs.sentry.io/workflow/releases/
            options.setRelease("io.sentry.samples.console@4.0.0+1");

            // Modifications to event before it goes out. Could replace the event altogether
            options.setBeforeSend(
                    (event, hint) -> {
                        // Drop an event altogether:
                        if (event.getTag("SomeTag") != null) {
                            return null;
                        }
                        return event;
                    });

            // Allows inspecting and modifying, returning a new or simply rejecting (returning null)
            options.setBeforeBreadcrumb(
                    (breadcrumb, hint) -> {
                        // Don't add breadcrumbs with message containing:
                        if (breadcrumb.getMessage() != null
                                && breadcrumb.getMessage().contains("bad breadcrumb")) {
                            return null;
                        }
                        return breadcrumb;
                    });

            // Configure the background worker which sends events to sentry:
            // Wait up to 5 seconds before shutdown while there are events to send.
            options.setShutdownTimeout(5000);

            // Enable SDK logging with Debug level
            options.setDebug(true);
            // To change the verbosity, use:
            // By default it's DEBUG.
            options.setDiagnosticLevel(
                    SentryLevel
                            .ERROR); //  A good option to have SDK debug log in prod is to use only level
            // ERROR here.

            // Exclude frames from some packages from being "inApp" so are hidden by default in Sentry
            // UI:
            options.addInAppExclude("org.jboss");
        });
    }

    @Override
    public void receivePostInitialize() {
        initializeSentry();
        try {
            testSentry();
        } catch (Exception e) {
            Sentry.capture(e);
        }
        initializeWeights();
        RelicTracker.initialize();
        WeightsMenu.initialize();
        Config.initialize();
    }

    private static void testSentry() throws Exception {
        throw new Exception();
    }
}