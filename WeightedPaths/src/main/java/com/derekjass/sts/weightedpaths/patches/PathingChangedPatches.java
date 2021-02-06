package com.derekjass.sts.weightedpaths.patches;

import com.derekjass.sts.weightedpaths.WeightedPaths;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

@SpirePatch(clz = AbstractDungeon.class, method = "setCurrMapNode")
@SpirePatch(clz = CardCrawlGame.class, method = "getDungeon",
        paramtypez = {String.class, AbstractPlayer.class})
@SpirePatch(clz = CardCrawlGame.class, method = "getDungeon",
        paramtypez = {String.class, AbstractPlayer.class, SaveFile.class})
public class PathingChangedPatches {

    @SpirePostfixPatch
    public static void onPathingChanged() {
        WeightedPaths.regeneratePaths();
    }

}
