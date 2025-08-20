package com.yourname.yourmod;

import com.yourname.yourmod.entity.ModEntityTypes;
import com.yourname.yourmod.item.ModItems;
import com.yourname.yourmod.sound.ModSounds;
import com.yourname.yourmod.client.renderer.entity.SimpleCarRenderer;
import com.yourname.yourmod.client.renderer.entity.SimpleCarRenderer.CarModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(YourMod.MOD_ID)
public class YourMod {
    public static final String MOD_ID = "yourmod";
    private static final Logger LOGGER = LoggerFactory.getLogger(YourMod.class);

    public YourMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register mod content
        ModItems.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModSounds.register(modEventBus);
        
        // Register setup events
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerLayerDefinitions);
        
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Bugati Cars Mod v1.0 has been loaded!");
        LOGGER.info("Created by Maxwell Horwitz - https://github.com/ElderMars/bugati-mod");
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Bugati Cars Mod common setup complete!");
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register simple entity renderers for debugging
            EntityRenderers.register(ModEntityTypes.BUGATTI_CAR.get(), SimpleCarRenderer::new);
            EntityRenderers.register(ModEntityTypes.BUGATTI_VEYRON.get(), SimpleCarRenderer::new);
            EntityRenderers.register(ModEntityTypes.BUGATTI_DIVO.get(), SimpleCarRenderer::new);
            EntityRenderers.register(ModEntityTypes.BUGATTI_TYPE35.get(), SimpleCarRenderer::new);
            EntityRenderers.register(ModEntityTypes.BUGATTI_CENTODIECI.get(), SimpleCarRenderer::new);
            EntityRenderers.register(ModEntityTypes.BUGATTI_BOLIDE.get(), SimpleCarRenderer::new);
        });
        LOGGER.info("Bugati Cars Mod client setup complete!");
    }
    
    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SimpleCarRenderer.LAYER_LOCATION, CarModel::createBodyLayer);
    }
}
