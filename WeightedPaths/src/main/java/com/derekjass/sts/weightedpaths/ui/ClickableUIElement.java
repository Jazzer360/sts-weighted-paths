package com.derekjass.sts.weightedpaths.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.HitboxListener;

public abstract class ClickableUIElement implements HitboxListener {

    private final Hitbox hb;
    private final Texture texture;
    private final float x, y;

    protected ClickableUIElement(Texture texture, float x, float y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        hb = new Hitbox(x, y, texture.getWidth(), texture.getHeight());
    }

    protected abstract void onClick();

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y);
        hb.render(sb);
    }

    public void update() {
        hb.encapsulatedUpdate(this);
    }

    @Override
    public void hoverStarted(Hitbox hb) {}

    @Override
    public void startClicking(Hitbox hb) {}

    @Override
    public void clicked(Hitbox hb) {
        onClick();
    }
}
