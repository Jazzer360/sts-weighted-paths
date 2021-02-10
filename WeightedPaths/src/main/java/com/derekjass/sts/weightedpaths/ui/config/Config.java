package com.derekjass.sts.weightedpaths.ui.config;

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
    private static final String FORCE_EMERALD_KEY = "forceEmerald";
    private static SpireConfig config;

    public static void initialize() {
        Properties defaults = new Properties();
        defaults.setProperty(COLORED_WEIGHTS_KEY, "true");
        defaults.setProperty(FORCE_EMERALD_KEY, "false");
        try {
            config = new SpireConfig("WeightedPaths", "config", defaults);
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ModPanel panel = new ModPanel();
        ModLabeledToggleButton toggleColor = new ModLabeledToggleButton(
                "Show colored background on weight values.",
                400.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                config.getBool(COLORED_WEIGHTS_KEY), panel,
                (label) -> {},
                (button) -> {
                    config.setBool(COLORED_WEIGHTS_KEY, button.enabled);
                    saveConfig();
                });
        panel.addUIElement(toggleColor);
        ModLabeledToggleButton toggleEmerald = new ModLabeledToggleButton(
                "Force pathing through emerald key by act 3.",
                400.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                config.getBool(FORCE_EMERALD_KEY), panel,
                (label) -> {},
                (button) -> {
                    config.setBool(FORCE_EMERALD_KEY, button.enabled);
                    saveConfig();
                });
        panel.addUIElement(toggleEmerald);
        BaseMod.registerModBadge(new Texture("badge.png"),
                "Weighted Paths", "Derek Jass",
                "Evaluate all paths through the map and display the value on map nodes.",
                panel);
    }

    public static boolean useColoredWeights() {
        return config.getBool(COLORED_WEIGHTS_KEY);
    }

    public static boolean forceEmerald() {
        return config.getBool(FORCE_EMERALD_KEY);
    }

    private static void saveConfig() {
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
