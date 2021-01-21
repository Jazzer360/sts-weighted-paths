package com.derekjass.sts.weightedpaths;

import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.map.MapRoomNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpireInitializer
public class WeightedPaths {

    private static final Logger logger = LogManager.getLogger(WeightedPaths.class.getName());

    public static List<MapPath> paths;
    public static final Map<String, Float> weights = new HashMap<>();
    public static final Map<MapRoomNode, Float> roomValues = new HashMap<>();

    @SuppressWarnings("unused")
    public static void initialize() {
        weights.put("M", 0.0f);
        weights.put("?", 0.0f);
        weights.put("E", 1.0f);
        weights.put("R", 1.0f);
        weights.put("T", 0.0f);
        weights.put("$", 0.0f);
    }

    public static void regeneratePaths() {
        paths = MapPath.generateAll();
        refreshPathValues();
        logTopPaths(5);
    }

    public static void refreshPathValues() {
        roomValues.clear();
        for (MapPath path : paths) {
            for (MapRoomNode room: path) {
                Float val = WeightedPaths.roomValues.get(room);
                if (val == null || val < path.getValue()) {
                    WeightedPaths.roomValues.put(room, path.getValue());
                }
            }
        }
    }

    public static void logTopPaths(int number) {
        for (int i = 0; i < Math.min(number, WeightedPaths.paths.size()); i++) {
            logger.info(WeightedPaths.paths.get(i));
        }
    }
}