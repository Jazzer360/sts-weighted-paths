package com.derekjass.sts.weightedpaths.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeUIElement implements Renderable {

    private final List<Renderable> renderables = new ArrayList<>();

    protected void addComponent(Renderable r) {
        renderables.add(r);
    }

    @Override
    public void render(SpriteBatch sb) {
        renderables.forEach(r -> r.render(sb));
    }

    @Override
    public void update() {
        renderables.forEach(Renderable::update);
    }
}
