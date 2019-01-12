package trinsdar.powerchisels;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {
    private static final String CATEGORY_GENERAL = " General";

    public static boolean defaultFluxedChiselRecipe = true;
    public static boolean defaultFluxedChiselRecipeOverride = false;
    public static boolean thermalCompat = true;
    public static boolean enderIOCompat = true;

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initConfig(cfg);
        } catch (Exception e1) {
            PowerChisels.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged())
                cfg.save();
        }
    }

    private static void initConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        defaultFluxedChiselRecipe = cfg.getBoolean("enableDefaultFluxedChiselRecipe", CATEGORY_GENERAL, defaultFluxedChiselRecipe, "Set to false to disable the default fluxed chisel recipe");
        defaultFluxedChiselRecipeOverride = cfg.getBoolean("enableDefaultFluxedChiselRecipeOverride", CATEGORY_GENERAL, defaultFluxedChiselRecipeOverride, "Set to true to force the default fluxed chisel recipe even when TE or EnderIO are loaded.");
        thermalCompat = cfg.getBoolean("enableThermalExpansionCompat", CATEGORY_GENERAL, thermalCompat, "Set to false to disable Thermal Expansion compat for the fluxed chisel recipe.");
        enderIOCompat = cfg.getBoolean("enableEnderIOCompat", CATEGORY_GENERAL, enderIOCompat, "Set to false to disable Ender IO compat for the fluxed chisel recipe.");
    }
}
