package trinsdar.powerchisels;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = PowerChisels.MODID, name = PowerChisels.MODNAME, version = PowerChisels.MODVERSION, dependencies = PowerChisels.DEPENDS)
public class PowerChisels {
    public static final String MODID = "powerchisels";
    public static final String MODNAME = "Power Chisels";
    public static final String MODVERSION = "@VERSION@";
    public static final String DEPENDS = "after:ic2;required-after:chisel";
    @SidedProxy(clientSide = "trinsdar.powerchisels.ClientProxy", serverSide = "trinsdar.powerchisels.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static PowerChisels instance;
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
