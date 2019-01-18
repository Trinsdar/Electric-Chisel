package trinsdar.powerchisels;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonProxy {
    public static Configuration config;
    public void preInit(FMLPreInitializationEvent event) {
        if (Loader.isModLoaded("ic2")){
            MinecraftForge.EVENT_BUS.register(ItemElectricChisel.class);
        }
        MinecraftForge.EVENT_BUS.register(ItemFluxedChisel.class);
        File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "powerchisels.cfg"));
        Config.readConfig();
    }

    public void init(FMLInitializationEvent event) {
        if (Loader.isModLoaded("ic2")){
            ItemElectricChisel.initRecipe();
        }
        ItemFluxedChisel.initRecipe();
    }

    public void postInit(FMLPostInitializationEvent event){
    }
}
