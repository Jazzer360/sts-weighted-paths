package com.derekjass.sts.weightedpaths.ui.menu;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.derekjass.sts.weightedpaths.ui.CompositeUIElement;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.Legend;

public class WeightsMenu extends CompositeUIElement implements RenderSubscriber, PostUpdateSubscriber {

    static final Color FONT_COLOR = new Color(0xD0_D0_D0_FF);
    private static final float menuX = Legend.X + 80.0f;
    private static final float menuY = 40.0f;
    private static final float ySpacing = 40.0f;

    private WeightsMenu() {
        BaseMod.subscribe(this);
        float rowY = menuY;
        addComponent(new WeightSelector(menuX, rowY, "Store (per 100g):", "$"));
        rowY += ySpacing;
        addComponent(new WeightSelector(menuX, rowY, "Rest:", "R"));
        rowY += ySpacing;
        addComponent(new WeightSelector(menuX, rowY, "Unknown:", "?"));
        rowY += ySpacing;
        addComponent(new WeightSelector(menuX, rowY, "Monsters:", "M"));
        rowY += ySpacing;
        addComponent(new WeightSelector(menuX, rowY, "Elites:", "E"));
    }

    public static void initialize() {
        new WeightsMenu();
    }

    private boolean shouldRender() {
        return AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && !WeightedPaths.roomValues.isEmpty();
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (shouldRender()) {
            render(sb);
        }
    }

    @Override
    public void receivePostUpdate() {
        if (shouldRender()) {
            update();
        }
    }
}
