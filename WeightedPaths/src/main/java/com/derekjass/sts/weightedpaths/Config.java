package com.derekjass.sts.weightedpaths;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String COLORED_WEIGHTS_KEY = "coloredWeights";
    private static SpireConfig config;
    private static boolean useColoredWeights;

    static void initialize() {
        Properties defaults = new Properties();
        defaults.setProperty(COLORED_WEIGHTS_KEY, "true");
        try {
            config = new SpireConfig("WeightedPaths", "config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        useColoredWeights = config.getBool(COLORED_WEIGHTS_KEY);
        ModPanel panel = new ModPanel();
        ModLabeledToggleButton enableConsole = new ModLabeledToggleButton(
                "Show colored background on weight values.",
                400.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useColoredWeights, panel,
                (label) -> {},
                (button) -> {
                    useColoredWeights = button.enabled;
                    config.setBool(COLORED_WEIGHTS_KEY, button.enabled);
                });
        panel.addUIElement(enableConsole);
        BaseMod.registerModBadge(new Texture("badge.png"),
                "Weighted Paths", "Derek Jass",
                "Evaluate all paths through the map and display the value on map nodes.",
                panel);
    }

    public static boolean useColoredWeights() {
        return useColoredWeights;
    }
}
