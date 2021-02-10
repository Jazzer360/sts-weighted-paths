package com.derekjass.sts.weightedpaths.ui.menu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.derekjass.sts.weightedpaths.ui.Renderable;

import java.util.ArrayList;
import java.util.List;

class WeightSelector implements Renderable {

    private static final float arrowXSpacing = 74.0f;

    private final List<Renderable> renderables = new ArrayList<>();

    WeightSelector(@SuppressWarnings("SameParameterValue") float x, float y, String label, String nodeType) {
        float weightX = x + WeightArrow.width + ((arrowXSpacing - WeightArrow.width) / 2);
        renderables.add(new LabelText(x, y, label));
        renderables.add(new WeightArrow(WeightArrow.Direction.LEFT, x, y, nodeType));
        renderables.add(new WeightText(weightX, y, nodeType));
        renderables.add(new WeightArrow(WeightArrow.Direction.RIGHT, x + arrowXSpacing, y, nodeType));
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
}
