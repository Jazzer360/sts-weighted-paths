package com.derekjass.sts.weightedpaths.ui.menu;

import com.derekjass.sts.weightedpaths.ui.CompositeUIElement;

class WeightSelector extends CompositeUIElement {

    private static final float arrowXSpacing = 74.0f;

    WeightSelector(@SuppressWarnings("SameParameterValue") float x, float y, String label, String nodeType) {
        float weightX = x + WeightArrow.width + ((arrowXSpacing - WeightArrow.width) / 2);
        addComponent(new LabelText(x, y, label));
        addComponent(new WeightArrow(WeightArrow.Action.DECREASE, x, y, nodeType));
        addComponent(new WeightText(weightX, y, nodeType));
        addComponent(new WeightArrow(WeightArrow.Action.INCREASE, x + arrowXSpacing, y, nodeType));
    }
}
