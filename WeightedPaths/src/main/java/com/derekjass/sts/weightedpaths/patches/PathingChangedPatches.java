package com.derekjass.sts.weightedpaths.patches;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;

public class PathingChangedPatches {

    @SpirePatch(clz = AbstractDungeon.class, method = "generateMap")
    public static class PostGenerateMapPatch {

        @SpirePostfixPatch
        public static void onMapGenerated() {
            onPathingChanged();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "setCurrMapNode")
    public static class PostNewFloorPatch {

        @SpirePostfixPatch
        public static void onNewFloor(MapRoomNode room) {
            onPathingChanged();
        }
    }

    private static void onPathingChanged() {
        WeightedPaths.regeneratePaths();
    }
}
