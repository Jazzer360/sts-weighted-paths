package com.derekjass.sts.weightedpaths.ui;

import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.HitboxListener;

public interface HitboxClickListener extends HitboxListener {

    @Override
    default void hoverStarted(Hitbox hb) {}

    @Override
    default void startClicking(Hitbox hb) {}
}
