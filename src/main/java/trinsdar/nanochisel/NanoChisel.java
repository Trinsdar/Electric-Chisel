package trinsdar.nanochisel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = NanoChisel.MODID, name = NanoChisel.MODNAME, version = NanoChisel.MODVERSION, dependencies = NanoChisel.DEPENDS)
public class NanoChisel {
    public static final String MODID = "nanochisel";
    public static final String MODNAME = "Nano Chisel";
    public static final String MODVERSION = "@VERSION@";
    public static final String DEPENDS = "required-after:ic2;required-after:chisel";
    @SidedProxy(clientSide = "trinsdar.nanochisel.ClientProxy", serverSide = "trinsdar.nanochisel.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static NanoChisel instance;
    public static Logger logger;


    @Mod.EventHandler
    public synchronized void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
