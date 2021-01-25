package com.derekjass.sts.weightedpaths.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;

public abstract class ClickableUIElement {

    private final Hitbox hb;
    private final Texture texture;
    private final float x, y;

    protected ClickableUIElement(Texture texture, float x, float y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        System.out.println("\n\n\n" + texture.getWidth() + " " + texture.getHeight());
        hb = new Hitbox(x, y, texture.getWidth(), texture.getHeight());
    }

    protected abstract void onClick();

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y);
        hb.render(sb);
    }

    public void update() {
        hb.update();
        if (hb.clicked) {
            onClick();
        }
    }
}
