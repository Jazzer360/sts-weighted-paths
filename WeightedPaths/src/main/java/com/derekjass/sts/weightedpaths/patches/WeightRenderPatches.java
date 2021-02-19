package com.derekjass.sts.weightedpaths.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.ui.config.Config;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.Collections;

public class WeightRenderPatches {

    private static final Color WEIGHT_COLOR = new Color(0x00_00_00_58);
    private static final Texture BG_TEX = new Texture("weightbg.png");
    private static final int TEX_H = BG_TEX.getHeight();
    private static final int TEX_W = BG_TEX.getWidth();
    private static final float DELTA_X = 15.0f;
    private static BitmapFont font;

    private static void drawNodeValue(MapRoomNode room, SpriteBatch sb) {
        if (WeightedPaths.roomValues.containsKey(room)) {
            double value = WeightedPaths.roomValues.get(room);
            if (Config.useColoredWeights()) {
                double greenAmt = 1.0;
                double minValue = Collections.min(WeightedPaths.roomValues.values());
                double maxValue = Collections.max(WeightedPaths.roomValues.values());
                if (maxValue - minValue > 0.01) {
                    greenAmt = (value - minValue) / (maxValue - minValue);
                }
                Color c = getColor(greenAmt);
                sb.setColor(c);
                sb.draw(BG_TEX, room.hb.cX + DELTA_X, room.hb.cY - TEX_H / 2.0f);
            }
            FontHelper.renderFontCentered(
                    sb, font, String.format("%.1f", value),
                    room.hb.cX + DELTA_X + TEX_W / 2.0f, room.hb.cY, WEIGHT_COLOR);
            if (WeightedPaths.storeGold.containsKey(room)) {
                FontHelper.renderFontCentered(
                        sb, font, String.format("%.0fg", WeightedPaths.storeGold.get(room)),
                        room.hb.cX + DELTA_X + TEX_W / 2.0f, room.hb.cY - 28.0f, WEIGHT_COLOR);
            }
        }
    }

    private static Color getColor(double value) {
        int green = (int)(value * 0xBF);
        int red = (int)((1 - value) * 0xBF);
        int blended = (red << 24) | (green << 16) | 0x38;
        return new Color(blended);
    }

    @SpirePatch(clz = MapRoomNode.class, method = "render")
    public static class PostMapRoomNodeRenderPatch {

        @SpirePostfixPatch
        public static void onMapRoomNodeRender(MapRoomNode room, SpriteBatch sb) {
            drawNodeValue(room, sb);
        }
    }

    @SpirePatch(clz = FontHelper.class, method = "initialize")
    public static class PreTipBodyFontCreationPatch {

        @SpireInsertPatch(locator = Locator.class)
        public static void onFontCreation() {
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
}
