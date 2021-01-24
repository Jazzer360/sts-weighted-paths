package com.derekjass.sts.weightedpaths.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SpirePatch(clz = MapRoomNode.class, method = "render")
public class PostMapRoomNodeRenderPatch {

    @SpirePatch(clz = FontHelper.class, method = "initialize")
    public static class OnTipBodyFontCreationPatch {

        @SpireInsertPatch(locator = Locator.class)
        public static void postFontCreation() {
            font = FontHelper.prepFont(18.0f, false);
        }

        private static class Locator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(FontHelper.class, "tipBodyFont");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    private static final Color WEIGHT_COLOR = new Color(0x00_00_00_58);

    private static BitmapFont font;

    @SpirePostfixPatch
    public static void onPostMapRoomNodeRender(MapRoomNode room, SpriteBatch sb) {
        if (WeightedPaths.roomValues.containsKey(room)) {
            drawNodeValue(room, WeightedPaths.roomValues.get(room), sb);
        }
    }

    public static void drawNodeValue(MapRoomNode room, double value, SpriteBatch sb) {
        FontHelper.renderFont(sb, font, String.format("%.1f", value),
                room.hb.cX + 20, room.hb.cY, WEIGHT_COLOR);
    }
}
