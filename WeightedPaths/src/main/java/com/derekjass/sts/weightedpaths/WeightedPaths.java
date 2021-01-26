package com.derekjass.sts.weightedpaths;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.derekjass.sts.weightedpaths.helpers.RelicTracker;
import com.derekjass.sts.weightedpaths.menu.WeightsMenu;
import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.map.MapRoomNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpireInitializer
public class WeightedPaths implements PostInitializeSubscriber {

    private static final Logger logger = LogManager.getLogger(WeightedPaths.class.getName());

    private static List<MapPath> paths;
    public static final Map<String, Double> weights = new HashMap<>();
    public static final Map<MapRoomNode, Double> roomValues = new HashMap<>();

    private WeightedPaths() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new WeightedPaths();
    }

    public static void regeneratePaths() {
        paths = MapPath.generateAll();
        refreshPathValues();
        logTopPaths();
    }

    public static void refreshPathValues() {
        if (paths == null || paths.size() == 0) {
            logger.info("No paths to evaluate.");
            return;
        }
        logger.info("Evaluating paths.");
        roomValues.clear();
        for (MapPath path : paths) {
            path.valuate();
            for (MapRoomNode room: path) {
                Double val = WeightedPaths.roomValues.get(room);
                if (val == null || val < path.getValue()) {
                    WeightedPaths.roomValues.put(room, path.getValue());
                }
            }
        }
        paths.sort(Collections.reverseOrder());
        logger.info("Paths evaluated and sorted.");
    }

    private static void logTopPaths() {
        for (int i = 0; i < Math.min(5, WeightedPaths.paths.size()); i++) {
            logger.info(WeightedPaths.paths.get(i));
        }
    }

    @Override
    public void receivePostInitialize() {
        weights.put("M", 1.5);
        weights.put("?", 1.5);
        weights.put("E", 3.0);
        weights.put("R", 3.0);
        weights.put("T", 0.0);
        weights.put("$", 1.0);
        RelicTracker.initialize();
        WeightsMenu.initialize();
    }
}