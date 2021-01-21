package com.derekjass.sts.weightedpaths.patches;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz = AbstractDungeon.class, method = "generateMap")
public class PostGenerateMapPatch {

    @SpirePostfixPatch
    public static void onMapGenerated() {
        WeightedPaths.regeneratePaths();
    }
}
