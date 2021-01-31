package com.derekjass.sts.weightedpaths.patches;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(clz = AbstractPlayer.class, method = "gainGold")
@SpirePatch(clz = AbstractPlayer.class, method = "loseGold")
public class GoldChangedPatches {

    @SpirePostfixPatch
    public static void onGainGold(AbstractPlayer instance, int amount) {
        WeightedPaths.refreshPathValues();
    }
}


