package com.derekjass.sts.weightedpaths;

import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.megacrit.cardcrawl.map.MapRoomNode;

import java.util.HashMap;
import java.util.List;

public class WeightedPaths {

    public static List<MapPath> paths;

    public static HashMap<String, Float> weights = new HashMap<>();
    public static HashMap<MapRoomNode, Float> roomValues = new HashMap<>();

    static {
        weights.put("M", 1.0f);
        weights.put("?", 1.0f);
        weights.put("E", 2.0f);
        weights.put("R", 2.0f);
        weights.put("T", 0.0f);
        weights.put("$", 1.0f);
    }
}