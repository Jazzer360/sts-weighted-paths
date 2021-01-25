package com.derekjass.sts.weightedpaths.patches;

import com.derekjass.sts.weightedpaths.menu.WeightsMenu;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.map.DungeonMap;

@SpirePatch(clz = DungeonMap.class, method = "update")
public class PostDungeonMapUpdatePatch {

    @SpirePostfixPatch
    public static void onUpdate(DungeonMap instance) {
        WeightsMenu.menu.update();
    }
}
