package com.derekjass.sts.weightedpaths.ui.menu;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.derekjass.sts.weightedpaths.ui.Renderable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.Legend;

import java.util.LinkedList;
import java.util.List;

public class WeightsMenu implements RenderSubscriber, PostUpdateSubscriber, Renderable {

    static final Color FONT_COLOR = new Color(0xD0_D0_D0_FF);
    private static final float menuX = Legend.X + 75.0f;
    private static final float menuY = 45.0f;
    private static final float ySpacing = 45.0f;

    private final List<Renderable> renderables = new LinkedList<>();

    private WeightsMenu() {
        BaseMod.subscribe(this);
        float rowY = menuY;
        renderables.add(new WeightSelector(menuX, rowY, "Store (per 100g):", "$"));
        rowY += ySpacing;
        renderables.add(new WeightSelector(menuX, rowY, "Rest:", "R"));
        rowY += ySpacing;
        renderables.add(new WeightSelector(menuX, rowY, "Unknown:", "?"));
        rowY += ySpacing;
        renderables.add(new WeightSelector(menuX, rowY, "Monsters:", "M"));
        rowY += ySpacing;
        renderables.add(new WeightSelector(menuX, rowY, "Elites:", "E"));
    }

    public static void initialize() {
        new WeightsMenu();
    }

    @Override
    public void render(SpriteBatch sb) {
        for (Renderable r : renderables) {
            r.render(sb);
        }
    }

    @Override
    public void update() {
        for (Renderable r : renderables) {
            r.update();
        }
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && !WeightedPaths.roomValues.isEmpty()) {
            sb.setColor(Color.WHITE);
            render(sb);
        }
    }

    @Override
    public void receivePostUpdate() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && !WeightedPaths.roomValues.isEmpty()) {
            update();
        }
    }
}
