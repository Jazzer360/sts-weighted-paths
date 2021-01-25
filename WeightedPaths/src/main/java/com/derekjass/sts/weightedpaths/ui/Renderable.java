package com.derekjass.sts.weightedpaths.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderable {
    void render(SpriteBatch sb);
    default void update() {}
}
