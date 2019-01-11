package trinsdar.powerchisels;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        if (Loader.isModLoaded("ic2")){
            MinecraftForge.EVENT_BUS.register(ItemElectricChisel.class);
        }
        MinecraftForge.EVENT_BUS.register(ItemFluxedChisel.class);
    }

    public void init(FMLInitializationEvent event) {
        if (Loader.isModLoaded("ic2")){
            ItemElectricChisel.initRecipe();
        }

    }

    public void postInit(FMLPostInitializationEvent event){
    }
}
