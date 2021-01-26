package com.derekjass.sts.weightedpaths.helpers;

import basemod.BaseMod;
import basemod.interfaces.PreStartGameSubscriber;
import basemod.interfaces.RelicGetSubscriber;
import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MawBank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RelicTracker implements RelicGetSubscriber, PreStartGameSubscriber {

    @SpirePatch(clz = MawBank.class, method = "setCounter")
    public static class PreMawSetCounterPatch {

        @SpirePrefixPatch
        public static void onMawSetCounter(MawBank instance, int counter) {
            if (counter == -2) {
                updateRelicsAndRefreshPathValues("MawBank", false);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "loseRelic")
    public static class PostLoseRelicPatch {

        @SpirePostfixPatch
        public static void onLoseRelic(AbstractPlayer instance, String id) {
            updateRelicsAndRefreshPathValues(id, false);
        }
    }

    private static final Logger logger = LogManager.getLogger(RelicTracker.class.getName());

    public static boolean hasIdol = false, hasFace = false, hasMaw = false, hasMembership = false, hasCourier = false,
            hasEcto = false;

    private RelicTracker() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new RelicTracker();
    }

    private static boolean updateRelics(String relicId, boolean add) {
        switch (relicId) {
            case "Golden Idol":
                logger.info(String.format("Has Golden Idol %b.", add));
                hasIdol = add;
                return true;
            case "SsserpentHead":
                logger.info(String.format("Has Ssserpent Head %b.", add));
                hasFace = add;
                return true;
            case "MawBank":
                logger.info(String.format("Has Maw Bank %b.", add));
                hasMaw = add;
                return true;
            case "Membership Card":
                logger.info(String.format("Has Membership Card %b.", add));
                hasMembership = add;
                return true;
            case "The Courier":
                logger.info(String.format("Has Courier %b.", add));
                hasCourier = add;
                return true;
            case "Ectoplasm":
                logger.info(String.format("Has Ectoplasm %b.", add));
                hasEcto = add;
                return true;
        }
        return false;
    }

    private static void updateRelicsAndRefreshPathValues(String relicId, boolean add) {
        if (updateRelics(relicId, add)) {
            WeightedPaths.refreshPathValues();
        }
    }

    @Override
    public void receiveRelicGet(AbstractRelic relic) {
        updateRelicsAndRefreshPathValues(relic.relicId, true);
    }

    @Override
    public void receivePreStartGame() {
        hasIdol = hasFace = hasMaw = hasMembership = hasCourier = hasEcto = false;
        logger.info("Cleared tracked relics.");
    }
}
