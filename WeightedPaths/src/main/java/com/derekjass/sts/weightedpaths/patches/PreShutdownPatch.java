package com.derekjass.sts.weightedpaths.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import io.sentry.Sentry;

@SpirePatch(clz = CardCrawlGame.class, method = "dispose")
public class PreShutdownPatch {

    @SpirePrefixPatch
    public static void onShutdown() {
        Sentry.endSession();
    }
}
