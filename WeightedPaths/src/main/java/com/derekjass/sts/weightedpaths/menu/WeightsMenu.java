package com.derekjass.sts.weightedpaths.menu;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class WeightsMenu implements RenderSubscriber, PostUpdateSubscriber {

    private static final Texture leftArrow = ImageMaster.CF_LEFT_ARROW;
    private static final Texture rightArrow = ImageMaster.CF_RIGHT_ARROW;

    private final WeightSelector eliteUp;

    private WeightsMenu() {
        BaseMod.subscribe(this);
        eliteUp = new WeightSelector(rightArrow, 500, 250);
    }

    public static void initialize() {
        new WeightsMenu();
    }

    private void render(SpriteBatch sb) {
        eliteUp.render(sb);
    }

    private void update() {
        eliteUp.update();
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            sb.setColor(Color.WHITE);
            render(sb);
        }
    }

    @Override
    public void receivePostUpdate() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            update();
        }
    }
}
