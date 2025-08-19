package com.yourname.yourmod.entity;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.yourname.yourmod.YourMod;

@Mod.EventBusSubscriber(modid = YourMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityAttributes {
    
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        // Create complete attributes for all car entities
        AttributeSupplier.Builder carAttributes = AttributeSupplier.builder()
            .add(Attributes.MAX_HEALTH, 50.0D)  // 25 hearts
            .add(Attributes.MOVEMENT_SPEED, 0.0D)  // Cars don't use vanilla movement
            .add(Attributes.ARMOR, 10.0D)  // Some armor protection
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)  // Heavy cars resist knockback
            .add(Attributes.FOLLOW_RANGE, 16.0D)  // Required for Mob entities
            .add(Attributes.ATTACK_DAMAGE, 0.0D); // Cars don't attack
        
        // Register attributes for all Bugati cars
        event.put(ModEntityTypes.BUGATTI_CAR.get(), carAttributes.build());
        event.put(ModEntityTypes.BUGATTI_VEYRON.get(), carAttributes.build());
        event.put(ModEntityTypes.BUGATTI_DIVO.get(), carAttributes.build());
        event.put(ModEntityTypes.BUGATTI_TYPE35.get(), carAttributes.build());
        event.put(ModEntityTypes.BUGATTI_CENTODIECI.get(), carAttributes.build());
        event.put(ModEntityTypes.BUGATTI_BOLIDE.get(), carAttributes.build());
    }
}
