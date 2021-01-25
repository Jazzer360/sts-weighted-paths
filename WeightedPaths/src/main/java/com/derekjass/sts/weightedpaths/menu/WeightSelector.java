package com.derekjass.sts.weightedpaths.menu;

import com.badlogic.gdx.graphics.Texture;
import com.derekjass.sts.weightedpaths.ui.ClickableUIElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WeightSelector extends ClickableUIElement {

    private static final Logger logger = LogManager.getLogger(WeightSelector.class.getName());

    public WeightSelector(Texture texture, float x, float y) {
        super(texture, x, y);
    }

    @Override
    protected void onClick() {
        logger.info("CLICKED!");
    }
}
