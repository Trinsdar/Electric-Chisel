package trinsdar.nanochisel;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ItemNanoChisel.class);
    }

    public void init(FMLInitializationEvent event) {
        ItemNanoChisel.initRecipe();
    }

    public void postInit(FMLPostInitializationEvent event){
    }
}
