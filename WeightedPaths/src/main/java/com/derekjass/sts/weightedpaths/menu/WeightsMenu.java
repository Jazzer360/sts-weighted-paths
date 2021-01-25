package com.derekjass.sts.weightedpaths.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class WeightsMenu {

    private static final Texture leftArrow = ImageMaster.CF_LEFT_ARROW;
    private static final Texture rightArrow = ImageMaster.CF_RIGHT_ARROW;

    public static WeightsMenu menu;
    private final WeightSelector eliteUp;

    private WeightsMenu() {
        eliteUp = new WeightSelector(rightArrow, 500, 250);
    }

    public static void initialize() {
        menu = new WeightsMenu();
    }

    public void render(SpriteBatch sb) {
//        Color c = sb.getColor();
//        sb.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        eliteUp.render(sb);
//        sb.setColor(c);
    }

    public void update() {
        eliteUp.update();
    }
}
