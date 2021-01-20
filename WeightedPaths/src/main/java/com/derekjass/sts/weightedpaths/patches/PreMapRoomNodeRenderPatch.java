package com.derekjass.sts.weightedpaths.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;

@SpirePatch(clz = MapRoomNode.class, method="render")
public class PreMapRoomNodeRenderPatch {

    private static final Color WEIGHT_COLOR = new Color(0x00_00_00_58);

    @SpirePrefixPatch
    public static void onPreMapRoomNodeRender(MapRoomNode room, SpriteBatch sb) {
        if (WeightedPaths.roomValues.containsKey(room)) {
            drawNodeValue(room, WeightedPaths.roomValues.get(room), sb);
        }
    }

    public static void drawNodeValue(MapRoomNode room, float value, SpriteBatch sb) {
        FontHelper.renderFont(sb, FontHelper.tipBodyFont, String.format("%.0f", value),
                room.hb.cX + 20, room.hb.cY, WEIGHT_COLOR);
    }
}
