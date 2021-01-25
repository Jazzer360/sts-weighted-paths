package com.derekjass.sts.weightedpaths.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.derekjass.sts.weightedpaths.ui.ClickableUIElement;

public class WeightSelector extends ClickableUIElement {

    private final boolean increase;
    private final String nodeType;

    WeightSelector(Texture texture, float x, float y, String nodeType, boolean increase) {
        super(texture, x, y);
        this.increase = increase;
        this.nodeType = nodeType;
    }

    @Override
    protected void onClick() {
        double inc = isShiftPressed() ? 1.0 : 0.1;
        WeightedPaths.weights.put(nodeType, WeightedPaths.weights.get(nodeType) + (increase ? inc : -inc));
        WeightedPaths.refreshPathValues();
    }

    public static boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }
}
