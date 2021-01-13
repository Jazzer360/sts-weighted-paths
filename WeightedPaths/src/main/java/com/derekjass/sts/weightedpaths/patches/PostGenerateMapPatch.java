package com.derekjass.sts.weightedpaths.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz=AbstractDungeon.class, method="generateMap")
public class PostGenerateMapPatch {

    @SpirePostfixPatch
    public static void onMapGenerated() {
        System.out.println("----------Map generated.----------");
    }
}
