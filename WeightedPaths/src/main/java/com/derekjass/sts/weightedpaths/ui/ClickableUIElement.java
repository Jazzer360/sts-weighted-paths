package com.derekjass.sts.weightedpaths.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;

public abstract class ClickableUIElement implements Renderable {

    private static final float HOVER_SCALE = 1.2f;

    private final Hitbox hb;
    private final Texture texture;
    private final float x, y;
    private final float xOffset, yOffset;
    private final int width, height;
    private final Color color;

    protected ClickableUIElement(Texture texture, float x, float y) {
        this(texture, x, y, Color.WHITE);
    }

    protected ClickableUIElement(Texture texture, float x, float y, Color color) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.color = color;
        width = texture.getWidth();
        height = texture.getHeight();
        xOffset = (width * HOVER_SCALE - width) / 2.0f;
        yOffset = (height * HOVER_SCALE - height) / 2.0f;
        hb = new Hitbox(x, y, width, height);
    }

    protected abstract void onClick();

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        if (hb.hovered && !hb.clickStarted) {
            sb.draw(texture, x - xOffset, y - yOffset, width * HOVER_SCALE, height * HOVER_SCALE);
        } else {
            sb.draw(texture, x, y);
        }
    }

    @Override
    public void update() {
        hb.encapsulatedUpdate((HitboxClickListener) hb -> onClick());
    }
}
