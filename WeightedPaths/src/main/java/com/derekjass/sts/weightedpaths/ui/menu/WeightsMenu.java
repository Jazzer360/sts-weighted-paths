package com.derekjass.sts.weightedpaths.ui.menu;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.ui.Renderable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.Legend;

import java.util.LinkedList;
import java.util.List;

public class WeightsMenu implements RenderSubscriber, PostUpdateSubscriber {

    static final Color FONT_COLOR = new Color(0xD0_D0_D0_FF);
    private static final Texture LEFT_ARROW = ImageMaster.CF_LEFT_ARROW;
    private static final Texture RIGHT_ARROW = ImageMaster.CF_RIGHT_ARROW;
    private static final float menuX = Legend.X + 75.0f;
    private static final float menuY = 45.0f;
    private static final float ySpacing = 45.0f;
    private static final float arrowXSpacing = 74.0f;

    private final List<Renderable> renderables = new LinkedList<>();

    private WeightsMenu() {
        BaseMod.subscribe(this);
        float rowY = menuY;
        createWidgetRow(rowY, "Store (per 100g):", "$");
        rowY += ySpacing;
        createWidgetRow(rowY, "Rest:", "R");
        rowY += ySpacing;
        createWidgetRow(rowY, "Unknown:", "?");
        rowY += ySpacing;
        createWidgetRow(rowY, "Monsters:", "M");
        rowY += ySpacing;
        createWidgetRow(rowY, "Elites:", "E");
    }

    private void createWidgetRow(float y, String label, String nodeType) {
        float weightX = menuX + LEFT_ARROW.getWidth() + ((arrowXSpacing - LEFT_ARROW.getWidth()) / 2);
        renderables.add(new LabelText(menuX, y, label));
        renderables.add(new WeightSelector(LEFT_ARROW, menuX, y, nodeType, false));
        renderables.add(new WeightText(weightX, y, nodeType));
        renderables.add(new WeightSelector(RIGHT_ARROW, menuX + arrowXSpacing, y, nodeType, true));
    }

    public static void initialize() {
        new WeightsMenu();
    }

    private void render(SpriteBatch sb) {
        for (Renderable r : renderables) {
            r.render(sb);
        }
    }

    private void update() {
        for (Renderable r : renderables) {
            r.update();
        }
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
