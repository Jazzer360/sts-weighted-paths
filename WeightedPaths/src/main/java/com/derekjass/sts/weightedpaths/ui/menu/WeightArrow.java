package com.derekjass.sts.weightedpaths.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.derekjass.sts.weightedpaths.ui.ClickableUIElement;
import com.megacrit.cardcrawl.helpers.ImageMaster;

class WeightArrow extends ClickableUIElement {

    enum Action {
        DECREASE(ImageMaster.CF_LEFT_ARROW),
        INCREASE(ImageMaster.CF_RIGHT_ARROW);

        private final Texture texture;

        Action(Texture texture) {
            this.texture = texture;
        }
    }

    static final float width = Action.DECREASE.texture.getWidth();
    static final float height = Action.DECREASE.texture.getHeight();

    private final boolean increase;
    private final String nodeType;

    WeightArrow(Action dir, float x, float y, String nodeType) {
        super(dir.texture, x, y);
        increase = dir == Action.INCREASE;
        this.nodeType = nodeType;
    }

    @Override
    protected void onClick() {
        double inc = isShiftPressed() ? 1.0 : 0.1;
        WeightedPaths.weights.put(nodeType, WeightedPaths.weights.get(nodeType) + (increase ? inc : -inc));
        WeightedPaths.refreshPathValues();
    }

    private static boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }
}
