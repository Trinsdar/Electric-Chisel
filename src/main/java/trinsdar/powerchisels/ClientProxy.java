package trinsdar.powerchisels;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy{

    @SideOnly(Side.CLIENT)
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
