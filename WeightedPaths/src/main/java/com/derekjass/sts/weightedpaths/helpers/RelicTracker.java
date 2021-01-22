package com.derekjass.sts.weightedpaths.helpers;

import basemod.BaseMod;
import basemod.interfaces.PreStartGameSubscriber;
import basemod.interfaces.RelicGetSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MawBank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class RelicTracker implements RelicGetSubscriber, PreStartGameSubscriber {

    @SpirePatch(clz = MawBank.class, method = "setCounter")
    public static class MawTracker {

        @SpirePrefixPatch
        public static void onBreak(MawBank instance, int counter) {
            if (counter == -2) {
                hasMaw = false;
                logger.info("Disabled maw bank.");
            }
        }
    }

    private static final Logger logger = LogManager.getLogger(RelicTracker.class.getName());

    public static boolean hasIdol = false, hasFace = false, hasMaw = false, hasMembership = false, hasCourier = false;

    private RelicTracker() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new RelicTracker();
    }

    public static void updateRelics(AbstractRelic relic) {
        switch (relic.relicId) {
            case "Golden Idol":
                logger.info("Golden Idol tracked.");
                hasIdol = true;
                break;
            case "SsserpentHead":
                logger.info("Ssserpent Head tracked.");
                hasFace = true;
                break;
            case "MawBank":
                logger.info("Maw Bank tracked.");
                hasMaw = true;
                break;
            case "Membership Card":
                logger.info("Membership Card tracked.");
                hasMembership = true;
                break;
            case "The Courier":
                logger.info("Courier tracked.");
                hasCourier = true;
                break;
        }
    }

    @Override
    public void receiveRelicGet(AbstractRelic relic) {
        updateRelics(relic);
    }

    @Override
    public void receivePreStartGame() {
        hasIdol = hasFace = hasMaw = hasMembership = hasCourier = false;
        logger.info("Cleared tracked relics.");
    }
}
