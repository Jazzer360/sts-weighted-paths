package com.derekjass.sts.weightedpaths.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;

@SpirePatch(clz = MapRoomNode.class, method="render")
public class PostMapRoomNodeRenderPatch {

    private static final Color WEIGHT_COLOR = new Color(0x00_00_00_58);

    @SpirePostfixPatch
    public static void onPostMapRoomNodeRender(MapRoomNode room, SpriteBatch sb) {
        if (WeightedPaths.roomValues.containsKey(room)) {
            drawNodeValue(room, WeightedPaths.roomValues.get(room), sb);
        }
    }

    public static void drawNodeValue(MapRoomNode room, double value, SpriteBatch sb) {
        FontHelper.renderFont(sb, FontHelper.tipBodyFont, String.format("%.0f", value),
                room.hb.cX + 20, room.hb.cY, WEIGHT_COLOR);
    }
}
