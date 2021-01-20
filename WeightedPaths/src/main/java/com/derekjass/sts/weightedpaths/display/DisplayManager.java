package com.derekjass.sts.weightedpaths.display;

import basemod.BaseMod;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class DisplayManager implements RenderSubscriber {

    private static final Logger logger = LogManager.getLogger(DisplayManager.class.getName());
    private static final Color WEIGHT_COLOR = new Color(0x00_00_00_58);

    public DisplayManager() {
        logger.info("Subscribing DisplayManager to BaseMod.");
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        DisplayManager dm = new DisplayManager();
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (!CardCrawlGame.isInARun()) {
            return;
        }

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            for (MapRoomNode room : WeightedPaths.roomValues.keySet()) {
                drawNodeValue(room, WeightedPaths.roomValues.get(room), sb);
            }
        }
    }

    public static void drawNodeValue(MapRoomNode room, float value, SpriteBatch sb) {
        FontHelper.renderFont(sb, FontHelper.tipBodyFont, String.format("%.0f", value),
                room.hb.cX + 20, room.hb.cY, WEIGHT_COLOR);
    }
}
