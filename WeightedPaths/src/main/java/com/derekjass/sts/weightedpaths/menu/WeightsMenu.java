package com.derekjass.sts.weightedpaths.menu;

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

    private final List<Renderable> renderables = new LinkedList<>();

    private WeightsMenu() {
        BaseMod.subscribe(this);
        float rowY = menuY;
        float leftX = menuX;
        float weightX = menuX + 60;
        float rightX = menuX + 74;
        renderables.add(new LabelText(leftX, rowY, "Store (per 100g):"));
        renderables.add(new WeightSelector(LEFT_ARROW, leftX, rowY, "$", false));
        renderables.add(new WeightText(weightX, rowY, "$"));
        renderables.add(new WeightSelector(RIGHT_ARROW, rightX, rowY, "$", true));
        rowY += ySpacing;
        renderables.add(new LabelText(leftX, rowY, "Rest:"));
        renderables.add(new WeightSelector(LEFT_ARROW, leftX, rowY, "R", false));
        renderables.add(new WeightText(weightX, rowY, "R"));
        renderables.add(new WeightSelector(RIGHT_ARROW, rightX, rowY, "R", true));
        rowY += ySpacing;
        renderables.add(new LabelText(leftX, rowY, "Unknown:"));
        renderables.add(new WeightSelector(LEFT_ARROW, leftX, rowY, "?", false));
        renderables.add(new WeightText(weightX, rowY, "?"));
        renderables.add(new WeightSelector(RIGHT_ARROW, rightX, rowY, "?", true));
        rowY += ySpacing;
        renderables.add(new LabelText(leftX, rowY, "Monsters:"));
        renderables.add(new WeightSelector(LEFT_ARROW, leftX, rowY, "M", false));
        renderables.add(new WeightText(weightX, rowY, "M"));
        renderables.add(new WeightSelector(RIGHT_ARROW, rightX, rowY, "M", true));
        rowY += ySpacing;
        renderables.add(new LabelText(leftX, rowY, "Elites:"));
        renderables.add(new WeightSelector(LEFT_ARROW, leftX, rowY, "E", false));
        renderables.add(new WeightText(weightX, rowY, "E"));
        renderables.add(new WeightSelector(RIGHT_ARROW, rightX, rowY, "E", true));
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
