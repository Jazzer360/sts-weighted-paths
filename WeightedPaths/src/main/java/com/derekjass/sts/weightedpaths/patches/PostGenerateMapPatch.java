package com.derekjass.sts.weightedpaths.patches;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.derekjass.sts.weightedpaths.paths.MapPath;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch(clz = AbstractDungeon.class, method = "generateMap")
public class PostGenerateMapPatch {

    private static final Logger logger = LogManager.getLogger(PostGenerateMapPatch.class.getName());

    @SpirePostfixPatch
    public static void onMapGenerated() {
        WeightedPaths.paths = MapPath.generateAll();
        WeightedPaths.logTopPaths(5, logger);
    }
}
