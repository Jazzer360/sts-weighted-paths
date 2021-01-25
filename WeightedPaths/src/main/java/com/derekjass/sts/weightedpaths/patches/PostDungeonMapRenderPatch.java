package com.derekjass.sts.weightedpaths.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.menu.WeightsMenu;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.map.DungeonMap;

@SpirePatch(clz = DungeonMap.class, method = "render")
public class PostDungeonMapRenderPatch {

    @SpirePostfixPatch
    public static void onRender(DungeonMap instance, SpriteBatch sb) {
        WeightsMenu.menu.render(sb);
    }
}
