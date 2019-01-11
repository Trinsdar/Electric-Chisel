package trinsdar.powerchisels;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy{

    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (Loader.isModLoaded("ic2")){
            IC2Integration.enable();
        }
        ItemFluxedChisel.fluxedChisel.initModel();
    }

    static class IC2Integration {
        public static void enable()
        {
            ItemElectricChisel.electricChisel.initModel();
        }
    }
}
