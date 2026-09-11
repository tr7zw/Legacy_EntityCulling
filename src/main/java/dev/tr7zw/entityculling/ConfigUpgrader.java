package dev.tr7zw.entityculling;

public class ConfigUpgrader {

    public static boolean upgradeConfig(Config config) {
        boolean changed = false;

        // check for more changes here
        if(config.configVersion < 5) {
            config.configVersion = 5;
            changed = true;
            config.blockEntityWhitelist.add("beacon");
        }

        return changed;
    }

}
