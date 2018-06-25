package com.ecs.measure;

import com.ecs.measure.Handlers.KeyInputHandler;
import com.ecs.measure.Handlers.SoundHandler;
import com.ecs.measure.Renderers.MeasureRenderer;
import com.ecs.measure.Renderers.WallHackRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Measure.MODID, name = Measure.NAME, version = Measure.VERSION)
public class Measure {
    public static final String MODID = "measure";
    public static final String NAME = "Measure";
    public static final String VERSION = "2.1";
    
    public static Minecraft minecraft;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        minecraft = FMLClientHandler.instance().getClient();
        
        MinecraftForge.EVENT_BUS.register(new WallHackRenderer());
        MinecraftForge.EVENT_BUS.register(new MeasureRenderer());
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        MinecraftForge.EVENT_BUS.register(new SoundHandler());
    }
}
