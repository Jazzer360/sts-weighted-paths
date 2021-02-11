package com.derekjass.sts.weightedpaths.ui.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.ui.Renderable;
import com.megacrit.cardcrawl.helpers.FontHelper;

class LabelText implements Renderable {

    private final String label;
    private final float x, y;

    LabelText(float x, float y, String label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }

    @Override
    public void render(SpriteBatch sb) {
        FontHelper.renderFontRightAligned(sb, FontHelper.tipBodyFont, label,
                x, y + WeightArrow.height / 2, WeightsMenu.FONT_COLOR);
    }
}
