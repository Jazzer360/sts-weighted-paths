package com.derekjass.sts.weightedpaths.patches;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class GoldChangedPatches {

    @SpirePatch(clz = AbstractPlayer.class, method = "gainGold")
    public static class PostGoldGainedPatch {

        @SpirePostfixPatch
        public static void onGainGold(AbstractPlayer instance, int amount) {
            onGoldChanged();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "loseGold")
    public static class PostGoldLostPatch {

        @SpirePostfixPatch
        public static void onLostGold(AbstractPlayer instance, int amount) {
            onGoldChanged();
        }
    }

    private static void onGoldChanged() {
        WeightedPaths.refreshPathValues();
    }
}


